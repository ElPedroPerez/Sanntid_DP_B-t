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
    protected static String ipAdress;

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
        
        while(true)
        {
            dh.handleDataFromArduino();
            int fb_podPosPS = dh.getFb_podPosPS();
            double yaw = dh.getYaw();
            double pitch = dh.getPitch();
            double roll = dh.getRoll();
            System.out.println("fb_podPosPS is: " + fb_podPosPS);
            System.out.println("Yaw is: " + yaw);
            System.out.println("Pitch is: " + pitch);
            System.out.println("Roll is: " + roll);
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
