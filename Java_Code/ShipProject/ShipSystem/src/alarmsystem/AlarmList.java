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
public class AlarmList
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
   

    public AlarmList(DataHandler dh)
    {
        this.dh = dh;
        initiateAlarmThreads();
    }

    private void initiateAlarmThreads()
    {
        //PortSpeedFeedbackError
        Thread portSpeedFeedbackError = new Thread(new TimeBasedAlarm(dh.getCmd_speedPS(), dh.getFb_speedPS(), portSpeedFeedbackErrorAlarm, 5, ack, inhibit_portSpeedFeedbackErrorAlarm));

        //StbSpeedFeedbackError
        Thread stbSpeedFeedbackError = new Thread(new TimeBasedAlarm(dh.getCmd_speedSB(), dh.getFb_speedSB(), stbSpeedFeedbackErrorAlarm, 5, ack, inhibit_stbSpeedFeedbackErrorAlarm));

        //PortRotationFeedbackError
        Thread portRotationFeedbackError = new Thread(new TimeBasedAlarm(dh.getCmd_podPosPS(), dh.getFb_podPosPS(), portRotationFeedbackErrorAlarm, 5, ack, inhibit_portRotationFeedbackErrorAlarm));

        //StbRotationFeedbackError
        Thread stbRotationFeedbackError = new Thread(new TimeBasedAlarm(dh.getCmd_podPosSB(), dh.getFb_podPosSB(), stbRotationFeedbackErrorAlarm, 5, ack, inhibit_stbRotationFeedbackErrorAlarm));

        //Start threads
        portSpeedFeedbackError.start();
        stbSpeedFeedbackError.start();
        portRotationFeedbackError.start();
        stbRotationFeedbackError.start();

    }
}
