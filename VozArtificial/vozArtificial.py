import pyttsx3
from flask import Flask, jsonify, request
import threading

# Crear un objeto de motor de texto a voz
engine = pyttsx3.init()
app = Flask(__name__)



@app.route('/receive-move', methods=['POST'])
def receive_move():
    # Recibe los datos del movimiento en formato JSON
    data = request.get_json()
    col = data['mensaje']

    # Convierte el movimiento en voz
    mensaje = f"Mi siguiente movimiento es en la columna numero {col}"
    engine.say(mensaje)

    # Verifica si el motor ya está en ejecución
    if engine._inLoop:
        engine.endLoop()

    # Ejecuta engine.runAndWait() en un hilo separado
    threading.Thread(target=engine.runAndWait).start()

    return jsonify({'status': 'success'}), 200



app.run(host='localhost', port=5001)
