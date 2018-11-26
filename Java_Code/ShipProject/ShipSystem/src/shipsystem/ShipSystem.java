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
 * @author Haakon, Bjørnar, Robin
 */
public class ShipSystem
{

    protected static DataHandler dh;
    private static Thread acclerationFilter;
    private static Thread alarmList;
    private static Thread controller;
    private static Thread server;
    private static Thread serialDataHandler;
    private static Thread udpListener;
    private static Semaphore semaphore;
    static SendEventState enumStateEvent;

    protected static String ipAddressGUI = "192.168.0.104"; // Bjørnar: "158.38.199.111", Håkon: "158.38.85.64", Robin: "158.38.85.192"
    protected static int sendPort = 5057;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        semaphore = new Semaphore(1, true);

        dh = new DataHandler();
        dh.setThreadStatus(true);

        acclerationFilter = new Thread(new AccelerationFilter(dh));

        udpListener = new Thread(new UDPListener(dh));

        serialDataHandler = new Thread(new SerialDataHandler(dh));
        //alarmList = new Thread(new AlarmList(dh));

        controller = new Thread(new Controller(dh, semaphore));

        controller.setName("Controller");
        udpListener.setName("UDPListener");
        acclerationFilter.setName("AccelerationFilter");
        serialDataHandler.setName("SerialDataHandler");

        controller.start();
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
            }
            catch (Exception e)
            {
            }
            // System.out.println("getSoftSpeedPod: " + dh.getSoftSpeedPod());
            boolean kake = true;
            //  System.out.println("Test value is: " + dh.getTest()); 
            //  System.out.println("Test2 value is: " + dh.getTest2());
            // System.out.println("Ping : " + dh.);
            //do nothing
        }
//        // bRYNJARS testområde
//        while (true)
//        {
//            try
//            {
//                System.out.println("Angle from IC: " + dh.getTemp_Angle());
//                System.out.println("Speed from IC: " + dh.getIc_speed());
//                System.out.println("L1: " + dh.getIc_L1());
//                System.out.println("R1: " + dh.getIc_R1());
//                System.out.println("X: " + dh.getIc_X());
//                System.out.println("A: " + dh.getIc_A());
//                System.out.println("B: " + dh.getIc_B());
//                System.out.println("Y: " + dh.getIc_Y());
//                Thread.sleep(250);
//            } catch (InterruptedException ex)
//            {
//                Logger.getLogger(ShipSystem.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }

        // }
//        //Robins test area
//        dh.handleDataFromRemote();
//        
//        
//        while (true)
//        {
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
////            System.out.println("fb_podPosPS is: " + fb_podPosPS);
////            System.out.println("fb_podPosSB is: " + fb_podPosSB);
////            System.out.println("fb_speedPS is: " + fb_speedPS);
////            System.out.println("fb_speedSB is: " + fb_speedSB);
////
////            System.out.println("Yaw is: " + yaw);
////            System.out.println("Pitch is: " + pitch);
////            System.out.println("Roll is: " + roll);
////
////            System.out.println("Com response time: " + dh.getComResponseTime());
//        }
////End of Robins test area
//        controller = new Thread(new Controller(dh, semaphore));
//        server = new Thread(new UDPServer(semaphore, dh));        
//        alarmList = new Thread(new alarmsystem.AlarmList(dh));
//
//        controller.start();
//        server.start();
//        alarmList.start();
    }
//
//        }

}
