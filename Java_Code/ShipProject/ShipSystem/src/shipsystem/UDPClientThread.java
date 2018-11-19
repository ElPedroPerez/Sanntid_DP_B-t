package shipsystem;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a single thread which handles all the communication to the
 * connected client socket.
 *
 * @author Håkon
 * @version 0.1
 */
public class UDPClientThread implements Runnable
{

    private final DatagramSocket connectionSocket;
    private static String clientSentence;
    private final int clientNumber;
    private final DataHandler dh;
    private DatagramSocket receiveSocket;
    private UDPListener udpListener;

    /**
     * Creates an instance of the ClientThread class
     *
     * @param connectionSocket the given socket
     * @param clientNumber the given client number
     */
    public UDPClientThread(DataHandler dh, DatagramSocket connectionSocket, UDPListener udpListener, int clientNumber)
    {
        this.dh = dh;
        this.connectionSocket = connectionSocket;
        this.clientNumber = clientNumber;
        this.udpListener = udpListener;
    }

    @Override
    public void run()
    {
        String start_char = "<";
        String end_char = ">";
        String sep_char = ":";

        try
        {
            while (true)
            {
                
              
                byte[] receiveData = new byte[1024];
                //receiveSocket = new DatagramSocket(5056);
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                connectionSocket.receive(receivePacket);

                String dataReceived = new String(receivePacket.getData());
                if (dataReceived != null)
                {
                    dataReceived = dataReceived.substring(dataReceived.indexOf(start_char) + 1);
                    dataReceived = dataReceived.substring(0, dataReceived.indexOf(end_char));
                    dataReceived = dataReceived.replace("?", "");
                    String[] data = dataReceived.split(sep_char);
                    for (int i = 0; i < data.length; i = i + 2)
                    {
                        dh.data.put(data[i], data[i + 1]);
                        //SerialDataList.put(data[i], data[i + 1]);
                        //System.out.println("Key: " + data[i] + "     Value:" + data[i + 1]);                   
                    }
                    dh.handleDataFromRemote();
                }
            }

        } catch (IOException e)
        {
            System.out.println(e);
        }

        //-------------------------------------------------------------------
    }
}
