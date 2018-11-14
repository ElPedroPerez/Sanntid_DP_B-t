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
 * Handles data from GUI
 *
 * @author Bjørnar
 */
public class Datahandler
{

    private String dataFromGui;
    private int angle;

    public Datahandler()
    {

        //this.dataFromGui = dataFromGui;
    }
//
//    public void updateGUI()
//    {
//        setChanged();
//        notifyObservers();
//    }
//
//    public void setAngle(int angle)
//    {
//        this.angle = angle;
//        setChanged();
//        notifyObservers();
//    }

    public int getAngle()
    {
        return this.angle;
    }

    /**
     * Gets data from GUI
     *
     * @return dataFromGui
     */
    public String getDataFromGui()
    {
        return this.dataFromGui;
    }

    /**
     * Creates and sends the data String over UDP
     */
    public void sendData()
    {
        new UDPsender().send(GUISystem.IPADDRESS, dataFromGui, GUISystem.SENDPORT);
    }

}
