/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guisystem;

import InputController.InputController;

/**
 *
 * @author Haakon
 */
public class GUISystem
{

    static final String IPADDRESS = "158.38.199.20"; //"192.168.0.101"; //"10.16.4.27"; //"192.168.0.103";  //Fugl"158.38.199.58";  // Jørg"10.16.5.58";
    static final int RECEIVEPORT = 9877;
    static final int SENDPORT = 5056; //9876

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        Thread inputController = new Thread(new InputController(IPADDRESS, SENDPORT));
        inputController.start();
    }

}
