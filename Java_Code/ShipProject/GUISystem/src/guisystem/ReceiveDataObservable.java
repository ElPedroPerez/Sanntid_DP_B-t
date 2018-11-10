/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guisystem;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

/**
 * Observable object for object data
 * 
 * @author Bjørnar
 */
public class ReceiveDataObservable extends Observable        
{
    
    private int distanceSensor;
    
    public ReceiveDataObservable()
    { 
        
    }
    
    /**
     * Overrided function for adding observers to a observable object.
     * 
     * @param o 
     */
       @Override 
        public synchronized void addObserver(Observer o)
        {
            super.addObserver(o);
        }
        
        /**
         * Sets the recieving data to the fields, and notifies observers.
         * 
         * @param data
         */ 
        public void setData(byte[] data)
        {
            // check if the array is of the same length and the requestcode has changed
            if (data.length == 6)
            {
                distanceSensor = data[4];
                
                setChanged();
                notifyObservers();
            }
        }
        
        
        /**
         * Returns the distance value
         * 
         * @return distanceSensor value 
         */
        public int getDistance()
        {
            return this.distanceSensor;
        }
        
        public boolean shouldChildOfThisRun()
        {
            return true;
        }
        
}
