/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shipsystem;

import SerialCom.SerialDataHandler;
import alarmsystem.AlarmList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main class of the shipsystem.
 *
 * @author Haakon, Bjørnar, Robin
 */
public class ShipSystem
{

    /**
     * Main class of the ship system
     */
    protected static DataHandler dh;
    private static Thread acclerationFilter;
    private static Thread alarmList;
    private static Thread controller;
    private static Thread guiSender;
    private static Thread serialDataHandler;
    private static Thread udpListener;

    /**
     * IP address and port number
     */
    protected static String ipAddressGUI = "158.38.92.72"; // Bjørnar: "158.38.199.111", Håkon: "158.38.85.64", Robin: "158.38.85.192"
    protected static int sendPort = 5057;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        dh = new DataHandler();
        dh.setThreadStatus(true);

        acclerationFilter = new Thread(new AccelerationFilter(dh));
        guiSender = new Thread(new GUIsender(dh));
        udpListener = new Thread(new UDPListener(dh));
        serialDataHandler = new Thread(new SerialDataHandler(dh));
        controller = new Thread(new Controller(dh));
        //alarmList = new Thread(new AlarmList(dh));

        controller.setName("Controller");
        guiSender.setName("GUIsender");
        udpListener.setName("UDPListener");
        acclerationFilter.setName("AccelerationFilter");
        serialDataHandler.setName("SerialDataHandler");
        //alarmList.setName("AlarmList");

        controller.start();
        guiSender.start();
        acclerationFilter.start();
        udpListener.start();
        serialDataHandler.start();
        //alarmList.start();

        int fb_podPosPS = 0;
        int fb_podPosSB = 0;
        int fb_speedPS = 0;
        int fb_speedSB = 0;

        int yaw = 0;
        int pitch = 0;
        int roll = 0;

        while (true)
        {
            try
            {
                System.out.println("A: " + dh.ic_A);
                //System.out.println("dh.setTemp_Angle: " + dh.getTemp_Angle());
                System.out.println("R1: " + dh.ic_R1);
                System.out.println("Angle PS POD: " + dh.fb_podPosPS);
                System.out.println("Speed PS: " + dh.fb_speedPS);
                System.out.println("Speed SB: " + dh.fb_speedSB);
                Thread.sleep(250);
            } catch (Exception e)
            {
            }
        }
    }
}
