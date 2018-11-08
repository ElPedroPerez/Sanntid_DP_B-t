/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package remotecontroller;

import java.io.IOException;
import java.net.*;

/**
 *
 * @author lars-harald
 */
public class UDPreceiver implements Runnable {
    private ReceiveDataObservable observer;
    private DatagramSocket receiveSocket;
    private int port;
    private Thread t;

    /**
     * Constructor of the UDPreceiver. 
     * 
     * @param receiveDataObs ReceiveDataObservable. Updates the GUI.
     * @param port Port number as integer
     */
    public UDPreceiver(ReceiveDataObservable receiveDataObs, int port){
        this.observer = receiveDataObs; 
        this.port = port;
    }
    
    /**
     * Start the thread
     */
    public void start(){
        t = new Thread(this,"UDPReceiverThread");
        t.start();
    }
    
    /**
     * Run function. Checks if the observer field has an objekt.
     * Waits for a packet, and sets the data in the ReceiveDataObservable
     */
    public void run() {
        if(observer != null) {
            try {
                receiveSocket = new DatagramSocket(port);
                DatagramPacket receivePacket = new DatagramPacket(new byte[6],6);
                
                while(observer.shouldChildOfThisRun()){
                    receiveSocket.receive(receivePacket);
                    observer.setData(receivePacket.getData());
                }
            } catch (IOException e) {
                System.out.println(e);
            } finally {
                receiveSocket.close();
                System.out.println("receivesocket closed");
            }
        } else {
            System.out.println("receive datahandler not created in udpreceiver thread");
        }
    }
    
}
    
    

