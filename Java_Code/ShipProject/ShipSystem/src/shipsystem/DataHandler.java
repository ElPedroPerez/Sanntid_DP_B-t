/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shipsystem;

import SerialCom.SerialDataHandler;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Overview protocol: To Arduino: Byte 0: bit 0 - stopp bit 1 - fwd bit 2 - rev
 * bit 3 - left bit 4 - right Byte 1: Left motor speed Byte 2: Right motor speed
 * Byte 3: bit 0 - left servo bit 1 - right servo bit 2 - auto/manual bit 3 -
 * start bit 7 - request feedback Byte 4: Sensitivity Byte 5: Reserved
 *
 * From Arduino: Byte 0: Pixy x value low byte Byte 1: Pixy x value high byte
 * Byte 2: Pixy y value low byte Byte 3: Pixy y value high byte Byte 4: Distance
 * sensor 4-30 cm Byte 5: Reserved
 *
 * @author Eivind Fugledal
 */
public class DataHandler
{

    private SerialDataHandler sdh = new SerialDataHandler();
    private String arduinoFeedbackComPort;
    private String arduinoCommandComPort;
    private String arduinoFeedbackComPortIMU;
    private int arduinoBaudRate;

    private byte[] dataFromArduino;
    private byte[] dataToArduino;
    private byte[] dataFromGui;
    private boolean dataFromArduinoAvaliable = false;
    private boolean dataFromGuiAvailable = false;
    private boolean threadStatus;
    private byte requestCodeFromArduino;

    private int fb_speedSB;
    private int fb_speedPS;
    private int fb_podPosSB;
    private int fb_podPosPS;
    private boolean fb_ballastSensor;
    private int Yaw;
    private int Pitch;
    private int Roll;

    private int cmd_speedSB;
    private int cmd_speedPS;
    private int cmd_speedPodRotSB;
    private int cmd_speedPodRotPS;
    private int cmd_podPosSB;
    private int cmd_podPosPS;
    private boolean cmd_ballastSensor;

    private boolean speedSBavailable;
    private boolean speedPSavailable;
    private boolean podPosSBavailable;
    private boolean podPosPSavailable;
    private boolean ballastSensorAvailable;

    // pid parameters
    private double P; // prop gain
    private double I; // integral gain
    private double D; // derivation gain
    private double F; // feed fwd gain
    private double RR; // output ramp rate (max delta output)
    private boolean PIDparamChanged;
    public ConcurrentHashMap<String, String> data = new ConcurrentHashMap<>();

    public DataHandler()
    {
        this.dataFromArduino = new byte[6];
        this.dataToArduino = new byte[6];
        this.dataFromGui = new byte[6];
        dataFromArduinoAvaliable = false;
        dataFromGuiAvailable = false;

        arduinoFeedbackComPort = "Com3";
        arduinoFeedbackComPortIMU = "Com4";
        arduinoCommandComPort = "Com2";
        arduinoBaudRate = 115200;

        fb_speedSB = 0;
        fb_speedPS = 0;
        fb_podPosSB = 0;
        fb_podPosPS = 0;
        fb_ballastSensor = false;
        Yaw = 0;
        Pitch = 0;
        Roll = 0;

        speedSBavailable = false;
        speedPSavailable = false;
        podPosSBavailable = false;
        podPosPSavailable = false;
        ballastSensorAvailable = false;
    }

    //*****************************************************************
    //********************** THREAD STATUS METHODS*********************
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

    public void setPidParamChanged(boolean state)
    {
        this.PIDparamChanged = state;
    }

    public boolean getPidParamChanged()
    {
        return this.PIDparamChanged;
    }

    //*****************************************************************
    //*************** FROM ARDUINO METHODS*****************************
    public void handleDataFromArduino(byte[] data)
    {
        // check if the array is of the same length and the requestcode has changed
        if (data.length == this.dataFromArduino.length && data[Protocol.REQUEST_FEEDBACK.getValue()] != this.getRequestCodeFromArduino())
        {
            this.dataFromArduino = data;
            //this.setDistanceSensor(data[4]);
            this.setRequestCodeFromArduino(data[Protocol.REQUEST_FEEDBACK.getValue()]);
            //this.setPixyXvalue(new BigInteger(Arrays.copyOfRange(data, 0, 2)).intValue());
            //this.setPixyYvalue(new BigInteger(Arrays.copyOfRange(data, 2, 4)).intValue());
            this.dataFromArduinoAvaliable = true;
        }
    }

    public byte[] getDataFromArduino()
    {
        return dataFromArduino;
    }

    /**
     *
     * @return true if new data available, false if not
     */
    public boolean isDataFromArduinoAvailable()
    {
        return this.dataFromArduinoAvaliable;
    }

    public int getFb_speedSB()
    {
        return fb_speedSB;
    }

    public void setFb_speedSB(int fb_speedSB)
    {
        this.fb_speedSB = fb_speedSB;
    }

    public int getFb_speedPS()
    {
        return fb_speedPS;
    }

    public void setFb_speedPS(int fb_speedPS)
    {
        this.fb_speedPS = fb_speedPS;
    }

    public int getFb_podPosSB()
    {
        return fb_podPosSB;
    }

    public void setFb_podPosSB(int fb_podPosSB)
    {
        this.fb_podPosSB = fb_podPosSB;
    }

    public int getFb_podPosPS()
    {
        return fb_podPosPS;
    }

    public void setFb_podPosPS(int fb_podPosPS)
    {
        this.fb_podPosPS = fb_podPosPS;
    }

    public boolean isFb_ballastSensor()
    {
        return fb_ballastSensor;
    }

    public void setFb_ballastSensor(boolean fb_ballastSensor)
    {
        this.fb_ballastSensor = fb_ballastSensor;
    }

    public int getCmd_speedSB()
    {
        return cmd_speedSB;
    }

    public void setCmd_speedSB(int cmd_speedSB)
    {
        this.cmd_speedSB = cmd_speedSB;
    }

    public int getCmd_speedPS()
    {
        return cmd_speedPS;
    }

    public void setCmd_speedPS(int cmd_speedPS)
    {
        this.cmd_speedPS = cmd_speedPS;
    }

    public int getCmd_speedPodRotSB()
    {
        return cmd_speedPodRotSB;
    }

    public void setCmd_speedPodRotSB(int cmd_speedPodRotSB)
    {
        this.cmd_speedPodRotSB = cmd_speedPodRotSB;
    }

    public int getCmd_speedPodRotPS()
    {
        return cmd_speedPodRotPS;
    }

    public void setCmd_speedPodRotPS(int cmd_speedPodRotPS)
    {
        this.cmd_speedPodRotPS = cmd_speedPodRotPS;
    }

    public int getCmd_podPosSB()
    {
        return cmd_podPosSB;
    }

    public void setCmd_podPosSB(int cmd_podPosSB)
    {
        this.cmd_podPosSB = cmd_podPosSB;
    }

    public int getCmd_podPosPS()
    {
        return cmd_podPosPS;
    }

    public void setCmd_podPosPS(int cmd_podPosPS)
    {
        this.cmd_podPosPS = cmd_podPosPS;
    }

    public boolean isCmd_ballastSensor()
    {
        return cmd_ballastSensor;
    }

    public void setCmd_ballastSensor(boolean cmd_ballastSensor)
    {
        this.cmd_ballastSensor = cmd_ballastSensor;
    }

    public int getYaw()
    {
        return Yaw;
    }

    public int getPitch()
    {
        return Pitch;
    }

    public int getRoll()
    {
        return Roll;
    }

    public boolean isSpeedSBavailable()
    {
        return speedSBavailable;
    }

    public void setSpeedSBavailable(boolean speedSBavailable)
    {
        this.speedSBavailable = speedSBavailable;
    }

    public boolean isSpeedPSavailable()
    {
        return speedPSavailable;
    }

    public void setSpeedPSavailable(boolean speedPSavailable)
    {
        this.speedPSavailable = speedPSavailable;
    }

    public boolean isPodPosSBavailable()
    {
        return podPosSBavailable;
    }

    public void setPodPosSBavailable(boolean podPosSBavailable)
    {
        this.podPosSBavailable = podPosSBavailable;
    }

    public boolean isPodPosPSavailable()
    {
        return podPosPSavailable;
    }

    public void setPodPosPSavailable(boolean podPosPSavailable)
    {
        this.podPosPSavailable = podPosPSavailable;
    }

    public boolean isBallastSensorAvailable()
    {
        return ballastSensorAvailable;
    }

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

    //****************************************************************
    //************** FROM GUI METHODS*********************************
    /**
     * Gets the byte array containing data from GUI
     *
     * @return The byte array
     */
    public byte[] getDataFromController()
    {
        ShipSystem.enumStateEvent = SendEventState.FALSE;
        return this.dataToArduino;
    }

    /**
     * Sets the byte array containing data from GUI
     *
     * @param data New byte array
     */
    public void setDataFromGUI(byte[] data)
    {

        for (int i = 0; i < 6; i++)
        {
            this.dataFromGui[i] = data[i];
        }

        // pid parameters
        double P = (double) data[6] / 10.0;
        double I = (double) data[7] / 10.0;
        double D = (double) data[8] / 10.0;
        double F = (double) data[9] / 10.0;    // feed fwd
        double RR = (double) data[10] / 10.0; // ramp rate

        // set new values if value changed
        if (P != this.P)
        {
            this.P = P;
            this.PIDparamChanged = true;
        }
        if (I != this.I)
        {
            this.I = I;
            this.PIDparamChanged = true;
        }
        if (D != this.D)
        {
            this.D = D;
            this.PIDparamChanged = true;
        }
        if (F != this.F)
        {
            this.F = F;
            this.PIDparamChanged = true;
        }
        if (RR != this.RR)
        {
            this.RR = RR;
            this.PIDparamChanged = true;
        }

        this.setDataFromGuiAvailable(true);

        // Values below should be equal in both dataFromGui and dataToArduino
        //this.dataToArduino[Protocol.CONTROLS.getValue()] = this.dataFromGui[Protocol.CONTROLS.getValue()];
        //this.dataToArduino[Protocol.COMMANDS.getValue()] = this.dataFromGui[Protocol.COMMANDS.getValue()];
        this.dataToArduino[Protocol.SENSITIVITY.getValue()] = this.dataFromGui[Protocol.SENSITIVITY.getValue()];

        this.fireStateChanged();
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
     * Sets the value of sensitivity given from GUI (in percent)
     *
     * @param sensitivity Value between 0-100 percent
     */
    public void setSensitivity(byte sensitivity)
    {
        dataFromGui[Protocol.SENSITIVITY.getValue()] = sensitivity;
        this.fireStateChanged();
    }

    /**
     * Gets the sensitivity value
     *
     * @return Sensitivity value, between 0-100
     */
    public int getSensitivity()
    {
        return dataFromGui[Protocol.SENSITIVITY.getValue()] & 0xFF;
    }

    /**
     * Gets the request code
     *
     * @return The request code
     */
    public byte getRequestCodeFromGui()
    {
        return this.dataFromGui[Protocol.REQUEST_FEEDBACK.getValue()];
    }

    public void incrementRequestCode()
    {
        dataToArduino[Protocol.REQUEST_FEEDBACK.getValue()]++;
        this.fireStateChanged();
    }

    public void setRequestCodeToArduino(byte code)
    {
        dataToArduino[Protocol.REQUEST_FEEDBACK.getValue()] = code;
        this.fireStateChanged();
    }

    public void fireStateChanged()
    {
        ShipSystem.enumStateEvent = SendEventState.TRUE;
    }

    public boolean checkSendDataAvailable()
    {
        return ShipSystem.enumStateEvent == SendEventState.TRUE;
    }

    public String getDataToArduino()
    {
        return "podposps:" + this.getFb_podPosPS()
                + ":podpossb:" + this.getFb_podPosSB()
                + ":speedps:" + this.getFb_speedPS()
                + ":speedsb:" + this.getFb_speedSB();
    }

    public void handleDataFromArduino()
    {

//        ConcurrentHashMap<String, String> dataFeedback = new ConcurrentHashMap<>();
//        ConcurrentHashMap<String, String> dataIMU = new ConcurrentHashMap<>();

//        dataFeedback = 
        sdh.readData(this, arduinoFeedbackComPort, arduinoBaudRate);
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
                case "Yaw":
                    this.Yaw = Integer.parseInt(value);
                    break;
                case "Pitch":
                    this.Pitch = Integer.parseInt(value);
                    break;
                case "Roll":
                    this.Roll = Integer.parseInt(value);
                    break;
            }
        }
    }
}
