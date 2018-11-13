/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alarmsystem;

import shipsystem.DataHandler;

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
    int time;
    boolean alarm;
    boolean ack;
    boolean inhibit;
    private String alarmName;
    AlarmList alarmList;
    DataHandler dh;

    public TimeBasedAlarm(DataHandler dh, AlarmList alarmList, String input, String fb, String alarmName,
            int time, boolean ack, boolean inhibit)
    {
        this.dh = dh;
        this.alarmList = alarmList;
        this.input = input;
        this.fb = fb;
        this.time = time;
        this.ack = ack;
        this.inhibit = inhibit;
        this.alarmName = alarmName;

        currentTime = System.nanoTime();
    }

    @Override
    public void run()
    {
        while (true)
        {
            while (!inhibit)
            {
                //if (alarmList.alarmDataList.get(input) > 0)
                if (1 > 0)
                {
                    lastTime = System.nanoTime();
                    double elapsedTime = 0;
                    while (alarmList.alarmDataList.get(fb) == 0 && !ack)
                    {
                        elapsedTime = (System.nanoTime() - lastTime) / 1000000000;
                        if (elapsedTime >= time)
                        {
                            try
                            {
                                if (alarmName != null)
                                {
                                    alarm = true;
                                    dh.handleDataFromAlarmList(alarmName, alarm);
                                    //dh.handleDataFromAlarmList(alarmName, true);
                                }

                            } catch (Exception e)
                            {
                                System.out.println("Exception in TimeBasedAlarm when setting alarmstate in DataHandler: " + e);
                            }

                            while (alarm)
                            {
                                if (ack)
                                {
                                    alarm = false;
                                    dh.handleDataFromAlarmList(alarmName, false);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
