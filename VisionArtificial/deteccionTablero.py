import time
import cv2
from flask import Flask, jsonify
import numpy as np
from collections import Counter
import tkinter as tk
import threading

# Clase para detectar y clasificar formas en un tablero de juego
class BoardDetector:
    def __init__(self, camera_index=0):
        # Inicializa la cámara y establece la cuadrícula del tablero de juego
        self.cap = cv2.VideoCapture(camera_index)
        self.cap=cv2.VideoCapture("http://192.168.20.32:4747/video")
        self.grid = [[' ']*3 for _ in range(3)]  # Asume una cuadrícula 3x3

    # Captura un solo fotograma de la cámara
    def get_frame(self):
        ret, frame = self.cap.read()
        return frame

    # Encuentra el número de jerarquía más repetido en los contornos detectados en una imagen
    def getMoreRepetitiveHierarchy(self, contours, hierarchy):
        jerarquias = []
        for i, contour in enumerate(contours):
            jerarquias.append(hierarchy[0][i][3])
        report = Counter(jerarquias)
        number, repetitions = report.most_common(1)[0]
        return number, repetitions

    # Realiza el procesamiento de imágenes para detectar y clasificar formas
    def detect_grid(self, frame):
        # Convierte la imagen a escala de grises, la difumina y luego utiliza el detector de bordes Canny para encontrar los bordes en la imagen
        gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
        blurred = cv2.GaussianBlur(gray, (5, 5), 0)
        edges = cv2.Canny(blurred, threshold1=50, threshold2=150)
        # Encuentra los contornos en la imagen
        contours, hierarchy = cv2.findContours(edges, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)
        # Obtiene el número de jerarquía más repetido
        hierarchyNumber, _ = self.getMoreRepetitiveHierarchy(contours, hierarchy)
        # Filtra los contornos para encontrar los cuadrados internos en el tablero de juego y luego los dibuja en la imagen
        inner_squares = [contour for i, contour in enumerate(contours) if hierarchy[0][i][3] == hierarchyNumber and cv2.contourArea(contour) > 15000]
        cv2.drawContours(frame, inner_squares, -1, (255, 0, 0), 3)

        # Detecta y dibuja cuadrados y triángulos internos
        inner_shapes = [contour for i, contour in enumerate(contours) if hierarchy[0][i][3] == hierarchyNumber and 1000 < cv2.contourArea(contour) < 15000]
        for shape in inner_shapes:
            # Calcula el perímetro y utiliza approxPolyDP para simplificar el contorno
            peri = cv2.arcLength(shape, True)
            approx = cv2.approxPolyDP(shape, 0.02 * peri, True)
            # Usa los momentos de la forma para encontrar su centroide y determinar su posición en la cuadrícula del tablero de juego
            M = cv2.moments(shape)
            if M["m00"] != 0:
                cX = int(M["m10"] / M["m00"])
                cY = int(M["m01"] / M["m00"])
                grid_pos = (cY // (frame.shape[0]//3), cX // (frame.shape[1]//3))
                # Clasifica la forma como un cuadrado o un triángulo y actualiza el estado del tablero de juego en consecuencia
                if len(approx) == 3:
                    color = (0, 255, 0)  # Verde para triángulos
                    self.grid[grid_pos[0]][grid_pos[1]] = 'T'
                elif len(approx) == 4:
                    color = (0, 0, 255)  # Rojo para cuadrados
                    self.grid[grid_pos[0]][grid_pos[1]] = 'C'
                else:
                    color = (0, 255, 255)  # Amarillo para otros
                cv2.drawContours(frame, [shape], -1, color, 2)

        return frame, self.grid

    # Libera la cámara cuando ya no se necesita
    def release(self):
        self.cap.release()

# Crea una instancia de BoardDetector
detector = BoardDetector()

# Reinicializa la cuadrícula del tablero de juego
def update_grid():
    detector.grid = [[' ']*3 for _ in range(3)]  
    print(detector.grid)

# Devuelve el estado actual del tablero de juego
def send_matrix():
    return detector.grid

# Ejecuta la detección de formas en un bucle
def run_camera():
    while True:
        frame = detector.get_frame()
        processed_image, grid = detector.detect_grid(frame)
        cv2.imshow('Tablero', processed_image)
        print(grid)
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

# Inicia un servidor Flask para proporcionar una API que devuelve el estado actual del tablero de juego
def run_flask_app():
    app = Flask(__name__)

    @app.route('/get-game-state', methods=['GET'])
    def get_game_state():
        grid = send_matrix()
        return jsonify(grid)

    app.run(host='localhost', port=5000)

# Crea un botón para actualizar la cuadrícula del tablero de juego si es necesario
root = tk.Tk()
buttonUpdate = tk.Button(root, text="Actualizar", command=update_grid)
buttonUpdate.pack()

# Inicia la detección de formas y el servidor Flask en hilos separados
camera_thread = threading.Thread(target=run_camera)
camera_thread.start()

flask_thread = threading.Thread(target=run_flask_app)
flask_thread.start()

# Mantiene la interfaz de usuario en ejecución hasta que se cierre
root.mainloop()

# Libera la cámara y cierra todas las ventanas de OpenCV cuando ya no se necesitan
detector.release()
cv2.destroyAllWindows()
