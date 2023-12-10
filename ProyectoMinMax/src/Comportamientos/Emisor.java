/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Comportamientos;

import com.fasterxml.jackson.databind.ObjectMapper;
import controllers.MinMaxFunctions;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.List;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
            // Crear una URL y abrir la conexión
            URL url = new URL(apiUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            // Configurar el tipo de solicitud y método
            con.setRequestMethod("GET");

            // Obtener la respuesta de la API
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Leer la respuesta de la API
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Guardar la respuesta en la variable "respuesta"
                respuesta = response.toString();

                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    List<List<String>> tablero = objectMapper.readValue(respuesta, List.class);
                    MinMaxFunctions function = new MinMaxFunctions();

                    // Imprimir el tablero
                    System.out.println("Tablero Actual:");
                    imprimirTablero(tablero);

                    // Calcular la próxima jugada usando el algoritmo minimax
                    int[] movimiento = function.encontrarMejorMovimiento(tablero, "C");

                    // Imprimir la próxima jugada
                    System.out.println("\nPróxima Jugada:");
                    System.out.println("Fila: " + movimiento[0] + ", Columna: " + movimiento[1]);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                System.out.println("La solicitud GET no fue exitosa. Código de respuesta: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void anunciarJugada() {

    }

    private static void imprimirTablero(List<List<String>> tablero) {
        for (List<String> fila : tablero) {
            System.out.println(fila);
        }
    }

}
