/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guisystem;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Handles data from GUI
 *
 * @author Bjørnar, Haakon, Robin
 */
public class DataHandler
{

    private String dataFromGui;
    private int angle;
    private Boolean guiPing;
    private long ping;
    public ConcurrentHashMap<String, String> data = new ConcurrentHashMap<>();
    public ConcurrentHashMap<String, Boolean> alarmList = new ConcurrentHashMap<>();

    private int fb_podPosPS;
    private int fb_podPosSB;
    private int fb_speedPS;
    private int fb_speedSB;
    private int fb_heading;

    //Alarm variables
    private boolean sbSpeedFbAlarm;
    private boolean psSpeedFbAlarm;
    private boolean sbPodPosFbAlarm;
    private boolean psPodPosFbAlarm;
    private boolean visionDeviationAlarm;
    private boolean imuRollAlarm;
    //private boolean pingAlarm;

    public DataHandler()
    {
        guiPing = false;
        ping = 0;
        fb_podPosPS = 0;
        fb_podPosSB = 0;
        fb_speedPS = 0;
        fb_speedSB = 0;
        //this.dataFromGui = dataFromGui;
    }

    /**
     * angle value
     * @param angle 
     */
    public synchronized void setAngle(int angle)
    {
        this.angle = angle;
//        setChanged();
//        notifyObservers();
    }

    /**
     * 
     * @return angle
     */
    public synchronized int getAngle()
    {
        return this.angle;
    }

    /**
     * 
     * @return pod position feedback port side
     */
    public synchronized int getFb_podPosPS()
    {
        return fb_podPosPS;
    }

    /**
     * pod position feedback port side
     * @param fb_podPosPS 
     */
    public synchronized void setFb_podPosPS(int fb_podPosPS)
    {
        this.fb_podPosPS = fb_podPosPS;
    }

    /**
     * 
     * @return pod position feedback star board
     */
    public synchronized int getFb_podPosSB()
    {
        return fb_podPosSB;
    }

    /**
     * pod position feedback star board
     * @param fb_podPosSB 
     */
    public synchronized void setFb_podPosSB(int fb_podPosSB)
    {
        this.fb_podPosSB = fb_podPosSB;
    }

    /**
     * 
     * @return speed feedback port side
     */
    public synchronized int getFb_speedPS()
    {
        return fb_speedPS;
    }

    /**
     * speed feedback port side
     * @param fb_speedPS 
     */
    public synchronized void setFb_speedPS(int fb_speedPS)
    {
        this.fb_speedPS = fb_speedPS;
    }

    /**
     * 
     * @return  speed feedback star board
     */
    public synchronized int getFb_speedSB()
    {
        return fb_speedSB;
    }

    /**
     * speed feedback star board
     * @param fb_speedSB 
     */
    public synchronized void setFb_speedSB(int fb_speedSB)
    {
        this.fb_speedSB = fb_speedSB;
    }

    /**
     * 
     * @return heading feedback
     */
    public synchronized int getFb_heading()
    {
        return fb_heading;
    }

   /**
    * heading feedback
    * @param fb_heading 
    */
    public synchronized void setFb_heading(int fb_heading)
    {
        this.fb_heading = fb_heading;
    }

    /**
     * Gets data from GUI
     *
     * @return dataFromGui
     */
    public synchronized String getDataFromGui()
    {
        return this.dataFromGui;
    }

//    /**
//     * Creates and sends the data String over UDP
//     */
//    public synchronized void sendData()
//    {
//        new UDPsender().send(GUISystem.IPADDRESS, dataFromGui, GUISystem.SENDPORT);
//    }

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

    public synchronized void handleDataFromRemote()
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
                case "fb_podPosPS":
                    this.fb_podPosPS = Integer.parseInt(value);
                    break;
                case "fb_podPosSB":
                    this.fb_podPosSB = Integer.parseInt(value);
                    break;
                case "fb_speedPS":
                    this.fb_speedPS = Integer.parseInt(value);
                    break;
                case "fb_speedSB":
                    this.fb_speedSB = Integer.parseInt(value);
                    break;
                case "fb_heading":
                    this.fb_heading = Integer.parseInt(value);
                    break;
                case "sbSpeedFbAlarm":
                    if (this.sbSpeedFbAlarm != "1".equals(value))
                    {
                        this.sbSpeedFbAlarm = Boolean.parseBoolean(value);
                        this.alarmList.put(key, Boolean.parseBoolean(value));
                    }
                    break;
                case "psSpeedFbAlarm":
                    if (this.psSpeedFbAlarm != "1".equals(value))
                    {
                        this.psSpeedFbAlarm = Boolean.parseBoolean(value);
                        this.alarmList.put(key, Boolean.parseBoolean(value));
                    }
                    break;
                case "sbPodPosFbAlarm":
                    if (this.sbPodPosFbAlarm != "1".equals(value))
                    {
                        this.sbPodPosFbAlarm = Boolean.parseBoolean(value);
                        this.alarmList.put(key, Boolean.parseBoolean(value));
                    }
                    break;
                case "psPodPosFbAlarm":
                    if (this.psPodPosFbAlarm != "1".equals(value))
                    {
                        this.psPodPosFbAlarm = Boolean.parseBoolean(value);
                        this.alarmList.put(key, Boolean.parseBoolean(value));
                    }
                    break;
                case "visionDeviationAlarm":
                    if (this.visionDeviationAlarm != "1".equals(value))
                    {
                        this.visionDeviationAlarm = Boolean.parseBoolean(value);
                        this.alarmList.put(key, Boolean.parseBoolean(value));
                    }
                    break;
                case "imuRollAlarm":
                    if (this.imuRollAlarm != "1".equals(value))
                    {
                        this.imuRollAlarm = Boolean.parseBoolean(value);
                        this.alarmList.put(key, Boolean.parseBoolean(value));
                    }
                    break;
//              case "pingAlarm":
//                    if (this.sbSpeedFbAlarm != "1".equals(value))
//                    {
//                    this.psSpeedFbAlarm = Boolean.parseBoolean(value);
//                    this.alarmList.put(key, Boolean.parseBoolean(value));
//                    }
//                    break;
            }
        }
    }
}
