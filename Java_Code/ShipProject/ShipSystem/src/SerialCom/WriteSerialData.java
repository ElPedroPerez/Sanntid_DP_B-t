/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SerialCom;

import java.awt.BorderLayout;
import java.util.HashMap;
import jssc.SerialPort;
import jssc.SerialPortException;

/**
 *
 * @author rocio
 */
public class WriteSerialData
{

    public void writeData(String comPort, int baudRate, String data)
    {

        //Declare Special Symbol Used in Serial Data Stream from Arduino
        String start_char = "<";
        String end_char = ">";
        String sep_char = ":";
        //Define Serial Port # -- can be found in Device Manager or Arduino IDE
        SerialPort serialPort = new SerialPort(comPort);
        
      
        try
        {
            serialPort.openPort();

        } catch (SerialPortException ex)
        {
            System.out.println(ex);
        }

        try
        {
            System.out.println("Sleep");
            //Thread.sleep(2000);
        } catch (Exception e)
        {
        }

        try
        {
            serialPort.setParams(baudRate, 8, 1, 0);
            String stringData = start_char + "This is my data" + end_char;
            
            
//            for (int i = 0; i <= serialDataList.size(); i++)
//            {
//                stringData = stringData + serialDataList.
//            }

            serialPort.writeString(stringData);
             System.out.println("Data is sent");

        } catch (Exception ex)
        {
            System.out.println(ex);
        }

        try
        {
            serialPort.closePort();
        } catch (Exception e)
        {
            System.err.println(e);
        }
    }
}
