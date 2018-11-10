/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package remotecontroller;

import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

/**
 * Observable object for video frames
 * 
 * @author Magnus Gribbestad
 */
public class ReceiveVideoObservable extends Observable {
    private BufferedImage buff;
    
    /**
     * Overrided method for adding observer.
     * 
     * @param o Observer
     */
    @Override
    public synchronized void addObserver(Observer o) {
        super.addObserver(o); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * Sets the bufferedImage field and notifies observers.
     * 
     * @param buff BufferdImage
     */
    public void setFrame(BufferedImage buff) {
            this.buff = buff;
            
            setChanged();
            notifyObservers();
    }

    /**
     * Return the last updated BufferdImage
     * 
     * @return BufferedImage 
     */
    public BufferedImage getBuff(){
        return this.buff;
    }


} 

