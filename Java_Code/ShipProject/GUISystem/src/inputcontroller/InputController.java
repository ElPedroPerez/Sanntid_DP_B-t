/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InputController;

import com.exlumina.j360.ButtonListener;
import com.exlumina.j360.Controller;
import com.exlumina.j360.ValueListener;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Haakon
 */
public class InputController
{
    private static boolean finished = false;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        System.out.println("REMEMBER: XBOX button labels. (X is not X, X is SQUARE)");
        ValueListener Ly = new LeftThumbYListener();
        ValueListener Lx = new LeftThumbXListener();
        ValueListener Ry = new RightThumbYListener();
        ValueListener Rx = new RightThumbXListener();
        ButtonListener L1 = new LeftShoulderListener();
        ButtonListener R1 = new RightShoulderListener();
        ButtonListener X = new XButtonListener();
        ButtonListener Y = new YButtonListener();
        ButtonListener A = new AButtonListener();
        ButtonListener B = new BButtonListener();
        Controller c1 = Controller.C1;

        c1.leftThumbY.addValueChangedListener(Ly);
        c1.leftThumbX.addValueChangedListener(Lx);
        c1.rightThumbY.addValueChangedListener(Ry);
        c1.rightThumbX.addValueChangedListener(Rx);
        c1.buttonLeftShoulder.addButtonPressedListener(L1);
        c1.buttonRightShoulder.addButtonPressedListener(R1);
        c1.buttonX.addButtonPressedListener(X);
        c1.buttonY.addButtonPressedListener(Y);
        c1.buttonA.addButtonPressedListener(A);
        c1.buttonB.addButtonPressedListener(B);

        while (!finished)
        {
            // run
        }

//      try
//        {
//            Thread.sleep(10000);
//        }
//        catch (InterruptedException ex)
//        {
//            Logger.getLogger(InputController.class.getName()).log(Level.SEVERE, null, ex);
//        }
        //System.out.println("Goodbye!");
        //System.exit(0);
    }

    public void setFinished()
    {
        this.finished = true;
    }

}
