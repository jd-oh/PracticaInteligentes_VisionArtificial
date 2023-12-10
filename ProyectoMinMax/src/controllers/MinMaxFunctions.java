/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package controllers;

import java.util.List;


/**
 *
 * @author Jeffrey
 * @author Juan David
 */
public class MinMaxFunctions {
    
       public int[] encontrarMejorMovimiento(List<List<String>> tablero, String jugador) {
    int[] mejorMovimiento = null;
    int mejorValor = (jugador.equals("C")) ? Integer.MIN_VALUE : Integer.MAX_VALUE;

    // Iterar sobre todas las posiciones vacías y evaluar el valor minimax
    for (int i = 0; i < tablero.size(); i++) {
        for (int j = 0; j < tablero.get(i).size(); j++) {
            if (tablero.get(i).get(j).equals(" ") && i == 2) {
                tablero.get(i).set(j, jugador);
                int valor = minimax(tablero, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, !jugador.equals("C"));
                tablero.get(i).set(j, " "); // Deshacer el movimiento

                // Actualizar si se encuentra un valor mejor
                if ((jugador.equals("C") && valor > mejorValor) || (jugador.equals("T") && valor < mejorValor)) {
                    mejorValor = valor;
                    mejorMovimiento = new int[]{i, j};
                }
            }else if (tablero.get(i).get(j).equals(" ") && !tablero.get(i+1).get(j).equals(" ")){
                tablero.get(i).set(j, jugador);
                int valor = minimax(tablero, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, !jugador.equals("C"));
                tablero.get(i).set(j, " "); // Deshacer el movimiento

                // Actualizar si se encuentra un valor mejor
                if ((jugador.equals("C") && valor > mejorValor) || (jugador.equals("T") && valor < mejorValor)) {
                    mejorValor = valor;
                    mejorMovimiento = new int[]{i, j};
                }
            }
        }
    }
    return mejorMovimiento;
}

   private int minimax(List<List<String>> tablero, int profundidad, int alfa, int beta, boolean esMaximizando) {
    if (esVictoria(tablero, "C")) {
        return 10 - profundidad;
    } else if (esVictoria(tablero, "T")) {
        return profundidad - 10;
    } else if (esEmpate(tablero)) {
        return 0;
    }

    // Continuar la búsqueda en el árbol
    if (esMaximizando) {
        int mejorValor = Integer.MIN_VALUE;
        for (int i = 0; i < tablero.size(); i++) {
            for (int j = 0; j < tablero.get(i).size(); j++) {
                if (tablero.get(i).get(j).equals(" ")) {
                    tablero.get(i).set(j, "C");
                    mejorValor = Math.max(mejorValor, minimax(tablero, profundidad + 1, alfa, beta, false));
                    tablero.get(i).set(j, " "); // Deshacer el movimiento

                    alfa = Math.max(alfa, mejorValor);
                    if (beta <= alfa) {
                        break;
                    }
                }
            }
        }
        return mejorValor;
    } else {
        int mejorValor = Integer.MAX_VALUE;
        for (int i = 0; i < tablero.size(); i++) {
            for (int j = 0; j < tablero.get(i).size(); j++) {
                if (tablero.get(i).get(j).equals(" ")) {
                    tablero.get(i).set(j, "T");
                    mejorValor = Math.min(mejorValor, minimax(tablero, profundidad + 1, alfa, beta, true));
                    tablero.get(i).set(j, " "); // Deshacer el movimiento

                    beta = Math.min(beta, mejorValor);
                    if (beta <= alfa) {
                        break;
                    }
                }
            }
        }
        return mejorValor;
    }
}
   

    private static boolean esVictoria(List<List<String>> tablero, String jugador) {
    // Verificar filas
    for (List<String> fila : tablero) {
        if (fila.stream().allMatch(casilla -> casilla.equals(jugador))) {
            return true;
        }
    }

    // Verificar columnas
    for (int i = 0; i < tablero.get(0).size(); i++) {
        final int columna = i; // Capturar el valor de i en una variable final
        if (tablero.stream().allMatch(fila -> fila.get(columna).equals(jugador))) {
            return true;
        }
    }

//    // Verificar diagonal principal
//    if (tablero.get(0).get(0).equals(jugador) &&
//        tablero.get(1).get(1).equals(jugador) &&
//        tablero.get(2).get(2).equals(jugador)) {
//        return true;
//    }

    // Verificar diagonal secundaria
//    if (tablero.get(0).get(2).equals(jugador) &&
//        tablero.get(1).get(1).equals(jugador) &&
//        tablero.get(2).get(0).equals(jugador)) {
//        return true;
//    }

    return false;
}

    private static boolean esEmpate(List<List<String>> tablero) {
    for (List<String> fila : tablero) {
        if (fila.contains(" ")) {
            return false; // Todavía hay al menos una casilla vacía, no es empate
        }
    }
    return true; // Todas las casillas están ocupadas, es empate
}
}
