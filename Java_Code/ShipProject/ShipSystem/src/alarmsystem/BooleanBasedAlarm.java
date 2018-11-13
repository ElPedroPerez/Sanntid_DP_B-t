/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alarmsystem;

/**
 *
 * @author rocio
 */
public class BooleanBasedAlarm implements Runnable
{

    long currentTime = 0;
    long lastTime = 0;

    int inputSignal;
    int setPoint;
    boolean alarm;
    boolean HAlarm;
    boolean ack;
    boolean inhibit;

    public BooleanBasedAlarm(AlarmList al, int inputSignal, int setPoint, boolean alarm,
            boolean HAlarm, boolean ack, boolean inhibit)
    {
        this.inputSignal = inputSignal;
        this.setPoint = setPoint;
        this.alarm = alarm;
        this.HAlarm = HAlarm;
        this.ack = ack;
        this.inhibit = inhibit;
        currentTime = System.nanoTime();
    }

    @Override
    public void run()
    {
        while (true)
        {
            while (!inhibit)
            {
                if (HAlarm && inputSignal > setPoint)
                {
                    // High Alarm
                    alarm = true;
                    while (alarm)
                    {
                        if (ack)
                        {
                            alarm = false;
                        }
                    }
                }
                if (!HAlarm && inputSignal < setPoint)
                {
                    // Low Alarm
                    alarm = true;
                    while (alarm)
                    {
                        if (ack)
                        {
                            alarm = false;
                        }
                    }
                }
            }
        }
    }
}
