/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guisystem;

import InputController.InputController;
import gui.GUI;
//import guisystem.Datahandler;

/**
 *
 * @author Haakon
 */
public class GUISystem
{

    
    private static UDPsender udpsender;
    static final String IPADDRESS = "158.38.92.52"; //"192.168.0.101"; //"10.16.4.27"; //"192.168.0.103";  //Fugl"158.38.199.58";  // Jørg"10.16.5.58";
    static final int RECEIVEPORT = 5057;
    static final int SENDPORT = 5056; //9876
    public static Datahandler dh;
    public static InputController inputController;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        GUISystem.udpsender = new UDPsender();
        dh = new Datahandler();
        GUI gui = new GUI();
        //  InputController inputController = new InputController(IPADDRESS, SENDPORT, dh);
        UDPping udpPing = new UDPping(IPADDRESS, SENDPORT, dh);
        UDPListener udpListener = new UDPListener(dh, RECEIVEPORT );
        inputController = new InputController(IPADDRESS, SENDPORT, dh);

        Thread guiThread = new Thread(gui);
        Thread inputControllerThread = new Thread(inputController);
        Thread udpPingThread = new Thread(udpPing);
        Thread udpListenerThread = new Thread(udpListener);

        //guiThread.start();
        
        //inputControllerThread.start();
        udpPingThread.start();
        udpListenerThread.start();
        int count = 0;
        while (true)
        {
            try
            {
                Thread.sleep(200);
               // udpsender.send("localhost", "<Test:" + count + ">", SENDPORT);
                if (dh.getPing() == 0)
                {
                    System.out.println("No connection");
                    System.out.println("Thread count: " + java.lang.Thread.activeCount());
                } else
                {
                    System.out.println("Ping: " + dh.getPing() +"ms");
                }
//                count = count + 1;
//                if (count > 1000)
//                {
//                    count = 0;
//                }
            } catch (Exception e)
            {
            }

        }

        // Using observer pattern for updating GUI
        //dh.addObserver(gui);  // Adds the GUI as observer
    }

}
