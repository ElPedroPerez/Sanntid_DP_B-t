/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shipsystem;

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

    private byte[] dataFromArduino;
    private byte[] dataToArduino;
    private byte[] dataFromGui;
    private boolean dataFromArduinoAvaliable = false;
    private boolean dataFromGuiAvailable = false;
    private boolean threadStatus;
    private byte requestCodeFromArduino;

    private int speedSB;
    private int speedPS;
    private int podPosSB;
    private int podPosPS;
    private boolean ballastSensor;

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

    public DataHandler()
    {
        this.dataFromArduino = new byte[6];
        this.dataToArduino = new byte[6];
        this.dataFromGui = new byte[6];
        
        speedSB = 0;
        speedPS = 0;
        podPosSB = 0;
        podPosPS = 0;
        ballastSensor = false;
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
            this.setDistanceSensor(data[4]);
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

    public int getSpeedSB()
    {
        return speedSB;
    }

    public void setSpeedSB(int speedSB)
    {
        this.speedSB = speedSB;
    }

    public int getSpeedPS()
    {
        return speedPS;
    }

    public void setSpeedPS(int speedPS)
    {
        this.speedPS = speedPS;
    }

    public int getPodPosSB()
    {
        return podPosSB;
    }

    public void setPodPosSB(int podPosSB)
    {
        this.podPosSB = podPosSB;
    }

    public int getPodPosPS()
    {
        return podPosPS;
    }

    public void setPodPosPS(int podPosPS)
    {
        this.podPosPS = podPosPS;
    }

    public boolean isBallastSensor()
    {
        return ballastSensor;
    }

    public void setBallastSensor(boolean ballastSensor)
    {
        this.ballastSensor = ballastSensor;
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
     * Sets left motor speed
     *
     * @param speed Speed value between 0-255
     */
    public void setLeftMotorSpeed(float speed)
    {
        if (speed > 255.0f)
        {
            speed = 255.0f;
        }
        //System.out.println("left speed " + speed);
        dataToArduino[Protocol.LEFT_MOTOR_SPEED.getValue()] = (byte) ((speed / 100) * this.getSensitivity());
        this.fireStateChanged();

    }

    public int getLeftMotorSpeed()
    {
        if (this.getSensitivity() == 0)
        {
            return 0;
        }
        else
        {
            return dataToArduino[Protocol.LEFT_MOTOR_SPEED.getValue()] * (100 / this.getSensitivity());
        }
    }
<<<<<<< HEAD
    
    public void setLeftThrusterSpeed(float speed)
    {
        if (speed > 255.0f)
        {
            speed = 255.0f;
        }
        //System.out.println("left thruster speed " + speed);
        dataToArduino[Protocol.LEFT_THRUSTER_SPEED.getValue()] = (byte) ((speed / 100) * this.getSensitivity());
        this.fireStateChanged();
           
    }
    
    public int getLeftThrusterSpeed()
    {
        if (this.getSensitivity() == 0)
        {
            return 0;
        }
        else
        {
            return dataToArduino[Protocol.LEFT_THRUSTER_SPEED.getValue()] * (100 / this.getSensitivity());
        }
    }
    
 
    public void setLeftThrusterAngle(float angle)
    { 
        dataToArduino[Protocol.LEFT_THRUSTER_ANGLE.getValue()] = (byte) ((angle / 100) * this.getSensitivity());
    }
    
    
    public int getLeftThrusterAngle()
    {
        if (this.getSensitivity() == 0)
        {
            return 0;
        }
        else
        {
            return dataToArduino[Protocol.LEFT_THRUSTER_ANGLE.getValue()] * (100 / this.getSensitivity());
        }
    }
    
        public void setRightThrusterAngle(float angle)
    { 
        dataToArduino[Protocol.RIGHT_THRUSTER_ANGLE.getValue()] = (byte) ((angle / 100) * this.getSensitivity());
    }
    
    
    public int getRightThrusterAngle()
    {
        if (this.getSensitivity() == 0)
        {
            return 0;
        }
        else
        {
            return dataToArduino[Protocol.RIGHT_THRUSTER_ANGLE.getValue()] * (100 / this.getSensitivity());
        }
    }
    
=======

>>>>>>> 37b8b84cedfcd0021286ca468c1bfd087d8593b2
    /**
     * Sets right motor speed
     *
     * @param speed Speed value between 0-255
     */
    public void setRightMotorSpeed(float speed)
    {
        if (speed > 255.0f)
        {
            speed = 255.0f;
        }
        //System.out.println("right speed " + speed);
        dataToArduino[Protocol.RIGHT_MOTOR_SPEED.getValue()] = (byte) ((speed / 100) * this.getSensitivity());
        this.fireStateChanged();
    }

    public int getRightMotorSpeed()
    {
        if (this.getSensitivity() == 0)
        {
            return 0;
        }
        else
        {
            return dataToArduino[Protocol.RIGHT_MOTOR_SPEED.getValue()] * (100 / this.getSensitivity());
        }
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
        return "";
    }
    
}
