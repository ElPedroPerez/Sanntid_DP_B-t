/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alarmsystem;

import java.util.Map;
import shipsystem.DataHandler;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import shipsystem.UDPsender;

/**
 * List of all alarms
 * @author rocio
 */
public class AlarmList implements Runnable
{

    private final DataHandler dh;

    /**
     * Alarm variables
     */
    public boolean sbSpeedFbAlarm = false;
    public boolean psSpeedFbAlarm = false;
    public boolean sbPodPosFbAlarm = false;
    public boolean psPodPosFbAlarm = false;
    public boolean visionDeviationAlarm = false;
    public boolean imuRollAlarm = false;
    //public boolean pingAlarm = false;
    public boolean inhibit_sbSpeedFbAlarm = false;
    public boolean inhibit_psSpeedFbAlarm = false;
    public boolean inhibit_sbPodPosFbAlarm = false;
    public boolean inhibit_psPodPosFbAlarm = false;
    public boolean inhibit_visionDeviationAlarm = false;
    public boolean inhibit_imuRollAlarm = false;
    //public boolean inhibit_pingAlarm = false;

    /**
     * Timebased alarms
     */
    TimeBasedAlarm sbSpeedFbAlarmError;
    TimeBasedAlarm psSpeedFbAlarmError;
    TimeBasedAlarm sbPodPosFbAlarmError;
    TimeBasedAlarm psPodPosFbAlarmError;
    
    /**
     * Boolean based alarms
     */
    BooleanBasedAlarm visionDeviationAlarmError;
    BooleanBasedAlarm imuRollAlarmError;
    //BooleanBasedAlarm pingAlarmError;
    
    public boolean ack;
    Map<String, Integer> alarmDataList = new ConcurrentHashMap<>();

    /**
     * Creates an instance of datahandler
     * @param dh
     */
    public AlarmList(DataHandler dh)
    {
        this.dh = dh;

    }

    @Override
    public void run()
    {
        initiateAlarmThreads();
        populateAlarmList();
        UDPsender udpsender = new UDPsender();

        while (true)
        {
            try
            {
                String sendData = null;
                for (Map.Entry e : dh.listOfAlarms.entrySet())
                {
                    String key = (String) e.getKey();
                    String value = (String) String.valueOf(e.getValue());
                    sendData = ("<" + key + ":" + value + ">");
                }

                updateAlarmInputData();

                if (sendData != null)
                {
                    udpsender.send(dh.ipAddressGUI, sendData, dh.sendPort);
                }
                Thread.sleep(250);
            }
            catch (InterruptedException ex)
            {
                Logger.getLogger(AlarmList.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    /**
     * 
     */
    private void populateAlarmList()
    {
        dh.listOfAlarms.put("sbSpeedFbAlarm", false);
        dh.listOfAlarms.put("psSpeedFbAlarm", false);
        dh.listOfAlarms.put("sbPodPosFbAlarm", false);
        dh.listOfAlarms.put("psPodPosFbAlarm", false);
        dh.listOfAlarms.put("visionDeviationAlarm", false);
        dh.listOfAlarms.put("imuRollAlarm", false);
        //dh.listOfAlarms.put("pingAlarm", false);
    }

    /**
     * Initiates alarm threads
     */
    private void initiateAlarmThreads()
    {
        //Time Based Alarms
        sbSpeedFbAlarmError = new TimeBasedAlarm(
                dh, this, "cmd_speedSB", "fb_speedSB", "sbSpeedFbAlarm",
                5, ack, inhibit_sbSpeedFbAlarm);
        psSpeedFbAlarmError = new TimeBasedAlarm(
                dh, this, "cmd_speedPS", "fb_speedPS", "psSpeedFbAlarm",
                5, ack, inhibit_psSpeedFbAlarm);
        sbPodPosFbAlarmError = new TimeBasedAlarm(
                dh, this, "cmd_podPosSB", "fb_podPosSB", "sbPodPosFbAlarm",
                7, ack, inhibit_sbPodPosFbAlarm);
        psPodPosFbAlarmError = new TimeBasedAlarm(
                dh, this, "cmd_podPosPS", "fb_podPosPS", "psPodPosFbAlarm",
                7, ack, inhibit_psPodPosFbAlarm);
        //Boolean Based Alarms
        visionDeviationAlarmError = new BooleanBasedAlarm(
                dh, this, "posAccuracy", 10, "visionDeviationAlarm",
                true, ack, inhibit_visionDeviationAlarm);
        imuRollAlarmError = new BooleanBasedAlarm(
                dh, this, "Roll", 10, "imuRollAlarm",
                true, ack, inhibit_imuRollAlarm);
        //pingAlarmError = new BooleanBasedAlarm(dh, this, "guiPing", 50, "pingAlarm", true, ack, inhibit_pingAlarm);

        //Create threads
        Thread sbSpeedFbAlarm_Thread = new Thread(sbSpeedFbAlarmError);
        Thread psSpeedFbAlarm_Thread = new Thread(psSpeedFbAlarmError);
        Thread sbPodPosFbAlarm_Thread = new Thread(sbPodPosFbAlarmError);
        Thread psPodPosFbAlarm_Thread = new Thread(psPodPosFbAlarmError);
        Thread visionDeviationAlarm_Thread = new Thread(visionDeviationAlarmError);
        Thread imuRollAlarm_Thread = new Thread(imuRollAlarmError);
        //Thread pingAlarm_Thread = new Thread(pingAlarmError);

        //Name threads
        sbSpeedFbAlarm_Thread.setName("sbSpeedFbAlarm");
        psSpeedFbAlarm_Thread.setName("psSpeedFbAlarm");
        sbPodPosFbAlarm_Thread.setName("sbPodPosFbAlarm");
        psPodPosFbAlarm_Thread.setName("psPodPosFbAlarm");
        visionDeviationAlarm_Thread.setName("visionDeviationAlarm");
        imuRollAlarm_Thread.setName("imuRollAlarm");
        //pingAlarm_Thread.setName("pingAlarm");

        //Start threads        
        sbSpeedFbAlarm_Thread.start();
        psSpeedFbAlarm_Thread.start();
        sbPodPosFbAlarm_Thread.start();
        psPodPosFbAlarm_Thread.start();
        visionDeviationAlarm_Thread.start();
        imuRollAlarm_Thread.start();
        //pingAlarm_Thread.start();

    }

    /**
     * Updates alarm data
     */
    private void updateAlarmInputData()
    {
        while (true)
        {
            alarmDataList.put("cmd_speedSB", dh.cmd_speedSB);
            alarmDataList.put("fb_speedSB", dh.fb_speedSB);

            alarmDataList.put("cmd_speedPS", dh.cmd_speedPS);
            alarmDataList.put("fb_speedPS", dh.fb_speedPS);

            alarmDataList.put("cmd_podPosSB", dh.cmd_podPosSB);
            alarmDataList.put("fb_podPosSB", dh.fb_podPosSB);

            alarmDataList.put("cmd_podPosPS", dh.cmd_podPosPS);
            alarmDataList.put("fb_podPosPS", dh.fb_podPosPS);

            alarmDataList.put("posAccuracy", (int) Math.round(dh.posAccuracy));
            alarmDataList.put("Roll", dh.Roll);
            //alarmDataList.put("ping", dh.ping);
        }
    }
}
