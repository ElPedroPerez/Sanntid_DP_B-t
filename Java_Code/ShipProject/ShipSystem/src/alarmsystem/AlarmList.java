/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alarmsystem;

import java.util.HashMap;
import java.util.Map;
import shipsystem.DataHandler;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author rocio
 */
public class AlarmList implements Runnable
{

    private final DataHandler dh;

    public boolean portSpeedFeedbackErrorAlarm = false;
    public boolean stbSpeedFeedbackErrorAlarm = false;
    public boolean portRotationFeedbackErrorAlarm = false;
    public boolean stbRotationFeedbackErrorAlarm = false;

    public boolean inhibit_portSpeedFeedbackErrorAlarm = false;
    public boolean inhibit_stbSpeedFeedbackErrorAlarm = false;
    public boolean inhibit_portRotationFeedbackErrorAlarm = false;
    public boolean inhibit_stbRotationFeedbackErrorAlarm = false;

    private boolean ack;
    Map<String, Integer> alarmDataList = new ConcurrentHashMap<>();

    public AlarmList(DataHandler dh)
    {
        this.dh = dh;

    }

    @Override
    public void run()
    {
        initiateAlarmThreads();
        updateAlarmData();
    }

    private void initiateAlarmThreads()
    {

        //PortSpeedFeedbackError
        Thread portSpeedFeedbackError = new Thread(new TimeBasedAlarm(this, "CMDpodPosPS", "FBpodPosPS", portSpeedFeedbackErrorAlarm, 5, ack, inhibit_portSpeedFeedbackErrorAlarm));

        //StbSpeedFeedbackError
        Thread stbSpeedFeedbackError = new Thread(new TimeBasedAlarm(this, "Cmd_speedSB", "Fb_speedSB", stbSpeedFeedbackErrorAlarm, 5, ack, inhibit_stbSpeedFeedbackErrorAlarm));

        //PortRotationFeedbackError
        Thread portRotationFeedbackError = new Thread(new TimeBasedAlarm(this, "Cmd_podPosPS", "Fb_podPosPS", portRotationFeedbackErrorAlarm, 5, ack, inhibit_portRotationFeedbackErrorAlarm));

        //StbRotationFeedbackError
        Thread stbRotationFeedbackError = new Thread(new TimeBasedAlarm(this, "Cmd_podPosSB", "Fb_podPosSB", stbRotationFeedbackErrorAlarm, 5, ack, inhibit_stbRotationFeedbackErrorAlarm));

        //Start threads
        portSpeedFeedbackError.start();
        stbSpeedFeedbackError.start();
        portRotationFeedbackError.start();
        stbRotationFeedbackError.start();
    }

    private void updateAlarmData()
    {
        while (true)
        {
            alarmDataList.put("Cmd_podPosPS", dh.getCmd_podPosPS());
            alarmDataList.put("Fb_podPosPS", dh.getFb_podPosPS());

            alarmDataList.put("Cmd_podPosSB", dh.getCmd_podPosSB());
            alarmDataList.put("Fb_podPosSB", dh.getFb_podPosSB());

            alarmDataList.put("Cmd_podSpeedPS", dh.getCmd_speedPS());
            alarmDataList.put("Fb_podSpeedPS", dh.getFb_speedPS());

            alarmDataList.put("Cmd_podSpeedPS", dh.getCmd_speedSB());
            alarmDataList.put("Fb_podSpeedPS", dh.getFb_speedSB());

            try
            {
                Thread.sleep(250);
            } catch (Exception e)
            {
                System.out.println("AlarmList.updateAlarmData suffers from insomnia..");
            }

        }
    }

}
