/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alarmsystem;
import alarmsystem.AlarmList;


/**
 *
 * @author rocio
 */
public class TimeBasedAlarm implements Runnable
{

    
    long currentTime = 0;
    long lastTime = 0;

    String input;
    String fb;
    boolean alarm;
    int time;
    boolean ack;
    boolean inhibit;
    AlarmList al;
    

    public TimeBasedAlarm(AlarmList al, String input, String fb, boolean alarm,
            int time, boolean ack, boolean inhibit)
    {
        this.al = al;
        this.input = input;
        this.fb = fb;
        this.alarm = alarm;
        this.time = time;
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
                
                if (al.alarmDataList.get(input) > 0)
                {
                    lastTime = System.nanoTime();
                    double elapsedTime = 0;
                    while (al.alarmDataList.get(fb) == 0 && !ack)
                    {
                        elapsedTime = (System.nanoTime() - lastTime) / 1000000000;
                        if (elapsedTime >= time)
                        {
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
    }
}