/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shipsystem;

import SerialCom.SerialDataHandler;
import SerialCom.WriteSerialData;
import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Responsible for handling data from GUI and and from arduino
 *
 * @author Haakon, Bjørnar, Robin
 */
public class DataHandler
{

    private UDPsender udpSender = new UDPsender();
    //private SerialDataHandler sdh = new SerialDataHandler();

    private String arduinoFeedbackComPort;
    private String arduinoCommandComPort;
    private String arduinoFeedbackComPortIMU;
    private int arduinoBaudRate;

    /**
     *
     */
    public String ipAddressGUI;

    /**
     *
     */
    public int sendPort;

    private byte[] dataFromArduino;
    private byte[] dataToArduino;
    private byte[] dataFromGui;
    private boolean dataFromArduinoAvaliable = false;
    private boolean dataFromGuiAvailable = false;
    private boolean threadStatus = true;
    private byte requestCodeFromArduino;

    // flags
    private boolean ic_R1_flag = false;
    private boolean ic_L1_flag = false;
    private boolean ic_A_flag = false;
    private boolean ic_B_flag = false;
    private boolean ic_X_flag = false;
    private boolean ic_Y_flag = false;
    private boolean ic_speed_flag = false;
    private boolean ic_angle_flag = false;

    /**
     * For detecting change in data
     */
    public boolean dataToRemoteUpdated = false;
    public boolean dataToGuiUpdated = false;
    public boolean dataUpdated = false;

    /**
     * Feedback from sensors
     */
    public int fb_speedSB;
    public int fb_speedPS;
    public int fb_podPosSB;
    public int fb_podPosPS;
    public int fb_speedPodRotPS;
    public int fb_heading;
    public boolean fb_ballastSensor;

    //IMU variables
    private int Yaw;
    private int Pitch;

    /**
     *
     */
    public int Roll;
    private long comResponseTime;

    /**
     * Command variables for motors
     */
    public int cmd_speedSB;
    public int cmd_speedPS;
    public int cmd_speedPodRotSB;
    public int cmd_speedPodRotPS;
    public int cmd_podPosSB;
    public int cmd_podPosPS;
    public boolean cmd_ballastSensor;

    /**
     * Check if variable is available to change
     */
    private boolean speedSBavailable;
    private boolean speedPSavailable;
    private boolean podPosSBavailable;
    private boolean podPosPSavailable;
    private boolean ballastSensorAvailable;

    /**
     * Vision variables
     */
    private double xShipPos;
    private double yShipPos;

    /**
     * Position accuracy variable
     */
    public double posAccuracy;

    /**
     * Alarm variables
     */
    private boolean sbSpeedFbAlarm;
    private boolean psSpeedFbAlarm;
    private boolean sbPodPosFbAlarm;
    private boolean psPodPosFbAlarm;
    private boolean visionDeviationAlarm;
    private boolean imuRollAlarm;
    //private boolean pingAlarm;

    /**
     * Ping variables
     */
    private boolean visionPosDataPing;
    private String guiPing;
    private int Test;
    private int Test2;


    /**
     * Controller variables
     */
    public boolean ic_L1;
    public boolean ic_R1;
    public boolean ic_X;
    public boolean ic_A;
    public boolean ic_B;
    public boolean ic_Y;
    public int ic_speed;
    public int ic_angle;
    private int temp_Angle;

    /**
     * 
     */
    private int softSpeedPod;

    /**
     * Logical outputs
     */
    public byte thrusterCommand;
    public byte podPosSBCommand;
    public byte podPosPSCommand;

    /**
     *
     */
    public ConcurrentHashMap<String, String> data = new ConcurrentHashMap<>();
    public ConcurrentHashMap<String, String> dataToRemote = new ConcurrentHashMap<>();
    public ConcurrentHashMap<String, Boolean> listOfAlarms;

    /**
     * Responsible for handling data from and to GUISystem and to Arduino
     */
    public DataHandler()
    {
        listOfAlarms = new ConcurrentHashMap<>();

        this.dataFromArduino = new byte[6];
        this.dataToArduino = new byte[6];
        this.dataFromGui = new byte[6];
        dataFromArduinoAvaliable = false;
        dataFromGuiAvailable = false;

        arduinoFeedbackComPort = "Com5";
        arduinoFeedbackComPortIMU = "Com6";
        arduinoCommandComPort = "Com7";
        arduinoBaudRate = 115200;
        ipAddressGUI = ShipSystem.ipAddressGUI;
        sendPort = ShipSystem.sendPort;

        fb_speedSB = 0;
        fb_speedPS = 0;
        fb_podPosSB = 0;
        fb_podPosPS = 0;
        fb_speedPodRotPS = 0;
        fb_ballastSensor = false;
        Yaw = 0;
        Pitch = 0;
        Roll = 0;
        comResponseTime = 0;

        speedSBavailable = false;
        speedPSavailable = false;
        podPosSBavailable = false;
        podPosPSavailable = false;
        ballastSensorAvailable = false;

        xShipPos = 0;
        yShipPos = 0;
        posAccuracy = 0;

        //Alarms
        fillListOfAlarms();
        sbSpeedFbAlarm = (boolean) listOfAlarms.get("sbSpeedFbAlarm").booleanValue();
        psSpeedFbAlarm = (boolean) listOfAlarms.get("psSpeedFbAlarm").booleanValue();
        sbPodPosFbAlarm = (boolean) listOfAlarms.get("sbPodPosFbAlarm").booleanValue();
        psPodPosFbAlarm = (boolean) listOfAlarms.get("psPodPosFbAlarm").booleanValue();
        visionDeviationAlarm = (boolean) listOfAlarms.get("visionDeviationAlarm").booleanValue();
        imuRollAlarm = (boolean) listOfAlarms.get("imuRollAlarm").booleanValue();
        //pingAlarm = listOfAlarms.get(pingAlarm).booleanValue();

        //Ping variables
        visionPosDataPing = false;
        guiPing = "0.0.0.0";
        Test = 0;
        Test2 = 0;

        //Controller variables
        ic_L1 = false;
        ic_R1 = false;
        ic_X = false;
        ic_A = false;
        ic_B = false;
        ic_Y = false;
        ic_speed = 0;
        ic_angle = 0;
        temp_Angle = 0;

        //Filtered controller variables
        softSpeedPod = 0;

        //Logical outputs
        thrusterCommand = 0;
        podPosSBCommand = 0;
        podPosPSCommand = 0;
    }
    
    /**
     * Returns the threads status
     *
     * @return The threads status
     */
    public boolean shouldThreadRun()
    {
        return threadStatus;
    }

    /**
     * Sets the threads status
     *
     * @param threadStatus Thread status
     */
    public void setThreadStatus(boolean threadStatus)
    {
        this.threadStatus = threadStatus;
    }

    /**
     *
     * @return data from arduino
     */
    public byte[] getDataFromArduino()
    {
        return dataFromArduino;
    }

    /**
     * 
     * @return true if button is pushed, false if not
     */
    public synchronized boolean isIc_A_flag()
    {
        return ic_A_flag;
    }

    /**
     * 
     * @param ic_A_flag 
     */
    public synchronized void setIc_A_flag(boolean ic_A_flag)
    {
        this.ic_A_flag = ic_A_flag;
    }

    /**
     * 
     * @return true if button is pushed, false if not
     */
    public synchronized boolean isIc_R1_flag()
    {
        return ic_R1_flag;
    }

    /**
     * 
     * @param ic_R1_flag 
     */
    public synchronized void setIc_R1_flag(boolean ic_R1_flag)
    {
        this.ic_R1_flag = ic_R1_flag;
    }

    /**
     * 
     * @return true if button is pushed, false if not
     */
    public synchronized boolean isIc_L1_flag()
    {
        return ic_L1_flag;
    }

    /**
     * 
     * @param ic_L1_flag 
     */
    public synchronized void setIc_L1_flag(boolean ic_L1_flag)
    {
        this.ic_L1_flag = ic_L1_flag;
    }

    /**
     * 
     * @return true if button is pushed, false if not
     */
    public synchronized boolean isIc_B_flag()
    {
        return ic_B_flag;
    }

    /**
     * 
     * @param ic_B_flag 
     */
    public synchronized void setIc_B_flag(boolean ic_B_flag)
    {
        this.ic_B_flag = ic_B_flag;
    }

    /**
     * 
     * @return true if button is pushed, false if not
     */
    public synchronized boolean isIc_X_flag()
    {
        return ic_X_flag;
    }

    /**
     * 
     * @param ic_X_flag 
     */
    public synchronized void setIc_X_flag(boolean ic_X_flag)
    {
        this.ic_X_flag = ic_X_flag;
    }

    /**
     * 
     * @return true if button is pushed, false if not
     */
    public synchronized boolean isIc_Y_flag()
    {
        return ic_Y_flag;
    }

    /**
     * 
     * @param ic_Y_flag 
     */
    public synchronized void setIc_Y_flag(boolean ic_Y_flag)
    {
        this.ic_Y_flag = ic_Y_flag;
    }

    /**
     * 
     * @return integer from controller
     */
    public synchronized boolean isIc_speed_flag()
    {
        return ic_speed_flag;
    }

    /**
     * 
     * @param ic_speed_flag 
     */
    public synchronized void setIc_speed_flag(boolean ic_speed_flag)
    {
        this.ic_speed_flag = ic_speed_flag;
    }

    /**
     * 
     * @return integer from controller
     */
    public synchronized boolean isIc_angle_flag()
    {
        return ic_angle_flag;
    }

    /**
     * 
     * @param ic_angle_flag 
     */
    public synchronized void setIc_angle_flag(boolean ic_angle_flag)
    {
        this.ic_angle_flag = ic_angle_flag;
    }

    /**
     *
     * @return true if new data available, false if not
     */
    public synchronized boolean isDataFromArduinoAvailable()
    {
        return this.dataFromArduinoAvaliable;
    }

    /**
     *
     * @return data that has to be sent to the GUI has changed
     */
    public synchronized boolean isDataToRemoteUpdated()
    {
        return dataToRemoteUpdated;
    }

    /**
     * Sets data that has changed to GUI
     * @param dataToRemoteUpdated
     */
    public synchronized void setDataToRemoteUpdated(boolean dataToRemoteUpdated)
    {
        this.dataToRemoteUpdated = dataToRemoteUpdated;
    }

    /**
     *
     * @return data is changed
     */
    public synchronized boolean isDataUpdated()
    {
        return dataUpdated;
    }

    /**
     *
     * @param dataUpdated sets that data is changed
     */
    public synchronized void setDataUpdated(boolean dataUpdated)
    {
        this.dataUpdated = dataUpdated;
    }

    /**
     *
     * @return temporaray angle for testing 
     */
    public int getTemp_Angle()
    {
        return temp_Angle;
    }

    /**
     *
     * @param temp_Angle sets the temporary data
     */
    public void setTemp_Angle(int temp_Angle)
    {
        this.temp_Angle = temp_Angle;
    }

    /**
     * Sets L1 flag to false
     * @return value of the L1 button
     */
    public synchronized boolean getIc_L1()
    {
        this.setIc_L1_flag(false);
        return this.ic_L1;
    }

    /**
     * sets input L1 to local L1, sets flag for L1 true and data is updated
     * @param ic_L1
     */
    public synchronized void setIc_L1(boolean ic_L1)
    {
        this.ic_L1 = ic_L1;
        this.setIc_L1_flag(true);
        this.setDataUpdated(true);
    }

    /**
     * Sets R1 flag to false
     * @return value of the R1 button
     */
    public synchronized boolean getIc_R1()
    {
        this.setIc_R1_flag(false);
        return ic_R1;
    }

    /**
     * sets input R1 to local R1, sets flag for R1 true and data is updated
     * @param ic_R1
     */
    public synchronized void setIc_R1(boolean ic_R1)
    {
        this.ic_R1 = ic_R1;
        this.setIc_R1_flag(true);
        this.setDataUpdated(true);
    }

    /**
     * Sets X flag to false
     * @return value of the X button
     */
    public boolean getIc_X()
    {
        this.setIc_X_flag(false);
        return ic_X;
    }

    /**
     * sets input X to local X, sets flag for X true and data is updated
     * @param ic_X
     */
    public void setIc_X(boolean ic_X)
    {
        this.ic_X = ic_X;
        this.setIc_X_flag(true);
        this.setDataUpdated(true);
    }

    /**
     * Sets A flag to false
     * @return value of the A button
     */
    public synchronized boolean getIc_A()
    {
        this.setIc_A_flag(false);
        return ic_A;
    }

    /**
     * sets input A to local A, sets flag for A true and data is updated
     * @param ic_A
     */
    public synchronized void setIc_A(boolean ic_A)
    {
        this.ic_A = ic_A;
        this.setIc_A_flag(true);
        this.setDataUpdated(true);
    }

    /**
     * Sets B flag to false
     * @return value of the B button
     */
    public boolean getIc_B()
    {
        this.setIc_B_flag(false);
        return ic_B;
    }

    /** 
     * sets input B to local B, sets flag for B true and data is updated
     * @param ic_B
     */
    public void setIc_B(boolean ic_B)
    {
        this.ic_B = ic_B;
        this.setIc_B_flag(true);
        this.setDataUpdated(true);
    }

    /**
     * Sets Y flag to false
     * @return value of the Y button
     */
    public boolean getIc_Y()
    {
        this.setIc_Y_flag(false);
        return ic_Y;
    }

    /**
     * sets input Y to local Y, sets flag for Y true and data is updated
     * @param ic_Y
     */
    public void setIc_Y(boolean ic_Y)
    {
        this.ic_Y = ic_Y;
        this.setIc_Y_flag(true);
        this.setDataUpdated(true);
    }

    /**
     * Sets speed flag to false
     * @return value of the speed button
     */
    public int getIc_speed()
    {
        this.setIc_speed_flag(false);
        return ic_speed;
    }

    /**
     * sets input speed to local speed, sets flag for speed true and data is updated
     * @param ic_speed
     */
    public void setIc_speed(int ic_speed)
    {
        this.ic_speed = ic_speed;
        this.setIc_speed_flag(true);
        this.setDataUpdated(true);
    }

    /**
     * Sets angle flag to false
     * @return value of the angle button
     */
    public int getIc_angle()
    {
        this.setIc_angle_flag(false);
        return ic_angle;
    }

    /**
     * sets input angle to local angle, sets flag for angle true and data is updated
     * @param ic_angle
     */
    public void setIc_angle(int ic_angle)
    {
        this.ic_angle = ic_angle;
        this.setIc_angle_flag(true);
        this.setDataUpdated(true);
    }

    /**
     * 
     * @return Feedback of speed Star Board
     */
    public int getFb_speedSB()
    {
        return fb_speedSB;
    }

    /**
     *
     * @param fb_speedSB
     */
    public void setFb_speedSB(int fb_speedSB)
    {
        this.fb_speedSB = fb_speedSB;
    }

    /**
     *
     * @return Feedback speed Port Side
     */
    public int getFb_speedPS()
    {
        return fb_speedPS;
    }

    /**
     *
     * @param fb_speedPS
     */
    public void setFb_speedPS(int fb_speedPS)
    {
        this.fb_speedPS = fb_speedPS;
    }

    /**
     *
     * @return Feedback pod position Star Board
     */
    public int getFb_podPosSB()
    {
        return fb_podPosSB;
    }

    /**
     *
     * @param fb_podPosSB
     */
    public void setFb_podPosSB(int fb_podPosSB)
    {
        this.fb_podPosSB = fb_podPosSB;
    }

    /**
     *
     * @return Feedback pos position Port Side
     */
    public int getFb_podPosPS()
    {
        return fb_podPosPS;
    }

    /**
     *
     * @param fb_podPosPS
     */
    public void setFb_podPosPS(int fb_podPosPS)
    {
        this.fb_podPosPS = fb_podPosPS;
    }

    /**
     *
     * @return Feedback heading
     */
    public int getFb_heading()
    {
        return fb_heading;
    }

    /**
     *
     * @param fb_heading
     */
    public void setFb_heading(int fb_heading)
    {
        this.fb_heading = fb_heading;
    }

    /**
     *
     * @return Feedback ballast sensor
     */
    public boolean isFb_ballastSensor()
    {
        return fb_ballastSensor;
    }

    /**
     *
     * @param fb_ballastSensor
     */
    public void setFb_ballastSensor(boolean fb_ballastSensor)
    {
        this.fb_ballastSensor = fb_ballastSensor;
    }

    /**
     *
     * @return command speed Star Board
     */
    public int getCmd_speedSB()
    {
        return cmd_speedSB;
    }

    /**
     *
     * @param cmd_speedSB
     */
    public void setCmd_speedSB(int cmd_speedSB)
    {
        this.cmd_speedSB = cmd_speedSB;
    }

    /**
     *
     * @return command speed Port Side
     */
    public int getCmd_speedPS()
    {
        return cmd_speedPS;
    }

    /**
     *
     * @param cmd_speedPS
     */
    public void setCmd_speedPS(int cmd_speedPS)
    {
        this.cmd_speedPS = cmd_speedPS;
    }

    /**
     *
     * @return command rotation speed Star Board
     */
    public int getCmd_speedPodRotSB()
    {
        return cmd_speedPodRotSB;
    }

    /**
     *
     * @param cmd_speedPodRotSB
     */
    public void setCmd_speedPodRotSB(int cmd_speedPodRotSB)
    {
        this.cmd_speedPodRotSB = cmd_speedPodRotSB;
    }

    /**
     *
     * @return command rotation speed Port Side
     */
    public int getCmd_speedPodRotPS()
    {
        return cmd_speedPodRotPS;
    }

    /**
     *
     * @param cmd_speedPodRotPS
     */
    public void setCmd_speedPodRotPS(int cmd_speedPodRotPS)
    {
        this.cmd_speedPodRotPS = cmd_speedPodRotPS;
    }

    /**
     *
     * @return command pod position Star Board
     */
    public int getCmd_podPosSB()
    {
        return cmd_podPosSB;
    }

    /**
     *
     * @return feedback speed pod rotation Port Side
     */
    public int getFb_speedPodRotPS()
    {
        return fb_speedPodRotPS;
    }

    /**
     *
     * @param cmd_podPosSB
     */
    public void setCmd_podPosSB(int cmd_podPosSB)
    {
        this.cmd_podPosSB = cmd_podPosSB;
    }

    /**
     *
     * @return command pod position Port Side
     */
    public int getCmd_podPosPS()
    {
        return cmd_podPosPS;
    }

    /**
     *
     * @param cmd_podPosPS
     */
    public void setCmd_podPosPS(int cmd_podPosPS)
    {
        this.cmd_podPosPS = cmd_podPosPS;
    }

    /**
     *
     * @return command ballast sensor
     */
    public boolean isCmd_ballastSensor()
    {
        return cmd_ballastSensor;
    }

    /**
     *
     * @param cmd_ballastSensor
     */
    public void setCmd_ballastSensor(boolean cmd_ballastSensor)
    {
        this.cmd_ballastSensor = cmd_ballastSensor;
    }

    /**
     *
     * @return Yaw value
     */
    public int getYaw()
    {
        return Yaw;
    }

    /**
     *
     * @return Pitch value
     */
    public int getPitch()
    {
        return Pitch;
    }

    /**
     *
     * @return Roll value
     */
    public int getRoll()
    {
        return Roll;
    }

    /**
     *
     * @return Command respons time
     */
    public long getComResponseTime()
    {
        return comResponseTime;
    }

    /**
     *
     * @param comResponseTime
     */
    public void setComResponseTime(long comResponseTime)
    {
        this.comResponseTime = comResponseTime;
    }

    /**
     *
     * @return true if Speed Star Board is available, if else false
     */
    public boolean isSpeedSBavailable()
    {
        return speedSBavailable;
    }

    /**
     *
     * @param speedSBavailable
     */
    public void setSpeedSBavailable(boolean speedSBavailable)
    {
        this.speedSBavailable = speedSBavailable;
    }

    /**
     *
     * @return true if speed Port Side is available, if else false
     */
    public boolean isSpeedPSavailable()
    {
        return speedPSavailable;
    }

    /**
     *
     * @param speedPSavailable
     */
    public void setSpeedPSavailable(boolean speedPSavailable)
    {
        this.speedPSavailable = speedPSavailable;
    }

    /**
     *
     * @return true if pos position Star Board is available, if else false
     */
    public boolean isPodPosSBavailable()
    {
        return podPosSBavailable;
    }

    /**
     *
     * @param podPosSBavailable
     */
    public void setPodPosSBavailable(boolean podPosSBavailable)
    {
        this.podPosSBavailable = podPosSBavailable;
    }

    /**
     *
     * @return true if pod position Port Side is available, if else false
     */
    public boolean isPodPosPSavailable()
    {
        return podPosPSavailable;
    }

    /**
     *
     * @param podPosPSavailable
     */
    public void setPodPosPSavailable(boolean podPosPSavailable)
    {
        this.podPosPSavailable = podPosPSavailable;
    }

    /**
     *
     * @return true if ballast sensor is available, if else false
     */
    public boolean isBallastSensorAvailable()
    {
        return ballastSensorAvailable;
    }

    /**
     * 
     * @param ballastSensorAvailable
     */
    public void setBallastSensorAvailable(boolean ballastSensorAvailable)
    {
        this.ballastSensorAvailable = ballastSensorAvailable;
    }

    /**
     * Gets request code from Arduino
     *
     * @return Request code
     */
    public byte getRequestCodeFromArduino()
    {
        return requestCodeFromArduino;
    }

    /**
     * Sets request code from Arduino
     *
     * @param requestCodeFromArduino Request code
     */
    public void setRequestCodeFromArduino(byte requestCodeFromArduino)
    {
        this.requestCodeFromArduino = requestCodeFromArduino;
    }

    /**
     * Sets boolean flag to true or false depending on if there are new data
     * available from GUI or not
     *
     * @param state true or false
     */
    public void setDataFromGuiAvailable(boolean state)
    {
        this.dataFromGuiAvailable = state;
    }

    /**
     * Returns boolean flag
     *
     * @return true if new data available, false if not
     */
    public boolean getDataFromGuiAvailable()
    {
        return this.dataFromGuiAvailable;
    }

    /**
     * Ships x position
     * @param xShipPos
     */
    public void setXShipPos(double xShipPos)
    {
        this.xShipPos = xShipPos;
    }

    /**
     * 
     * @return the x position of the ship
     */
    public double getXShipPos()
    {
        return xShipPos;
    }

    /**
     * ships y postion
     * @param yShipPos
     */
    public void setYShipPos(double yShipPos)
    {
        this.yShipPos = xShipPos;
    }

    /**
     *
     * @return the y position of the ship
     */
    public double getYShipPos()
    {
        return yShipPos;
    }

    /**
     * 
     * @return commmand pos accuracy
     */
    public double getCmd_PosAccuracy()
    {
        return posAccuracy;
    }

    /**
     * Poistion accuracy
     * @param posAccuracy
     */
    public void setPosAccuracy(double posAccuracy)
    {
        this.posAccuracy = posAccuracy;
    }

    /**
     * 
     * @return the soft speed of the pods
     */
    public int getSoftSpeedPod()
    {
        return softSpeedPod;
    }

    /**
     * Sets the soft speed of the pods
     * @param softSpeed
     */
    public void setSoftSpeedPod(int softSpeed)
    {
        this.softSpeedPod = softSpeed;
        this.setDataToRemoteUpdated(true);
        this.setDataUpdated(false);
    }

    /**
     * // * ************************************************************** / *
     * ************** PING*********************************
     */
    
    /**
     * 
     * @return vision position data time
     */
    public boolean getVisionPosDataTime()
    {
        return visionPosDataPing;
    }

    /**
     * Data ping from vision position
     * @param visionPosDataPing
     */
    public void setVisionPosDataTime(boolean visionPosDataPing)
    {
        this.visionPosDataPing = visionPosDataPing;

    }

    /**
     * Ping of GUI
     * @param ipAddress
     */
    public void setGuiPing(String ipAddress)
    {
        // this.guiPing = guiPing;
        sendBackGuiPing(ipAddress);
        guiPing = "0.0.0.0";
    }

    private void sendBackGuiPing(String ipAddress)
    {
        udpSender.send(ipAddress, "<PingBack:true>", 5057);

//UDPServer.sendDataString("<ping:" + value + ">")
    }

    /**
     * test method
     * @return test value
     */
    public int getTest()
    {
        return Test;
    }

    /**
     * sets test value
     * @param test
     */
    public void setTest(int test)
    {
        this.Test = Test;
    }

    /**
     * 
     * @return test value 2
     */
    public int getTest2()
    {
        return Test2;
    }

    /**
     * sets test value 2
     * @param Test2
     */
    public void setTest2(int Test2)
    {
        this.Test2 = Test2;
    }

    /**
     * Byte of the thruster command, depending og what byte determines 
     * thruster rotation direction
     * @return
     */
    public synchronized byte getThrusterCommand()
    {
        return thrusterCommand;
    }

    /**
     * sets thruster command, data to GUI true, data updated false
     * @param thrusterCommand
     */
    public synchronized void setThrusterCommand(byte thrusterCommand)
    {
        this.thrusterCommand = thrusterCommand;
        this.setDataToRemoteUpdated(true);
        this.setDataUpdated(false);
    }

    /**
     *
     * @return pod position command Star board
     */
    public byte getPodPosSBCommand()
    {
        return podPosSBCommand;
    }

    /**
     *
     * @param podPosSBCommand
     */
    public void setPodPosSBCommand(byte podPosSBCommand)
    {
        this.podPosSBCommand = podPosSBCommand;
        this.setDataToRemoteUpdated(true);
        this.setDataUpdated(false);
    }

    /**
     *
     * @return pod position command Port Side
     */
    public byte getPodPosPSCommand()
    {
        return podPosPSCommand;
    }

    /**
     *
     * @param podPosPSCommand
     */
    public void setPodPosPSCommand(byte podPosPSCommand)
    {
        this.podPosPSCommand = podPosPSCommand;
        this.setDataToRemoteUpdated(true);
        this.setDataUpdated(false);
    }

    /**
     *
     * @return pod positions and pod speeds combined in one string
     */
    public String getDataToArduino()
    {
        return "podposps:" + this.getFb_podPosPS()
                + ":podpossb:" + this.getFb_podPosSB()
                + ":speedps:" + this.getFb_speedPS()
                + ":speedsb:" + this.getFb_speedSB();
    }

    /**
     *
     * @return pod positions, pod speed and heading in one string
     */
    public String getDataToGUI()
    {
        return "<"
                + "podposps:" + this.getFb_podPosPS()
                + ":podpossb:" + this.getFb_podPosSB()
                + ":speedps:" + this.getFb_speedPS()
                + ":speedsb:" + this.getFb_speedSB()
                + ":heading:" + this.getFb_heading()
                + ">";
    }

    /**
     * Fills list of alarms
     */
    private void fillListOfAlarms()
    {
        this.listOfAlarms.put("sbSpeedFbAlarm", false);
        this.listOfAlarms.put("psSpeedFbAlarm", false);
        this.listOfAlarms.put("sbPodPosFbAlarm", false);
        this.listOfAlarms.put("psPodPosFbAlarm", false);
        this.listOfAlarms.put("visionDeviationAlarm", false);
        this.listOfAlarms.put("imuRollAlarm", false);
        //this.listOfAlarms.put("pingAlarmError", false);
    }

    /**
     * Checks string, with alarms.
     * @param alarmName
     * @param state
     */
    public void handleDataFromAlarmList(String alarmName, boolean state)
    {
        listOfAlarms.put(alarmName, state);
    }

    /**
     * ConcurrentHashMap of alarms 
     * @return list of alarms 
     */
    public ConcurrentHashMap<String, Boolean> getListOfAlarms()
    {
        return listOfAlarms;
    }

    /**
     * sets data to GUI, changes int to string 
     */
    public synchronized void handleDataToRemote()
    {
        //dataToRemote.put("softSpeedPod", Integer.toString(getSoftSpeedPod()));
//        dataToRemote.put("cmd_speedSB", Integer.toString(getCmd_speedSB()));
//        dataToRemote.put("cmd_speedPS", Integer.toString(getCmd_speedPS()));
//        dataToRemote.put("cmd_speedPodRotSB", Integer.toString(getCmd_speedPodRotSB()));
        dataToRemote.put("softSpeedPod", Integer.toString(getSoftSpeedPod()));
        dataToRemote.put("case_cmd_podPosSB", Integer.toString(getPodPosPSCommand()));
        dataToRemote.put("case_cmd_podPosPS", Integer.toString(getPodPosSBCommand()));
        dataToRemote.put("ThrusterCommand", Integer.toString(getThrusterCommand()));
        //this.setDataToRemoteUpdated(true);
//
//        for (Entry e : dataToRemote.entrySet())
//        {
//            String key = (String) e.getKey();
//            String value = (String) e.getValue();
//            String data = ("<" + key + ":" + value + ">");
//            
//            wsd("Com3", arduinoBaudRate, data);
//            //wsd.writeData("Com3", arduinoBaudRate, data);
//
//        }
    }

    /**
     * Handles data from GUI, checks key string and value to the string, 
     * sets the value to correct key.
     */
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
        for (Entry e : data.entrySet())
        {
            String key = (String) e.getKey();
            String value = (String) e.getValue();

            switch (key)
            {
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
                case "fb_speedPodRotPS":
                    this.fb_speedPodRotPS = Integer.parseInt(value);
                    break;
                case "Yaw":
                    this.Yaw = Integer.parseInt(value);
                    break;
                case "Pitch":
                    this.Pitch = Integer.parseInt(value);
                    break;
                case "Roll":
                    this.Roll = Integer.parseInt(value);
                    break;
                case "angle":
                    if (this.ic_angle != Integer.parseInt(value))
                    {
                        this.ic_angle = Integer.parseInt(value);
                    }
                    break;
                case "speed":
                    if (this.ic_speed != Integer.parseInt(value))
                    {
                        this.ic_speed = Integer.parseInt(value);
                    }
                    break;
                case "L1":
                    if (this.ic_L1 != "1".equals(value))
                    {
                        this.setIc_L1("1".equals(value));
                    }
                    break;
                case "R1":
                    if (this.ic_R1 != "1".equals(value))
                    {
                        this.setIc_R1("1".equals(value));
                    }
                    break;
                case "X":
                    if (this.ic_X != "1".equals(value))
                    {
                        this.setIc_X("1".equals(value));
                    }
                    break;
                case "A":
                    if (this.ic_A != "1".equals(value))
                    {
                        this.setIc_A("1".equals(value));
                    }
                    break;
                case "B":
                    if (this.ic_B != "1".equals(value))
                    {
                        this.setIc_B("1".equals(value));
                    }
                    break;
                case "Y":
                    if (this.ic_Y != "1".equals(value))
                    {
                        this.setIc_Y("1".equals(value));
                    }
                    break;
                case "Test":
                    this.Test = Integer.parseInt(value);
                    break;
                case "Test2":
                    this.Test2 = Integer.parseInt(value);
                    break;
                case "GuiPing":
                    this.guiPing = value;
                    setGuiPing(value);
                    break;
            }
        }
    }
}
