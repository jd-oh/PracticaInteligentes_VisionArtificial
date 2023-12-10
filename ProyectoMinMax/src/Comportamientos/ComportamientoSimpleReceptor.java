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

/**
 *
 * @author CSS
 */
public class ComportamientoSimpleReceptor extends SimpleBehaviour {

    Agent miAgente;
    String respuesta = "";

    public ComportamientoSimpleReceptor(Agent miAgente) {
        this.miAgente = miAgente;
    }

    public String getRespuesta() {
        return respuesta;
    }

    @Override
    public void action() {
        ACLMessage mensajeRecibido = this.miAgente.blockingReceive();
        if (mensajeRecibido != null) {
            try {
                
                respuesta = mensajeRecibido.getContent();
                
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
            } catch (Exception e) {
            }
        } else {
            System.out.println("Receptor no me ha llegado mensaje");
        }
    }
    
     private static void imprimirTablero(List<List<String>> tablero) {
        for (List<String> fila : tablero) {
            System.out.println(fila);
        }
    }

    @Override
    public boolean done() {
        return true;
    }

}
