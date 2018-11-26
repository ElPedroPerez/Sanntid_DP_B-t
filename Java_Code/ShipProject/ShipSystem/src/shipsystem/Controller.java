/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shipsystem;

/**
 * Controller class controls the vehicle and the states of it
 *
 * @author Haakon, Bjørnar, Robin
 */
public class Controller implements Runnable
{

    private final DataHandler dh;
    private final Logic logic;

    /**
     * create a new Controller
     *
     * @param dh the shared resource
     */
    public Controller(DataHandler dh)
    {
        this.dh = dh;
        this.logic = new Logic(this.dh);
    }

    /**
     * run the controller
     */
    @Override
    public void run()
    {
        long lastTime = System.nanoTime();
        while (true)
        {
//            try
//            {

            //Calculate angle on the pods from PS3 controller and moves the pods
            this.logic.calculateAngle();
            this.logic.bowThrusterSignal();

            dh.handleDataToRemote();
            long elapsedTimer = (System.nanoTime() - lastTime) / 1000000;

            if (elapsedTimer > 250)
            {
                elapsedTimer = 0;
                lastTime = System.nanoTime();

            }

            //} catch (InterruptedException ex)
//            {
//                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
//            }
        }
    }
}
