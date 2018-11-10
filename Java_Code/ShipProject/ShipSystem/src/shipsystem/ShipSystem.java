/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shipsystem;

import java.util.concurrent.Semaphore;

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
    private static Semaphore semaphore;
    static SendEventState enumStateEvent;
    protected static String ipAdress = "localhost";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        semaphore = new Semaphore(1, true);

        dh = new DataHandler();
        dh.setThreadStatus(true);

        controller = new Thread(new Controller(dh, semaphore));
        server = new Thread(new UDPServer(semaphore, dh));

        controller.start();
        server.start();

        double fb_podPosPS = 0;
        double fb_podPosSB = 0;
        double fb_speedPS = 0;
        double fb_speedSB = 0;

        int yaw = 0;
        int pitch = 0;
        int roll = 0;

        while (true)
        {
//            long lastTime = System.nanoTime();
//            dh.handleDataFromArduino();
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
//            long elapsedTimer = (System.nanoTime() - lastTime) / 1000000;
//            if (elapsedTimer != 0)
//            {
//              System.out.println("Data is gøtt'n in: " + elapsedTimer + " millis"
//                        + " or with: " + 1000 / elapsedTimer + " Hz");  
//            }
//            else
//            {
//                System.out.println("Data is gøtt'n in: " + elapsedTimer + " millis"
//                        + " or with: unlimited Hz!");
//            }

        }

//        controller = new Thread(new Controller(dh, semaphore));
//        server = new Thread(new UDPServer(semaphore, dh));        
//        alarmList = new Thread(new alarmsystem.AlarmList(dh));
//
//        controller.start();
//        server.start();
//        alarmList.start();
    }
}
