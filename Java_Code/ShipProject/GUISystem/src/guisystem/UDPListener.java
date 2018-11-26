/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guisystem;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Listend for data throug UDP
 * @author rocio
 */
public class UDPListener implements Runnable
{

    private final int recievePort;
    private final DataHandler dh;
    public ConcurrentHashMap<String, String> queue = new ConcurrentHashMap<>();

    public UDPListener(DataHandler dh, int recievePort)
    {
        this.recievePort = recievePort;
        this.dh = dh;

        //LinkedBlockingQueue queue = new LinkedBlockingQueue(10);
    }

    @Override
    public void run()
    {
        String start_char = "<";
        String end_char = ">";
        String sep_char = ":";
        try
        {
            //ServerSocket listenSocket = new ServerSocket(5559);
            DatagramSocket listenSocket = new DatagramSocket(recievePort);
            int nextClientNumber = 0;
            List<String> ipAdressList = new ArrayList<>();

            while (true)
            {

                byte[] receiveData = new byte[1024];
                //System.out.println("Waiting for incoming connection...\n");
                DatagramPacket p = new DatagramPacket(receiveData, receiveData.length);
                listenSocket.receive(p);
                //  String dataReceived = new String(p.getData());

                String dataReceived = new String(p.getData());
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

//                try
//                {
//                    String clientsAddress = p.getAddress().toString();
//                    if (clientsAddress != "" && !ipAdressList.contains(clientsAddress))
//                    {
//                        ipAdressList.add(clientsAddress);
//                        nextClientNumber += 1;
//                        Thread t = new Thread(new UDPClientThread(dh, listenSocket, this,
//                                nextClientNumber));
//                        t.start();
//                        t.setName("UDPClientThread number: " + nextClientNumber);
//                        System.out.println(clientsAddress
//                                + " connected at port 5056");
//
//                    }
//                } catch (Exception e)
//                {
//                }
            }

        } catch (Exception e)

        {
            System.out.println("Exception: " + e);
        }
    }
}
