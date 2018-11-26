/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shipsystem;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Sends data through UDP
 * @author mgrib
 */
public class UDPsender
{

    private DatagramSocket clientSocket;

    /**
     * Sends data throud UDP
     */
    public UDPsender()
    {
    }

    /**
     * Initilizes the UDP sender. Creates a Datagram socket.
     */
    private void init()
    {
        try
        {
            clientSocket = new DatagramSocket();
        }
        catch (SocketException ex)
        {
            Logger.getLogger(UDPsender.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Sends a datagram packet with the given data to the given ip adress at the
     * given port.
     *
     * @param ipAddress String IP address to send to
     * @param sendData
     * @param port Integer Port number
     */
    public synchronized void send(String ipAddress, String sendData, int port)
    {
        this.init();
        try
        {
            byte[] data = sendData.getBytes();
            DatagramPacket packet = new DatagramPacket(data,
                    data.length,
                    InetAddress.getByName(ipAddress),
                    port);
            System.out.println("Data sent: " + new String(packet.getData()));
            clientSocket.send(packet);
            //System.out.println(Arrays.toString(data));
            //System.out.println("UDP send");
        }
        catch (IOException ex)
        {
            Logger.getLogger(UDPsender.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            clientSocket.close();
        }
    }

}
