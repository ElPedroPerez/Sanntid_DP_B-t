/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guisystem;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Handles data from GUI
 *
 * @author Bjørnar
 */
public class Datahandler
{

    private String dataFromGui;
    private int angle;
    private Boolean guiPing;
    private long ping;
    public ConcurrentHashMap<String, String> data = new ConcurrentHashMap<>();

    public Datahandler()
    {
        guiPing = false;
        ping = 0;
        //this.dataFromGui = dataFromGui;
    }
//
//    public void updateGUI()
//    {
//        setChanged();
//        notifyObservers();
//    }
//
    public void setAngle(int angle)
    {
        this.angle = angle;
//        setChanged();
//        notifyObservers();
    }

    public int getAngle()
    {
        return this.angle;
    }

    /**
     * Gets data from GUI
     *
     * @return dataFromGui
     */
    public String getDataFromGui()
    {
        return this.dataFromGui;
    }

    /**
     * Creates and sends the data String over UDP
     */
    public synchronized void sendData()
    {
        new UDPsender().send(GUISystem.IPADDRESS, dataFromGui, GUISystem.SENDPORT);
    }

    public synchronized Boolean getGuiPing()
    {
        return guiPing;
    }

    public synchronized void setGuiPing(Boolean guiPing)
    {
        this.guiPing = guiPing;
    }

    public synchronized long getPing()
    {
        return ping;
    }

    public synchronized void setPing(long ping)
    {
        this.ping = ping;
    }
    
    

    public synchronized void handleDataFromArduino()
    {

//        ConcurrentHashMap<String, String> dataFeedback = new ConcurrentHashMap<>();
//        ConcurrentHashMap<String, String> dataIMU = new ConcurrentHashMap<>();
//        dataFeedback = 
        //SerialDataHandler
        //sdh.readData(this, arduinoFeedbackComPort, arduinoBaudRate);
        //dataIMU = sdh.readData(arduinoFeedbackComPortIMU, arduinoBaudRate);
//        dataFeedback.forEach(data::putIfAbsent);
        //dataIMU.forEach(data::putIfAbsent);
        for (Map.Entry e : data.entrySet())
        {
            String key = (String) e.getKey();
            String value = (String) e.getValue();
            switch (key)
            {
                case "PingBack":
                    this.guiPing = Boolean.parseBoolean(value);
                    break;
            }
        }
    }
}
