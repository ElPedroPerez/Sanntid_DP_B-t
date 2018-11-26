/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guisystem;

/**
 * UDP ping
 * @author rocio
 */
public class UDPping implements Runnable
{

    private DataHandler dh;
    private UDPsender udpsender;
    private String ipAddress = "";
    private int sendPort;
    private long timeout = 1500000000;

    public UDPping(String ipAddress, int sendPort, DataHandler dh)
    {
        this.dh = dh;
        this.ipAddress = ipAddress;
        this.sendPort = sendPort;
    }

    /**
     * runs UDPping, creates UDP Sender
     */
    @Override
    public void run()
    {

        long elapsedTimer = 0;
        long lastTime = 0;

        while (true)
        {
            try
            {
               
                this.udpsender = new UDPsender();
                Thread.sleep(3000);            
                 lastTime = System.nanoTime();
                udpsender.send(ipAddress, "<GuiPing:true>", sendPort);
                while (!dh.getGuiPing() && elapsedTimer < timeout)
                {
                    elapsedTimer = (System.nanoTime() - lastTime) / 10;
                }

                if (dh.getGuiPing())
                {
                    dh.setPing(elapsedTimer);
                    dh.setGuiPing(false);
                    elapsedTimer = 0;
                } else
                {
                    dh.setPing(0);
                     dh.setGuiPing(false);
                     elapsedTimer = 0;
                }

            } catch (Exception e)
            {
                System.out.println("Ping exception");
            }
        }
    }
}
