/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guisystem;

import InputController.InputController;
import guisystem.Datahandler;

/**
 *
 * @author Haakon
 */
public class GUISystem
{

    static final String IPADDRESS = "158.38.199.20"; //"192.168.0.101"; //"10.16.4.27"; //"192.168.0.103";  //Fugl"158.38.199.58";  // Jørg"10.16.5.58";
    static final int RECEIVEPORT = 9877;
    static final int SENDPORT = 5056; //9876
    private static Datahandler dh;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        dh = new Datahandler();

        InputController inputController = new InputController(IPADDRESS, SENDPORT, dh);
        GUI gui = new GUI();

        Thread inputControllerThread = new Thread(inputController);
        Thread guiThread = new Thread(gui);

        inputControllerThread.start();
        guiThread.start();

        // Using observer pattern for updating GUI
        dh.addObserver(gui);  // Adds the GUI as observer
        dh.addObserver(gui);  // Adds the InputController as observer
    }

}
