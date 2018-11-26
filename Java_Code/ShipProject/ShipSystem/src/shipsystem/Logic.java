package shipsystem;

import SerialCom.SerialDataHandler;

/**
 * Logic class handles the logic that controls the ship and commands
 *
 * @author Haakon, Bjørnar, Robin
 * @version v1.0 30.10.2018
 */
public class Logic
{

    /**
     *
     */
    protected enum STATES
    {

        /**
         * States, for dp controll, manual controll, go forward and default.
         */
        DPCONTROLL(0),
        MANUALCONTROLL(1),
        GOFWD(1),
        DEFAULT(-99);

        private final int value;

        private STATES(int value)
        {
            this.value = value;
        }

        /**
         *
         * @return
         */
        protected int getValue()
        {
            return this.value;
        }
    }

    private final DataHandler dh;
    private final int maxSpeed = 100;
    private final int minSpeed = 0;
    private int calculatedAngle;
    private int inputAngle;
    private int inputSpeed;
    private boolean input_L1;
    private boolean input_R1;
    private boolean input_X;
    private boolean input_A;
    private boolean input_B;
    private boolean input_Y;

    private boolean podPosPSPOK;
    private boolean podPosSBOK;

    private int inputDPAngle;

    private int calibratedZeroIMU = 100;

    private int calculatedDPAnglePS;
    private int calculatedDPAngleSB;

    private boolean isServoOut = false;

    private STATES state;

    /**
     *
     * @param dh
     */
    public Logic(DataHandler dh)
    {
        this.dh = dh;
    }

    void prossesButtonCommandsFromGui()
    {
        this.isServoOut = false;
//        this.switchCaseButtonStates();
//        this.switchCaseMotorSpeeds();
    }

    /**
     * Responsible for deciding the direction which the bow thruster should rotate
     */
    protected void bowThrusterSignal()
    {
        if (dh.getIc_R1() && dh.isDataUpdated())
        {
            dh.setThrusterCommand((byte) 1);
        }
        if (dh.getIc_L1() && dh.isDataUpdated())
        {
            dh.setThrusterCommand((byte) 2);
        }
        if (!dh.getIc_R1() && !dh.getIc_L1())
        {
            if (dh.thrusterCommand != 0)
            {
                dh.setThrusterCommand((byte) 0);
            }
        }

//         else if (!dh.ic_R1 && dh.isDataToRemoteUpdated())
//        {
//            dh.setThrusterCommand((byte) 0);
//        }
    }

    /**
     * Reads angle on controller and calculates the value og angle on pods.
     */
    protected void calculateAngle()
    {

        this.inputAngle = dh.getIc_angle();
        this.calculatedAngle = 180 - this.inputAngle;

        if (this.calculatedAngle < 0)
        {
            this.calculatedAngle = this.calculatedAngle + 360;
        }

        dh.setTemp_Angle(this.calculatedAngle);
        calculatePodMovement();

    }

    /**
     * Finds the shortes route to new angle.
     */
    protected void calculatePodMovement()
    {
        //PS Pod rotation
        if ((dh.getTemp_Angle() > (dh.getFb_podPosPS() + 15))
                || dh.getTemp_Angle() < (dh.getFb_podPosPS() - 15))
        {
            if ((this.calculatedAngle - dh.getFb_podPosPS() + 360) % 360 < 180)
            // clockwise
            {
                if (dh.podPosPSCommand != 2)
                {
                    dh.setPodPosPSCommand((byte) 2);
                    dh.setPodPosSBCommand((byte) 2);
                }
            } // anti-clockwise
            else
            {
                if (dh.podPosPSCommand != 1)
                {
                    dh.setPodPosPSCommand((byte) 1);
                    dh.setPodPosSBCommand((byte) 1);
                }
            }
        } else if (dh.podPosPSCommand != 0)
        {
            dh.setPodPosPSCommand((byte) 0);
            dh.setPodPosSBCommand((byte) 0);
        }

        //SB Pod rotation
//        if ((dh.getTemp_Angle() > (dh.getFb_podPosSB() + 20))
//                || dh.getTemp_Angle() < (dh.getFb_podPosSB() - 20))
//        {
//            if ((this.calculatedAngle - dh.getFb_podPosSB() + 360) % 360 < 180)
//            // clockwise
//            {
//                if (dh.podPosSBCommand != 2)
//                {
//                    dh.setPodPosSBCommand((byte) 2);
//                }
//            } // anti-clockwise
//            else
//            {
//                if (dh.podPosSBCommand != 1)
//                {
//                    dh.setPodPosSBCommand((byte) 1);
//                }
//            }
//        } else
//        {
//            if (dh.podPosSBCommand != 0)
//            {
//                dh.setPodPosSBCommand((byte) 0);
//            }
//        }
    }

    /**
     * Calculates angle when in DP mode.
     */
    protected void calculateDPAnglePS()
    {
        if (this.inputDPAngle >= 180)
        {
            this.calculatedDPAnglePS = this.inputDPAngle - 180 - (dh.getYaw() - this.calibratedZeroIMU);

            if (this.calculatedDPAnglePS > 360)
            {
                this.calculatedDPAnglePS = this.calculatedDPAnglePS - 360;
            }

            if (this.calculatedDPAnglePS < 0)
            {
                this.calculatedDPAnglePS = this.calculatedDPAnglePS - 360;
            }
        }
        else
        {
            this.calculatedDPAnglePS = this.inputDPAngle + 180 - (dh.getYaw() - this.calibratedZeroIMU);
        }

        if (this.calculatedDPAnglePS > 360)
        {
            this.calculatedDPAnglePS = this.calculatedDPAnglePS - 360;
        }

        if (this.calculatedDPAnglePS < 0)
        {
            this.calculatedDPAnglePS = this.calculatedDPAnglePS + 360;
        }

    }
    /**
     * Calculate angle to be on the opposite side of the Port Side pod.
     */
    protected void calculateDPAngleSB()
    {
        this.calculatedDPAngleSB = this.calculatedDPAnglePS - 180;

        if (this.calculatedDPAngleSB < 0)
        {
            this.calculatedDPAngleSB = this.calculatedDPAngleSB + 360;
        }
    }

    /**
     * Sets the speed of the pod motors equal to the speed of the controller.
     */
    protected void runPodSpeed()
    {
        if (this.podPosPSPOK && this.podPosSBOK)
        {
            dh.setCmd_speedPS(inputSpeed);
            dh.setCmd_speedSB(inputSpeed);
        }
        else
        {
            dh.setCmd_speedPS(minSpeed);
            dh.setCmd_speedSB(minSpeed);
        }
    }

    /**
     * Checks if feedback angle is the same as the calculated angle.
     * If angles does not match, the rotation motor runs.
     */
    protected void runPodRotPS()
    {
        if (this.calculatedAngle != dh.getFb_podPosPS())
        {
            dh.setCmd_speedPodRotPS(maxSpeed);
        }
        else
        {
            dh.setCmd_speedPodRotPS(minSpeed);
        }
    }

    /**
     * Checks if feedback angle is the same as the calculated angle.
     * If angles does not match, the rotation motor runs.
     */
    protected void runPodRotSB()
    {
        if (this.calculatedAngle != dh.getFb_podPosSB())
        {
            dh.setCmd_speedPodRotSB(maxSpeed);
        }
        else
        {
            dh.setCmd_speedPodRotSB(minSpeed);
        }
    }

    /**
     * Checks if feedback value is within a set offset.
     */
    protected void podPosPS_OK()
    {
        if (dh.getFb_podPosPS() <= this.calculatedAngle + 5 || dh.getFb_podPosPS() >= this.calculatedAngle - 5)
        {
            this.podPosPSPOK = true;
        }
        else
        {
            this.podPosPSPOK = false;
        }
    }

    /**
     * Checks if feedback value is within a set offset.
     */
    protected void podPosSB_OK()
    {
        if (dh.getFb_podPosSB() <= this.calculatedAngle + 5 || dh.getFb_podPosSB() >= this.calculatedAngle - 5)
        {
            this.podPosSBOK = true;
        }
        else
        {
            this.podPosSBOK = false;
        }
    }

    /**
     * Sets system to DP controll
     */
    protected void runInDPControll()
    {
        this.setState(STATES.DPCONTROLL);
    }

    /**
     * Sets system to manual controll.
     */
    protected void runInManualCONTROL()
    {
        this.setState(STATES.MANUALCONTROLL);
    }

   

    /**
     * Set starboard motorspeed
     *
     * @param speedSB
     */
    public void setSBSpeed(int speedSB)
    {
        dh.setCmd_speedSB(speedSB);
    }

    /**
     * Set starboard pod motorspeed
     *
     * @param speedPodSB
     */
    public void setSBpodSpeed(int speedPodSB)
    {
        dh.setCmd_speedPodRotSB(speedPodSB);
    }

    /**
     * Set portside motorspeed
     *
     * @param speedPS
     */
    public void setPSSpeed(int speedPS)
    {
        dh.setCmd_speedPS(speedPS);
    }

    /**
     * Set portside pod motorspeed
     *
     * @param speedPodPS
     */
    public void setPSpodSpeed(int speedPodPS)
    {
        dh.setCmd_speedPodRotPS(speedPodPS);
    }

    /**
     * Get the state the ship is currently in
     *
     * @return
     */
    private STATES getState()
    {
        return state;
    }

    /**
     * set the state of movement to the vehicle
     *
     * @param state the new state of the vehicle
     */
    private void setState(STATES state)
    {
        this.state = state;
    }

    /**
     * Set state by value of the wanted state
     *
     * @param value integer value of the state
     */
    private void setStateByValue(int value)
    {
        this.state = this.findState(value);
    }

    /**
     * find a state by state value
     *
     * @param value the integer representation of the state
     * @return
     */
    private STATES findState(int value)
    {
        STATES[] stateArray = STATES.values();
        for (STATES s : stateArray)
        {
            if (s.getValue() == value)
            {
                return s;
            }
        }
        return STATES.DEFAULT;
    }

}
