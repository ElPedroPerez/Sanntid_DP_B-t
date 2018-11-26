/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shipsystem;

/**
 * Sends data using an UDPsender
 * 
 * @author Haakon
 */
public class GUIsender implements Runnable
{

    UDPsender udpSender = new UDPsender();
    DataHandler dh;

    /**
     *
     * @param dh The shared resource object of the DataHandler
     */
    public GUIsender(DataHandler dh)
    {
        this.dh = dh;
    }

    @Override
    public void run()
    {
        while (true)
        {
            if (dh.dataToGuiUpdated)
            {
                udpSender.send(ShipSystem.ipAddressGUI, dh.getDataToGUI(), ShipSystem.sendPort);
            }
        }
    }

}
