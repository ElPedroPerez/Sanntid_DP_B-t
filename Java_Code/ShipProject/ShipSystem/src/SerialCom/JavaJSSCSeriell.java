/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SerialCom;

import java.util.HashMap;
import jssc.*;

/**
 *
 * @author rocio
 */
public class JavaJSSCSeriell
{

    static SerialDataHandler serialDataHandler = new SerialDataHandler();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        while (true)
        {
            long lastTime = System.nanoTime();
            //HashMap serialData = new ReadSeriellData().readData("com3", 57600);
            long elapsedTimer = (System.nanoTime() - lastTime) / 1000000;
            System.out.println("Data is gøtt'n in: " + elapsedTimer + " millis");
            // serialDataHandler.writeData("Com3", 9600, "This is my data");
        }

//        // getting serial ports list into the array
//        String[] portNames = SerialPortList.getPortNames();
//
//        if (portNames.length == 0)
//        {
//            System.out.println("There are no serial-ports :( You can use an emulator, such ad VSPE, to create a virtual serial port.");
//            System.out.println("Press Enter to exit...");
//            try
//            {
//                System.in.read();
//            } catch (Exception e)
//            {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            return;
//        }
//
//        for (int i = 0; i < portNames.length; i++)
//        {
//            System.out.println(portNames[i]);
//        }
//
//        //Declare Special Symbol Used in Serial Data Stream from Arduino
//        String start_char = "<";
//        String end_char = ">";
//        String sep_char = ":";
//        //Define Serial Port # -- can be found in Device Manager or Arduino IDE
//        SerialPort serialPort = new SerialPort("COM1");
//        try
//        {
//            serialPort.openPort();
//        } catch (SerialPortException ex)
//        {
//            System.out.println(ex);
//        }
//
//        while (true)
//        {
//            try
//            {
//
//                //serialPort.setParams(9600, 8, 1, 0);
//                serialPort.setParams(57600, 8, 1, 0);
//                byte[] buffer;
//                buffer = serialPort.readBytes(25);
//
//                String dataStream = new String(buffer);
//                dataStream = dataStream.substring(dataStream.indexOf(start_char) + 1);
//                dataStream = dataStream.substring(0, dataStream.indexOf(end_char));
//                dataStream = dataStream.replace("?", "");
//                String[] data = dataStream.split(sep_char);
//
//                for (int i = 0; i < data.length; i++)
//                {
//                    System.out.println(data[i]);
//
//                }
//
//                
//            } catch (Exception ex )
//            {
//                System.out.println(ex);
//              
//            }
//            try
//            {
//                Thread.sleep(100);
//            } catch (InterruptedException ex)
//            {
//                Thread.currentThread().interrupt();
//            }
//            
//            
//
//        }
//
//    }
    }

}
