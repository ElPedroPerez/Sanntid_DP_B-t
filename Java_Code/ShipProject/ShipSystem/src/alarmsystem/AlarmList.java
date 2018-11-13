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

    public boolean visionVarianceTreshholdAlarm = false;

    public boolean inhibit_portSpeedFeedbackErrorAlarm = false;
    public boolean inhibit_stbSpeedFeedbackErrorAlarm = false;
    public boolean inhibit_portRotationFeedbackErrorAlarm = false;
    public boolean inhibit_stbRotationFeedbackErrorAlarm = false;
    public boolean inhibit_visionVarianceTreshholdAlarm = false;

    TimeBasedAlarm stbSpeedFeedbackError;

    public boolean ack;
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
        
//        while (true)
//        {
//            stbSpeedFeedbackError.updateInputs(dh.getCmd_speedPodRotPS(), dh.getFb_speedSB(), inhibit_stbSpeedFeedbackErrorAlarm);
//        }
    }

    private void initiateAlarmThreads()
    {
        //stbSpeedFeedbackErrorAlarm
        TimeBasedAlarm stbSpeedFeedbackError = new TimeBasedAlarm(this, "Cmd_podSpeedPS", "Fb_speedPodRotPS", stbSpeedFeedbackErrorAlarm, 5, ack, inhibit_stbSpeedFeedbackErrorAlarm);
        Thread stbSpeedFeedbackError_Thread = new Thread(stbSpeedFeedbackError);

//        //portSpeedFeedbackErrorAlarm
//        Thread psSpeedFeedbackError = new Thread(new TimeBasedAlarm("psSpeedFeedbackError",this, dh.getCmd_speedPodRotPS(), dh.getFb_speedPS(), portSpeedFeedbackErrorAlarm, 5, ack, inhibit_portSpeedFeedbackErrorAlarm));
//
//        //Steering Gear port side        
//        Thread portRotationFeedbackError = new Thread(new TimeBasedAlarm("portRotationFeedbackError",this, dh.getCmd_podPosPS(), dh.getFb_podPosPS(), portRotationFeedbackErrorAlarm, 5, ack, inhibit_portRotationFeedbackErrorAlarm));
//
//        //Steering Gear stb side
//        Thread stbRotationFeedbackError = new Thread(new TimeBasedAlarm("stbRotationFeedbackError",this, dh.getCmd_podPosSB(), dh.getFb_podPosSB(), stbRotationFeedbackErrorAlarm, 0, ack, inhibit_stbRotationFeedbackErrorAlarm));
//
//        //VisionVarianceTreshholdAlarm
//        Thread visionVarianceTreshhold = new Thread(new BooleanBasedAlarm(this, (int) Math.round(dh.getPosAccuracy()), 7, visionVarianceTreshholdAlarm, true, ack, inhibit_visionVarianceTreshholdAlarm));

        //Start threads        
//        stbRotationFeedbackError.start();
        stbSpeedFeedbackError_Thread.start();
//        portRotationFeedbackError.start();
//        psSpeedFeedbackError.start();
//        visionVarianceTreshhold.start();

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
            
            alarmDataList.put("Fb_speedPodRotPS", dh.getFb_speedPodRotPS());

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
