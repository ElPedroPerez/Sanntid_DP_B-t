/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package remotecontroller;

import java.util.TimerTask;


/**
 * 
 * @author mgrib
 * 
 * The GUIController class is used to bind the GUI together with the
 * datahandler. The idea is that this class will make it easier to make changes
 * in both GUI and the datahandler.
 */
public class GUIController extends TimerTask {
    
    private Datahandler datahandler;
    
    /**
     * Constructor of the GUIController. Sets up the datahandler, which is used
     * to handle the data and parameters from the GUI.
     * 
     * @param datahandler Sets the datahandler
     */
    public GUIController(){   

        //this.datahandler = datahandler; 
        this.datahandler = new Datahandler();
    }
    
    
    @Override
    public void run(){
        //System.out.println("Request data");
        this.datahandler.incrementRequestCode();
    }
      
    /**
     * Sets or releases the FWD bit in the Byte array in datahandler.
     * Run forward or stop running fwd. Used for manual driving.
     * 
     * @param value boolean to decide to set (True) or release (False)
     */
    public void setFwd(boolean value){ 
       if(value){
        //System.out.println("Set FWD");
        this.datahandler.goFwd();
       }
       else{
        //System.out.println("Not FWD");
        this.datahandler.releaseGoFwd();
       }
    }
    
    /**
     * Sets or releases the Left bit in the Byte array in datahandler.
     * Turn left or stop turn left. Used for manual driving.
     * 
     * @param value boolean to decide to set (True) or release (False)
     */
    public void setLeft(boolean value){  
        if(value){
           //System.out.println("Set LEFT");
           this.datahandler.goLeft();
       }
       else{
           //System.out.println("Not LEFT");
           this.datahandler.releaseGoLeft();
       }
    }
    
    /**
     * Sets or releases the REV bit in the Byte array in datahandler.
     * Run reverse or stop running reverse. Used for manual driving.
     * 
     * @param value boolean to decide to set (True) or release (False)
     */
    public void setRev(boolean value){  
        if(value){
           //System.out.println("Set Rev");
           this.datahandler.goRew();
       }
       else{
           //System.out.println("Not Rev");
           this.datahandler.releaseGoRew();
       }
    }
    
    /**
     * Sets or releases the Right bit in the Byte array in datahandler.
     * Turn right or stop turning right. Used for manual driving.
     * 
     * @param value boolean to decide to set (True) or release (False)
     */
    public void setRight(boolean value){ 
        if(value){
           //System.out.println("Set RIGHT");
           this.datahandler.goRight();
       }
       else{
           //System.out.println("Not RIGHT");
           this.datahandler.releaseGoRight();
       }
    }
    
    /**
     * Sets or releases the LEFT SERVO bit in the Byte array in datahandler.
     * Used to activate the left servo. Used for manual driving.
     * 
     * @param value boolean to decide to set (True) or release (False)
     */
    public void setLeftServo(boolean value){ 
        if(value){
            //System.out.println("Set LEFT SERVO");
            this.datahandler.setLeftServo();
        }
        else{
            this.datahandler.resetLeftServo();
        }

    }
    
    /**
     * Sets or releases the RIGHT SERVO bit in the Byte array in datahandler.
     * Used to activate the right servo. Used for manual driving.
     * 
     * @param value boolean to decide to set (True) or release (False)
     */
    public void setRightServo(boolean value){  
        if(value){
            //System.out.println("Set RIGHT SERVO");
            this.datahandler.setRightServo();
        }
        else{
            this.datahandler.resetRightServo();
        }
    }
    
    /**
     * Sets or releases the Start system bit in the Byte array in datahandler.
     * 
     * @param value boolean to decide to set (True) or release (False)
     */
    public void setStart(boolean value){  
        if (value){
            //System.out.println("START SYSTEM");
            this.datahandler.enableAUV();
        }
        else if(!value){
            //System.out.println("STOP SYSTEM");
            this.datahandler.disableAUV();
        }
    }
    
    /**
     * Toggles between Auto mode and Manual mode.
     * 
     * @param value boolean to decide to set (True) or release (False)
     */
    public void setAuto(boolean value){
        if (value){
            //System.out.println("AUTO MODE ON");
            this.datahandler.AUVautoMode();
        }
        else if(!value){
            //System.out.println("MANUAL MODE ON");
            this.datahandler.AUVmanualMode();
        }
      
    }
    
    /**
     * Sets the sensitivity byte in the datahandler.
     * 
     * @param sens boolean to decide to set (True) or release (False)
     */
    public void setSens(int sens){
        //System.out.println("New sens = " + sens);
        this.datahandler.setSensitivity((byte) sens);
    }

    /**
     * Sets the PID parameters in the datahandler.
     * 
     * @param pidParams Int[] PID parameters [P, I, D, feed fwd, ramp rate]
     */
    void setPidParams(int[] pidParams) {
        this.datahandler.setPidParams(pidParams);
    }
  
}

