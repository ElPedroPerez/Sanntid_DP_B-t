package shipsystem;

import SerialCom.SerialDataHandler;


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
//        this.switchCaseButtonStates();
        this.switchCaseMotorSpeeds();
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        
        
    }
    
//    /**
//     * Sets motor speed to run forward
//     * 
//     * @param leftSpeed
//     * @param rightSpeed
//     */
//    protected void runFWD(float leftSpeed, float rightSpeed)
//    {
//     this.setState(STATES.GOFWD);
//     this.setLeftSpeed(leftSpeed);
//     this.setRightSpeed(rightSpeed);
//    }
//    
//    /**
//     * Sets motor speed to run reverse
//     * @param leftSpeed
//     * @param rightSpeed 
//     */
//    protected void runRev(float leftSpeed, float rightSpeed)
//    {
//        this.setState(STATES.GOREV);
//        this.setLeftSpeed(leftSpeed);
//        this.setRightSpeed(rightSpeed);
//    }
    
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
//    
//    protected void handleButtonState()
//    {
//        int buttonState = 0;
//   
//        if (0 != dh.getFromGuiByte((byte) 0))
//        {
//            if(!(((1 == dh.getFwd()) && (1 == dh.getRev())) || 
//                    ((1 == dh.getLeft()) && (1 == dh.getRight()))))
//            {
//                if (1 == dh.getFwd())
//                {
//                    buttonState = STATES.GOFWD.getValue();
//                }
//                else if (1 == dh.getRev())
//                {
//                    buttonState = STATES.GOREV.getValue();
//                }
//                if (1 == dh.getLeft())
//                {
//                    buttonState = STATES.GOLEFT.getValue();
//                } else if (1 == dh.getRight())
//                {
//                    buttonState += STATES.GORIGHT.getValue();
//                }
//            }
//        }
//        this.setStateByValue(buttonState);
//    }
//    
//    
//    /**
//     * Selects the correct movement of the ship from state
//     */
//    protected void switchCaseButtonStates()
//    {
//        dh.resetToArduinoByte(0);
//        
//        switch (this.getState())
//        {
//            case STOP:
//                dh.stopAUV();
//                break;
//            case GOFWD:
//                dh.goFwd();
//                break;
//            case GOREV:
//                dh.goRev();
//                break;
//            case GOLEFT:
//                dh.goLeft();
//                break;
//            case GORIGHT:
//                dh.goRight();
//                break;
//            case GOFWDANDLEFT:
//                dh.goFwd();
//                break;
//            case GOFWDANDRIGHT:
//                dh.goFwd();
//                break;
//            case GOREVANDRIGHT:
//                dh.goRev();
//                break;
//            case GOREVANDLEFT:
//                dh.goRev();
//                break;
//                // unknown command
//            case DEFAULT:
//                break;
//            default:
//                break;
//        }
//    }
    
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
                if(dh.getFb_podPosPS() != 0)
                {
                    dh.setCmd_speedPodRotPS(maxSpeed);                   
                }
                else
                {
                    dh.setCmd_speedPodRotPS(minSpeed);             
                }
                
                if(dh.getFb_podPosSB() != 0)
                {
                   dh.setCmd_speedPodRotSB(maxSpeed); 
                }
                else
                {
                   dh.setCmd_speedPodRotSB(minSpeed); 
                }
                
                if (dh.getFb_podPosPS()== 0 && dh.getFb_podPosSB()== 0)
                {
                dh.setCmd_speedPS(maxSpeed);
                dh.setCmd_speedSB(maxSpeed);
                }
                break;
            case GOREV:
                 if(dh.getFb_podPosPS()!= 0)
                {
                    dh.setCmd_speedPodRotPS(maxSpeed);                   
                }
                else
                {
                    dh.setCmd_speedPodRotPS(minSpeed);            
                }
                 
                 if(dh.getFb_podPosSB() != 0)
                 {
                     dh.setCmd_speedPodRotSB(maxSpeed);
                 }
                 else
                 {
                    dh.setCmd_speedPodRotSB(minSpeed); 
                 }
                 
                if (dh.getFb_podPosPS()== 0 && dh.getFb_podPosSB()== 0)
                {
                dh.setCmd_speedPS(maxSpeed);
                dh.setCmd_speedSB(maxSpeed);
                }
                break;
            case GOLEFT:
                if (dh.getFb_podPosPS()!= 315)
                {
                    dh.setCmd_speedPodRotPS(maxSpeed);                   
                }
                else 
                {
                   dh.setCmd_speedPodRotPS(minSpeed);              
                }
                
                if(dh.getFb_podPosSB()!= 315)
                {
                  dh.setCmd_speedPodRotSB(maxSpeed);  
                }
                else
                {
                    dh.setCmd_speedPodRotSB(minSpeed);
                }
                
                if (dh.getFb_podPosPS()== 315 && dh.getFb_podPosSB()== 315)
                {
                dh.setCmd_speedPS(maxSpeed);
                dh.setCmd_speedSB(maxSpeed);
                }
                break;
            case GORIGHT:
                if (dh.getFb_podPosPS()!= 45)
                {
                    dh.setCmd_speedPodRotPS(maxSpeed);
                }
                else
                {
                   dh.setCmd_speedPodRotPS(minSpeed);                  
                }
                
                if(dh.getFb_podPosSB() != 45)
                {
                  dh.setCmd_speedPodRotSB(maxSpeed);  
                }
                else
                {
                 dh.setCmd_speedPodRotSB(minSpeed);   
                }
                
                if (dh.getFb_podPosPS()== 45 && dh.getFb_podPosSB()== 45)
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
                
                if(dh.getFb_podPosSB() != 345)
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
                if(dh.getFb_podPosSB() != 15)
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
                if(dh.getFb_podPosSB() != 15)
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
                
                if(dh.getFb_podPosSB() != 345)
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
     * @param speedSB 
     */   
    public void setSBSpeed(int speedSB)
    {
        dh.setCmd_speedSB(speedSB);
    }
    
    /**
     * Set starboard pod motorspeed
     * @param speedPodSB 
     */
    public void setSBpodSpeed(int speedPodSB)
    {
        dh.setCmd_speedPodRotSB(speedPodSB);
    }
    
    
    /**
     * Set portside motorspeed
     * @param speedPS
     */
    public void setPSSpeed(int speedPS)
    {
        dh.setCmd_speedPS(speedPS);
    }
    
    /**
     * Set portside pod motorspeed
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
