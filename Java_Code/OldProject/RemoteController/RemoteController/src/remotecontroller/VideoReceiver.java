/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package remotecontroller;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Videoreceiver class is a UDP receiver for video frames. Used for streaming.
 * 
 * @author Magnus Gribbestad
 */
public class VideoReceiver implements Runnable {
    
    private DatagramSocket serverSocket;
    private BufferedImage scaledImage = null;
    private int videoPort;
    private ReceiveVideoObservable observable;
    private Thread t;
    
    /**
     * Constructor for creating VideoReceiver objeckt
     * 
     * @param observable ReceiveVideoObservable object
     * @param port Int Receiving port
     */
    public VideoReceiver(ReceiveVideoObservable observable, int port){
        this.observable = observable;
        this.videoPort = port;
    }
    
    /**
     * Start a thread
     */
    public void start(){
        t = new Thread(this,"VideoReceiverThread");
        t.start();
    }
    
    /**
     * Runs the thread.
     * Waiting for receiving frame, makes input stream to buffered image.
     * Sets the frame in the observable object.
     */
    @Override
    public void run()
    {
        try {
            serverSocket = new DatagramSocket(this.videoPort);
            BufferedImage buff;
            
            byte[] receiveData = new byte[57654];
            
            while(true)
            {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                ByteArrayInputStream in = new ByteArrayInputStream(receiveData);
                scaledImage = ImageIO.read(in);
                if(this.observable != null){
                    this.observable.setFrame(this.getImage());
                }
            }
        } catch (SocketException ex) {
            Logger.getLogger(UDPreceiver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(UDPreceiver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
  
    /**
     * Returns the BufferedImage from the scaledImage field.
     * 
     * @return BufferedImage
     */
    public BufferedImage getImage()
    {
        return scaledImage;
    }
}