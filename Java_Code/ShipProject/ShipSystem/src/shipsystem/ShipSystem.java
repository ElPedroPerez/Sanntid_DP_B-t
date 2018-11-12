/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shipsystem;

import SerialCom.SerialDataHandler;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Haakon
 */
public class ShipSystem
{

    protected static DataHandler dh;
    private static Thread alarmList;
    private static Thread controller;
    private static Thread server;
    private static Thread serialDataHandler;
    private static Semaphore semaphore;
    static SendEventState enumStateEvent;

    protected static String ipAdress = "158.38.199.132";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        semaphore = new Semaphore(1, true);

        dh = new DataHandler();
        dh.setThreadStatus(true);

        serialDataHandler = new Thread(new SerialDataHandler(dh));

        controller = new Thread(new Controller(dh, semaphore));
        server = new Thread(new UDPServer(semaphore, dh));

        controller.start();
        server.start();
        serialDataHandler.start();

        int fb_podPosPS = 0;
        int fb_podPosSB = 0;
        int fb_speedPS = 0;
        int fb_speedSB = 0;

        int yaw = 0;
        int pitch = 0;
        int roll = 0;

//        // bRYNJARS testområde
//        while (true)
//        {
//            try
//            {
//                System.out.println("Angle from IC: " + dh.getTemp_Angle());
//                Thread.sleep(250);
//            } catch (InterruptedException ex)
//            {
//                Logger.getLogger(ShipSystem.class.getName()).log(Level.SEVERE, null, ex);
//            }

//        //Robins test area
//        dh.handleDataFromArduino();
//        while (true)
//        {
//
//
//            yaw = dh.getYaw();
//            pitch = dh.getPitch();
//            roll = dh.getRoll();
//
//            fb_podPosPS = dh.getFb_podPosPS();
//            fb_podPosSB = dh.getFb_podPosSB();
//            fb_speedPS = dh.getFb_speedPS();
//            fb_speedSB = dh.getFb_podPosSB();
//
//            System.out.println("fb_podPosPS is: " + fb_podPosPS);
//            System.out.println("fb_podPosSB is: " + fb_podPosSB);
//            System.out.println("fb_speedPS is: " + fb_speedPS);
//            System.out.println("fb_speedSB is: " + fb_speedSB);
//
//            System.out.println("Yaw is: " + yaw);
//            System.out.println("Pitch is: " + pitch);
//            System.out.println("Roll is: " + roll);
//            
//            System.out.println("Com response time: " + dh.getComResponseTime());
//                }
//End of Robins test area
//        controller = new Thread(new Controller(dh, semaphore));
//        server = new Thread(new UDPServer(semaphore, dh));        
//        alarmList = new Thread(new alarmsystem.AlarmList(dh));
//
//        controller.start();
//        server.start();
//        alarmList.start();
//
//        }

    }
}
