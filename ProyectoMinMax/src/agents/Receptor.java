/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agents;

import jade.core.Agent;
import Comportamientos.ComportamientoSimpleReceptor;

/**
 *
 * @author CSS
 */
public class Receptor extends Agent{
    public void setup(){
        addBehaviour(new ComportamientoSimpleReceptor(this));
    }
}
