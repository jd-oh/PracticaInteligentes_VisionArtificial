/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package proyectominmax;

import agents.Receptor;
import controllers.MinMaxFunctions;
import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @author CSS
 */
public class ProyectoMinMax {

    
    
    public static void main(String[] args) {
       
        Receptor receptor = new Receptor();
        
//        String jsonString = "[[\" \", \" \", \" \"], [\" \", \" \", \" \"], [\"C\", \"T\", \" \"]]";
//       //String jsonString = "[[\" \", \" \", \" \"], [\"X\", \" \", \" \"], [\" \", \"O\", \" \"]]";
//       //String jsonString = "[[\"X\", \" \", \" \"], [\"X\", \" \", \" \"], [\"O\", \"O\", \" \"]]";
//       //String jsonString = "[[\"X\", \" \", \" \"], [\"X\", \" \", \" \"], [\" \", \"O\", \"O\"]]";
//       //String jsonString = "[[\"X\", \" \", \" \"], [\"X\", \"O\", \" \"], [\"O\", \"O\", \"X\"]]";
//
//        // Convertir el JSON a una lista de listas
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            List<List<String>> tablero = objectMapper.readValue(jsonString, List.class);
//            MinMaxFunctions function = new MinMaxFunctions();
//
//            // Imprimir el tablero
//            System.out.println("Tablero Actual:");
//            imprimirTablero(tablero);
//
//            // Calcular la próxima jugada usando el algoritmo minimax
//            int[] movimiento = function.encontrarMejorMovimiento(tablero, "C");
//
//            // Imprimir la próxima jugada
//            System.out.println("\nPróxima Jugada:");
//            System.out.println("Fila: " + movimiento[0] + ", Columna: " + movimiento[1]);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
    
   
    
}
