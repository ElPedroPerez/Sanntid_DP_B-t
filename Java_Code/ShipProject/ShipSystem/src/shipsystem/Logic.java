package shipsystem;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Logic class
 * handles the logic that controls the ship and commands
 * @author Haakon, Bjørnar, Robin
 * @version v1.0 30.10.2018
 */
public class Logic
{
    
    protected enum STATES 
    {
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
        
        private int value;
        
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
    private final int maxSpeed = 255;
    private final int minSpeed = 0;
    
    private boolean isServoOut = false;
    
    private STATES state;

    public Logic(DataHandler dh)
    {
        this.dh = dh;
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void prossesButtonCommandsFromGui()
    {
        this.isServoOut = false;
        this.switchCaseButtonStates();
        this.switchCaseMotorSpeeds();
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        
        
    }
    
    /**
     * Sets motor speed to run forward
     * 
     * @param leftSpeed
     * @param rightSpeed
     */
    protected void runFWD(float leftSpeed, float rightSpeed)
    {
     this.setState(STATES.GOFWD);
     this.setLeftSpeed(leftSpeed);
     this.setRightSpeed(rightSpeed);
    }
    
    /**
     * Sets motor speed to run reverse
     * @param leftSpeed
     * @param rightSpeed 
     */
    protected void runRev(float leftSpeed, float rightSpeed)
    {
        this.setState(STATES.GOREV);
        this.setLeftSpeed(leftSpeed);
        this.setRightSpeed(rightSpeed);
    }
    
    /**
     * Sets motor speed to run left
     */
    protected void runLeft()
    {
        this.setState(STATES.GOLEFT);
        
    }
    
    /**
     * Sets motor speed to run right
     */
    protected void runRight()
    {
        this.setState(STATES.GORIGHT);
    }
    
    /**
     * Sets motor speed zero/stop
     */ 
    protected void stop()
    {
        this.setState(STATES.STOP);
    }
    
    protected void handleButtonState()
    {
        int buttonState = 0;
        
        if (0 != dh.getFromGuiByte((byte) 0))
        {
            if(!(((1 == dh.getFwd()) && (1 == dh.getRev())) || 
                    ((1 == dh.getLeft()) && (1 == dh.getRight()))))
            {
                if (1 == dh.getFwd())
                {
                    buttonState = STATES.GOFWD.getValue();
                }
                else if (1 == dh.getRev())
                {
                    buttonState = STATES.GOREV.getValue();
                }
                if (1 == dh.getLeft())
                {
                    buttonState = STATES.GOLEFT.getValue();
                } else if (1 == dh.getRight())
                {
                    buttonState += STATES.GORIGHT.getValue();
                }
            }
        }
        this.setStateByValue(buttonState);
    }
    
    
    /**
     * Selects the correct movement of the ship from state
     */
    protected void switchCaseButtonStates()
    {
        dh.resetToArduinoByte(0);
        
        switch (this.getState())
        {
            case STOP:
                dh.stopAUV();
                break;
            case GOFWD:
                dh.goFwd();
                break;
            case GOREV:
                dh.goRev();
                break;
            case GOLEFT:
                dh.goLeft();
                break;
            case GORIGHT:
                dh.goRight();
                break;
            case GOFWDANDLEFT:
                dh.goFwd();
                break;
            case GOFWDANDRIGHT:
                dh.goFwd();
                break;
            case GOREVANDRIGHT:
                dh.goRev();
                break;
            case GOREVANDLEFT:
                dh.goRev();
                break;
                // unknown command
            case DEFAULT:
                break;
            default:
                break;
        }
    }
    
    /**
     * sets the correct motorspeeds from state (manual mode)
     */
    
    protected void switchCaseMotorSpeeds()
    {
        
        switch (this.getState())
        {
            case STOP:
                dh.setLeftMotorSpeed(minSpeed);
                dh.setRightMotorSpeed(minSpeed);
                break;
            case GOFWD: 
                dh.setLeftMotorSpeed(maxSpeed);
                dh.setRightMotorSpeed(maxSpeed);
                break;
            case GOREV:
                dh.setLeftMotorSpeed(maxSpeed);
                dh.setRightMotorSpeed(maxSpeed);
                break;
            case GOLEFT:
                dh.setLeftMotorSpeed(maxSpeed);
                dh.setRightMotorSpeed(maxSpeed);
                break;
            case GORIGHT:
                dh.setLeftMotorSpeed(maxSpeed);
                dh.setRightMotorSpeed(maxSpeed);
                break;
            case GOFWDANDLEFT:
                System.out.println("fwd and left");
                dh.setLeftMotorSpeed(maxSpeed);
                dh.setRightMotorSpeed(maxSpeed/4);
                break;
            case GOFWDANDRIGHT:
                dh.setLeftMotorSpeed(maxSpeed/4);
                dh.setRightMotorSpeed(maxSpeed);
                break;
            case GOREVANDRIGHT:
                dh.setLeftMotorSpeed(maxSpeed/4);
                dh.setRightMotorSpeed(maxSpeed);
                break;
            case GOREVANDLEFT:
                dh.setLeftMotorSpeed(maxSpeed);
                dh.setRightMotorSpeed(maxSpeed/4);
                break;
                // unknown command
            case DEFAULT:
                break;
                
                default:
                    break;
        }
    }
       
    /**
     * Set right motorspeed
     * @param rightSpeed 
     */   
    public void setRightSpeed(float rightSpeed)
    {
        dh.setRightMotorSpeed(rightSpeed);
    }
    
    /**
     * Set right thruster speed
     * @param rightThruster 
     */
    public void setRightThrusterSpeed(float rightThruster)
    {
        dh.setRightThrusterSpeed(rightThruster);
    }
    
    
    /**
     * Set left motorspeed
     * @param leftSpeed 
     */
    public void setLeftSpeed(float leftSpeed)
    {
        dh.setLeftMotorSpeed(leftSpeed);
    }
    
    /**
     * Set left thruster speed
     * @param leftThruster 
     */
    public void setLeftThrusterSpeed (float leftThruster)
    {
        dh.setLeftThrusterSpeed(leftThruster);
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
