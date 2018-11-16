/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guisystem;

/**
 *
 * @author rocio
 */
public class UDPping implements Runnable
{

    private Datahandler dh;
    private UDPsender udpsender;
    private String ipAddress = "";
    private int sendPort;
    private long timeout = 15000;

    public UDPping(String ipAddress, int sendPort, Datahandler dh)
    {
        this.dh = dh;
        this.ipAddress = ipAddress;
        this.sendPort = sendPort;
    }

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
                    elapsedTimer = (System.nanoTime() - lastTime) / 1000000;
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
