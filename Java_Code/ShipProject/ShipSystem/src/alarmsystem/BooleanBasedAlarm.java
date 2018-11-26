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
public class BooleanBasedAlarm implements Runnable
{
    long currentTime = 0;
    long lastTime = 0;

    String input;
    int setPoint;
    Boolean alarm;
    boolean HAlarm;
    boolean ack;
    boolean inhibit;
    String alarmName;
    AlarmList alarmList;
    DataHandler dh;

    /**
     * 
     * @param dh
     * @param alarmList
     * @param input
     * @param setPoint
     * @param alarmName
     * @param HAlarm
     * @param ack
     * @param inhibit
     */
    public BooleanBasedAlarm(DataHandler dh, AlarmList alarmList, String input, int setPoint, String alarmName,
            boolean HAlarm, boolean ack, boolean inhibit)
    {
        this.alarmList = alarmList;
        this.input = input;
        this.setPoint = setPoint;
        
        this.alarmName = alarmName;
        this.HAlarm = HAlarm;
        this.ack = ack;
        this.inhibit = inhibit;
        this.dh = dh;
        currentTime = System.nanoTime();
    }

    @Override
    public void run()
    {
        while (true)
        {
            while (!inhibit)
            {
                if (HAlarm && alarmList.alarmDataList.get(input) > setPoint)
                {
                    // High Alarm
                    alarm = true;
                    dh.handleDataFromAlarmList(alarmName, alarm);
                    while (alarm)
                    {
                        if (ack)
                        {
                            alarm = false;
                            dh.handleDataFromAlarmList(alarmName, alarm);
                        }
                    }
                }
                if (!HAlarm && alarmList.alarmDataList.get(input) < setPoint)
                {
                    // Low Alarm
                    alarm = true;
                    dh.handleDataFromAlarmList(alarmName, alarm);
                    while (alarm)
                    {
                        if (ack)
                        {
                            alarm = false;
                            dh.handleDataFromAlarmList(alarmName, alarm);
                        }
                    }
                }
            }
        }
    }
}
