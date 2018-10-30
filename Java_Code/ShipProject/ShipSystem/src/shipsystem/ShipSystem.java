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
    }
}
