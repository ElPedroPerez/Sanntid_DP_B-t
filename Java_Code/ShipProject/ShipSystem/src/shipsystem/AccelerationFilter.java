/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shipsystem;

import static java.lang.Math.toIntExact;

/**
 * Acceleration filter is responsible for accelerate and decelerate motor speeds
 *
 * @author rocio
 */
public class AccelerationFilter implements Runnable
{

    private final DataHandler dh;

    //this is our target velocity while decelerating
    double initialVelocity = 0.0;

    //this is our target velocity while accelerating
    double finalVelocity = 100.0;

    //this is our current velocity
    double currentVelocity = 0.0;

    //this is the velocity we add each second while accelerating
    double accelerationRate = 3.33;

    //this is the velocity we subtract each second while decelerating
    double decelerationRate = 3.33;

    int lastSoftSpeed = 0;

    // Internal timer
    long currentTime = 0;
    long lastTime = 0;
    long elapsedTimer = 0;

    /**
     * 
     * @param dh
     */
    public AccelerationFilter(DataHandler dh)
    {
        this.dh = dh;

    }

    @Override
    public void run()
    {
        while (true)
        {
            accCalculation();

        }
    }

    /**
     * Adds a filter for motor acceleration
     */
    public void accCalculation()
    {

        //Acceleration 
        lastTime = System.nanoTime();
        elapsedTimer = (System.nanoTime() - lastTime) / 100000;
        finalVelocity = dh.getIc_speed();
        if (currentVelocity < finalVelocity && elapsedTimer >= 100)
        {
            currentVelocity = currentVelocity + accelerationRate;
        }

        //Deceleration 
        if (currentVelocity > finalVelocity && elapsedTimer >= 100)
        {
            currentVelocity = currentVelocity - accelerationRate;
        }

        if (elapsedTimer > 100)
        {
            elapsedTimer = 0;
        }

        int softSpeed = (int) Math.round(currentVelocity);
        if (softSpeed != lastSoftSpeed)
        {
            dh.setSoftSpeedPod(softSpeed);
        }
        lastSoftSpeed = softSpeed;
    }
}
