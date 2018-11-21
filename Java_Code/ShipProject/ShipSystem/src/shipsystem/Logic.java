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

    protected enum STATES
    {
        DPCONTROLL(0),
        MANUALCONTROLL(1),
        STOP(0),
        GOFWD(1),
        GOREV(-1),
        GOLEFT(10),
        GORIGHT(20),
        GOFWDANDLEFT(11),
        GOFWDANDRIGHT(21),
        GOREVANDLEFT(9),
        GOREVANDRIGHT(19),
        DEFAULT(-99);

        private final int value;

        private STATES(int value)
        {
            this.value = value;
        }

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

    public Logic(DataHandler dh)
    {
        this.dh = dh;
    }

    void prossesButtonCommandsFromGui()
    {
        this.isServoOut = false;
//        this.switchCaseButtonStates();
        this.switchCaseMotorSpeeds();
    }

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

    protected void calculatePodMovement()
    {
        //PS Pod rotation
        if ((dh.getTemp_Angle() > (dh.getFb_podPosPS() + 2))
                && dh.getTemp_Angle() < (dh.getFb_podPosPS() - 2))
        {
            if ((this.calculatedAngle - dh.getFb_podPosPS() + 360) % 360 < 180)
            // clockwise
            {
                dh.setPodPosPSCommand((byte) 1);
            } // anti-clockwise
            else
            {
                dh.setPodPosPSCommand((byte) 2);
            }
        } else
        {
            dh.setPodPosPSCommand((byte) 0);
        }

        //SB Pod rotation
        if ((dh.getTemp_Angle() > (dh.getFb_podPosSB() + 2))
                && dh.getTemp_Angle() < (dh.getFb_podPosSB() - 2))
        {
            if ((this.calculatedAngle - dh.getFb_podPosSB() + 360) % 360 < 180)
            // clockwise
            {
                dh.setPodPosSBCommand((byte) 1);
            } // anti-clockwise
            else
            {
                dh.setPodPosSBCommand((byte) 2);
            }
        } else
        {
            dh.setPodPosSBCommand((byte) 0);
        }
    }

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

    protected void calculateDPAngleSB()
    {
        this.calculatedDPAngleSB = this.calculatedDPAnglePS - 180;

        if (this.calculatedDPAngleSB < 0)
        {
            this.calculatedDPAngleSB = this.calculatedDPAngleSB + 360;
        }
    }

    protected void runPodSpeedPS()
    {
        dh.setCmd_speedPS(inputSpeed);
    }

    protected void runPodSpeedSB()
    {
        dh.setCmd_speedSB(inputSpeed);
    }

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

    protected void podPosPS_OK()
    {
        if (dh.getFb_podPosPS() <= this.calculatedAngle + 30 || dh.getFb_podPosPS() >= this.calculatedAngle - 30)
        {
            this.podPosPSPOK = true;
        }
        else
        {
            this.podPosPSPOK = false;
        }
    }

    protected void podPosSB_OK()
    {
        if (dh.getFb_podPosSB() <= this.calculatedAngle + 30 || dh.getFb_podPosSB() >= this.calculatedAngle - 30)
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
     * sets the correct motorspeeds from state (manual mode)
     */
    protected void switchCaseMotorSpeeds()
    {

        switch (this.getState())
        {
            case STOP:
                dh.setCmd_speedPS(minSpeed);
                dh.setCmd_speedSB(minSpeed);
                break;
            case GOFWD:
                if (this.podPosPSPOK && this.podPosSBOK)
                {
                    dh.setCmd_speedPodRotPS(inputSpeed);
                }
                else
                {
                    dh.setCmd_speedPodRotPS(minSpeed);
                }
                break;
            case GOREV:
                if (dh.getFb_podPosPS() != 0)
                {
                    dh.setCmd_speedPodRotPS(maxSpeed);
                }
                else
                {
                    dh.setCmd_speedPodRotPS(minSpeed);
                }

                if (dh.getFb_podPosSB() != 0)
                {
                    dh.setCmd_speedPodRotSB(maxSpeed);
                }
                else
                {
                    dh.setCmd_speedPodRotSB(minSpeed);
                }

                if (dh.getFb_podPosPS() == 0 && dh.getFb_podPosSB() == 0)
                {
                    dh.setCmd_speedPS(maxSpeed);
                    dh.setCmd_speedSB(maxSpeed);
                }
                break;
            case GOLEFT:
                if (dh.getFb_podPosPS() != 315)
                {
                    dh.setCmd_speedPodRotPS(maxSpeed);
                }
                else
                {
                    dh.setCmd_speedPodRotPS(minSpeed);
                }

                if (dh.getFb_podPosSB() != 315)
                {
                    dh.setCmd_speedPodRotSB(maxSpeed);
                }
                else
                {
                    dh.setCmd_speedPodRotSB(minSpeed);
                }

                if (dh.getFb_podPosPS() == 315 && dh.getFb_podPosSB() == 315)
                {
                    dh.setCmd_speedPS(maxSpeed);
                    dh.setCmd_speedSB(maxSpeed);
                }
                break;
            case GORIGHT:
                if (dh.getFb_podPosPS() != 45)
                {
                    dh.setCmd_speedPodRotPS(maxSpeed);
                }
                else
                {
                    dh.setCmd_speedPodRotPS(minSpeed);
                }

                if (dh.getFb_podPosSB() != 45)
                {
                    dh.setCmd_speedPodRotSB(maxSpeed);
                }
                else
                {
                    dh.setCmd_speedPodRotSB(minSpeed);
                }

                if (dh.getFb_podPosPS() == 45 && dh.getFb_podPosSB() == 45)
                {
                    dh.setCmd_speedPS(maxSpeed);
                    dh.setCmd_speedSB(maxSpeed);
                }
                break;
            case GOFWDANDLEFT:
                if (dh.getFb_podPosPS() != 345)
                {
                    dh.setCmd_speedPodRotPS(maxSpeed);
                }
                else
                {
                    dh.setCmd_speedPodRotPS(minSpeed);
                }

                if (dh.getFb_podPosSB() != 345)
                {
                    dh.setCmd_speedPodRotSB(maxSpeed);
                }
                else
                {
                    dh.setCmd_speedPodRotSB(minSpeed);
                }

                if (dh.getFb_podPosPS() == 345 && dh.getFb_podPosSB() == 345)
                {
                    dh.setCmd_speedPS(maxSpeed);
                    dh.setCmd_speedSB(maxSpeed);
                }
                break;
            case GOFWDANDRIGHT:
                if (dh.getFb_podPosPS() != 15)
                {
                    dh.setCmd_speedPodRotPS(maxSpeed);
                }
                else
                {
                    dh.setCmd_speedPodRotPS(minSpeed);
                }
                if (dh.getFb_podPosSB() != 15)
                {
                    dh.setCmd_speedPodRotSB(maxSpeed);
                }
                else
                {
                    dh.setCmd_speedPodRotSB(minSpeed);
                }
                if (dh.getFb_podPosPS() == 15 && dh.getFb_podPosSB() == 15)
                {
                    dh.setCmd_speedPS(maxSpeed);
                    dh.setCmd_speedSB(maxSpeed);
                }
                break;
            case GOREVANDRIGHT:
                if (dh.getFb_podPosPS() != 15)
                {
                    dh.setCmd_speedPodRotPS(maxSpeed);
                }
                else
                {
                    dh.setCmd_speedPodRotPS(minSpeed);
                }
                if (dh.getFb_podPosSB() != 15)
                {
                    dh.setCmd_speedPodRotSB(maxSpeed);
                }
                else
                {
                    dh.setCmd_speedPodRotSB(minSpeed);
                }
                if (dh.getFb_podPosPS() == 15 && dh.getFb_podPosSB() == 15)
                {
                    dh.setCmd_speedPS(maxSpeed);
                    dh.setCmd_speedSB(maxSpeed);
                }
                break;
            case GOREVANDLEFT:
                if (dh.getFb_podPosPS() != 345)
                {
                    dh.setCmd_speedPodRotPS(maxSpeed);
                }
                else
                {
                    dh.setCmd_speedPodRotPS(minSpeed);
                }

                if (dh.getFb_podPosSB() != 345)
                {
                    dh.setCmd_speedPodRotSB(maxSpeed);
                }
                else
                {
                    dh.setCmd_speedPodRotSB(minSpeed);
                }

                if (dh.getFb_podPosPS() == 345 && dh.getFb_podPosSB() == 345)
                {
                    dh.setCmd_speedPS(maxSpeed);
                    dh.setCmd_speedSB(maxSpeed);
                }
                break;
            // unknown command
            case DEFAULT:
                break;

            default:
                break;
        }
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
