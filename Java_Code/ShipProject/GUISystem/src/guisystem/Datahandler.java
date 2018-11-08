/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package remotecontroller;

import java.math.BigInteger;
import java.util.Arrays;

/**
 *
 * @author mgrib
 */

/****************************************************************************
Sending protocol:
Byte 0-x: [bit 0, bit 1, bit 2, bit 3, bit 4, bit 5, bit 6, bit 7]

Byte 0:[stopp, fwd, rev, left, right, bit 5, bit 6, bit 7]
Byte 1:[left motor speed]
Byte 2:[right motor speed]
Byte 3:[left servo,right servo, auto/manual, start program,...,request feedback]
Byte 4:[sensitivity]
Byte 5:[reservert]
*****************************************************************************
 */
public class Datahandler {

    private byte[] dataFromGui;


    public Datahandler() {

        this.dataFromGui = new byte[11];
    }

    //*****************************************************************
    //********** PRIVATE METHODS AREA**********************************
    /**
     * Sets a bit in byte. Takes a bit number and a byte in, sets the
     * bit in the byte. Return the byte.
     * 
     * 
     * @param b Byte that should be changed
     * @param bit Bit number in byte to be set
     * @return b The edited Byte
     */
    private byte setBit(byte b, int bit) {
        return b |= 1 << bit;
    }
    
    /**
     * Releases a bit in byte. Takes a bit number and a byte in, releases the
     * bit in the byte. Return the byte.
     * 
     * 
     * @param b Byte that should be changed
     * @param bit Bit number in byte to be released
     * @return b The edited Byte
     */
    private byte releaseBit(byte b, int bit) {
        return b &= ~(1 << bit);
    }

    //****************************************************************
    //************** FROM GUI METHODS*********************************
    /**
     * Returns the byte array consisting of all the parameters coming from 
     * the GUI. (FWD button state, Syste state, sensitivit and so on). Sending
     * protocol is defined at the top of the class.
     * 
     * @return Byte array consisting of all parameters in protocol to be sent.
     */
    public byte[] getDataFromGui() {
        return this.dataFromGui;
    }

    /**
     * Sets the FWD bit in the send byte Array. 
     */
    public void goFwd() {
        dataFromGui[0] = this.setBit(dataFromGui[0], 1);
        this.sendData();
    }

    /**
     * Releases the FWD bit in the send byte Array. 
     */
    public void releaseGoFwd() {
        dataFromGui[0] = this.releaseBit(dataFromGui[0], 1);
        this.sendData();
    }

    /**
     * Sets the REV bit in the send byte Array. 
     */
    public void goRew() {
        dataFromGui[0] = this.setBit(dataFromGui[0], 2);
        this.sendData();
    }

    /**
     * Releases the REV bit in the send byte Array. 
     */
    public void releaseGoRew() {
        dataFromGui[0] = this.releaseBit(dataFromGui[0], 2);
        this.sendData();
    }

    /**
     * Sets the LEFT bit in the send byte Array. 
     */
    public void goLeft() {
        dataFromGui[0] = this.setBit(dataFromGui[0], 3);
        this.sendData();
    }

    /**
     * Releases the LEFT bit in the send byte Array. 
     */
    public void releaseGoLeft() {
        dataFromGui[0] = this.releaseBit(dataFromGui[0], 3);
        this.sendData();
    }

    /**
     * Sets the RIGHT bit in the send byte Array. 
     */
    public void goRight() {
        dataFromGui[0] = this.setBit(dataFromGui[0], 4);
        this.sendData();
    }

    /**
     * Releases the RIGHT bit in the send byte Array. 
     */
    public void releaseGoRight() {
        dataFromGui[0] = this.releaseBit(dataFromGui[0], 4);
        this.sendData();
    }

    /**
     * Sets the Left servo bit in the send byte Array. 
     */
    public void setLeftServo() {
        dataFromGui[3] = this.setBit(dataFromGui[3], 0);
        this.sendData();
    }
    
    /**
     * Releases the Left servo bit in the send byte Array. 
     */
    public void resetLeftServo() {
        dataFromGui[3] = this.releaseBit(dataFromGui[3], 0);
        this.sendData();
    }
    
    /**
     * Sets the Right servo bit in the send byte Array. 
     */
    public void setRightServo() {
        dataFromGui[3] = this.setBit(dataFromGui[3], 1);
        this.sendData();
    }
    
    /**
     * Releases the Right servo bit in the send byte Array. 
     */
    public void resetRightServo() {
        dataFromGui[3] = this.releaseBit(dataFromGui[3], 1);
        this.sendData();
    }
    
    /**
     * Sets the AUV manual bit in the send byte Array. 
     */
    public void AUVmanualMode() {
        dataFromGui[3] = this.releaseBit(dataFromGui[3], 2);
        this.sendData();
    }
    
    /**
     * Sets the AUV auto bit in the send byte Array. 
     */
    public void AUVautoMode() {
        dataFromGui[3] = this.setBit(dataFromGui[3], 2);
        this.sendData();
    }

    /**
     * Sets the Start system bit in the send byte Array. 
     */
    public void enableAUV() {
        dataFromGui[3] = this.setBit(dataFromGui[3], 3);
        this.sendData();
    }
    
    /**
     * Releases the Start system bit in the send byte Array. 
     */
    public void disableAUV() {
        dataFromGui[3] = this.releaseBit(dataFromGui[3], 3);
        this.sendData();
    }
    
    /**
     * Sets the Sensitivity byte in the send byte Array. 
     */
    public void setSensitivity(byte sensetivity) {
        dataFromGui[4] = sensetivity;
        this.sendData();
    }
    
    /**
     * Increments the request code. Request vode is used to request feedback
     * from the Arduino.
     */
    public void incrementRequestCode() {
        dataFromGui[5]++;
        this.sendData();
    }

    /**
     * Creates and sends the data byte array over UDP.
     */
    public void sendData() {
        new UDPsender().send(Main.IPADDRESS, dataFromGui, Main.SENDPORT);
    }

    void setPidParams(int[] pidParams) {
        for (int i=0; i<5; i++){
           dataFromGui[i+6] = (byte)pidParams[i];
        }
        this.sendData();
    }

}
