/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SerialCom;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import jssc.SerialPort;
import shipsystem.DataHandler;

/**
 *
 * @author rocio
 */
public class SerialDataHandler
{

    private static Thread readSerialData;

    HashMap<String, Boolean> comPorts = new HashMap<>();

    //ReadSeriellData reader = new ReadSeriellData();
    WriteSerialData writer = new WriteSerialData();

    //Populate hashmap
    public SerialDataHandler()
    {
        //True equals port busy
        comPorts.put("Com1", false);
        comPorts.put("Com2", false);
        comPorts.put("Com3", false);
        comPorts.put("Com4", false);
        comPorts.put("Com5", false);
        comPorts.put("Com6", false);
        comPorts.put("Com7", false);
    }

    public ConcurrentHashMap readData(DataHandler dh, String comPort, int baudRate)
    {
        ConcurrentHashMap serialData = new ConcurrentHashMap();

        if (comPorts.containsKey(comPort))
        {
            if (!comPorts.get(comPort))
            {
                readSerialData = new Thread(new ReadSeriellData(dh, this, comPort, baudRate));
                readSerialData.start();

            } else
            {
                System.out.println(comPort + " busy");
                // Error message, com port busy
            }
        } else
        {
            System.out.println(comPort + " doeas not exist!");
            // Error message, illegal com port
        }
        return serialData;
    }

    public void writeData(String comPort, int baudRate, String data)
    {

        if (comPorts.containsKey(comPort))
        {
            if (!comPorts.get(comPort))
            {
                writer.writeData(comPort, baudRate, data);
//                HashMap serialData = new ReadSeriellData().readData(comPort, baudRate);

            } else
            {
                //Error message, com port busy
            }
        } else
        {
            // Error message, illegal com port
        }
    }
}
