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
import java.util.HashMap;

/**
 * Observable object for object data
 * 
 * @author Bjørnar
 */
public class ReceiveDataObservable extends Observable        
{
    
    private int distanceSensor;
    private final HashMap<String, String> receivedDataList;
    private final String start_char = "<";
    private final String end_char = ">";
    private final String sep_char = ":";
    
    public ReceiveDataObservable()
    { 
        this.receivedDataList = new HashMap<>();
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
        public void setData(String receivedData)
        {
                receivedData = receivedData.substring(receivedData.indexOf(start_char) + 1);
                receivedData = receivedData.substring(0, receivedData.indexOf(end_char));
                receivedData = receivedData.replace("?", "");
                String[] data = receivedData.split(sep_char);

                for (int i = 0; i < data.length; i = i + 2)
                {
                    receivedDataList.put(data[i], data[i + 1]);
                    //System.out.println("Key: " + data[i] + "     Value:" + data[i + 1]);
                    //receivedData = true;

                }
                setChanged();
                notifyObservers();
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
