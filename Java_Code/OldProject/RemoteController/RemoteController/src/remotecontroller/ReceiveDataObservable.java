/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package remotecontroller;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;


/**
 * Observable object for object data (distance and error angels)
 * 
 * @author Magnus Gribbestad
 */
public class ReceiveDataObservable extends Observable{

    private int distanceSensor;
    private float errorAngleX;
    private float errorAngleY;
    
    public ReceiveDataObservable() {
    }
    
    /**
     * Overrided function for adding observers to a observable object.
     * 
     * @param o Observer
     */
    @Override
    public synchronized void addObserver(Observer o) {
        super.addObserver(o); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Sets the receiving data to the fields, and notifies observers.
     * 
     * @param data 
     */
    public void setData(byte[] data) {
        // check if the array is of the same length and the requestcode has changed
        if (data.length == 6) {
            
            distanceSensor = data[4];
            errorAngleX = (new BigInteger(Arrays.copyOfRange(data, 0, 2)).intValue());
            errorAngleY = (new BigInteger(Arrays.copyOfRange(data, 2, 4)).intValue());
            
            setChanged();
            notifyObservers();
        }
    }
    
    /**
     * Returns the distance value
     * 
     * @return distanceSensor value
     */
    public int getDistance(){
        return this.distanceSensor;
    }
    
    /**
     * Returns the error angle in the X-direction
     * 
     * @return errorAngleX
     */
    public float getErrorAngleX(){
        return this.errorAngleX;
    }
    
    /**
     * Returns the error angle in the Y-direction
     * 
     * @return errorAngleY
     */
    public float getErrorAngleY(){
        return this.errorAngleY;
    }
    
    
    public boolean shouldChildOfThisRun(){
        return true;
    }
    
}
