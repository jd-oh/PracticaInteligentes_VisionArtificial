/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Comportamientos;

import com.fasterxml.jackson.databind.ObjectMapper;
import controllers.MinMaxFunctions;

import java.util.List;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author CSS
 */
public class Emisor {

    String respuesta;

    public Emisor() {

    }

    public String getRespuesta() {
        return respuesta;
    }

    public void RealizarJugada() {
        String apiUrl = "http://127.0.0.1:5000/get-game-state";

        try {

            URL url = new URL(apiUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                if (respuesta != response.toString()) {

                    respuesta = response.toString();

                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        List<List<String>> tablero = objectMapper.readValue(respuesta, List.class);
                        MinMaxFunctions function = new MinMaxFunctions();

                        System.out.println("Tablero Actual:");
                        imprimirTablero(tablero);

                        // Calcular la próxima jugada usando el algoritmo minimax
                        int[] movimiento = function.encontrarMejorMovimiento(tablero, "C");
                        
                        String jugada = "Fila: " + movimiento[0] + ", Columna: " + movimiento[1];
                        this.anunciarJugada(movimiento[1]);
                        
                        System.out.println("\nPróxima Jugada:");
                        System.out.println(jugada);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                System.out.println("La solicitud GET no fue exitosa. Código de respuesta: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void anunciarJugada(String jugada) {
        String apiUrl = "http://127.0.0.1:5001/receive-move";

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");

            // Habilitar el envío de datos
            con.setDoOutput(true);

            // Datos que deseas enviar en el cuerpo de la solicitud (cambia según tus necesidades)
            String datos = "{\"mensaje\":\"" + jugada + "\"}";

            // Obtener el flujo de salida y escribir los datos en el cuerpo de la solicitud
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = datos.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Obtener la respuesta de la API
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void imprimirTablero(List<List<String>> tablero) {
        for (List<String> fila : tablero) {
            System.out.println(fila);
        }
    }

}
