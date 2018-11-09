/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InputController;

import com.exlumina.j360.ButtonListener;
import com.exlumina.j360.Controller;
import com.exlumina.j360.ValueListener;
import com.exlumina.j360.XInputGamepad;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Haakon
 */
public class InputController implements Runnable
{

    private static boolean finished = false;
    private int btnLy = 0;
    private int btnLx = 0;
    private int btnRy = 0;
    private int btnRx = 0;
    private boolean btnL1 = false;
    private boolean btnR1 = false;
    private boolean btnX = false;
    private boolean btnY = false;
    private boolean btnA = false;
    private boolean btnB = false;
    private int angle = 0;
    private static int lastAngle = 0;
    private static float lastVal = 0;

    private boolean L1pressed = false;
    private boolean R1pressed = false;
    private boolean Xpressed = false;
    private boolean Ypressed = false;
    private boolean Apressed = false;
    private boolean Bpressed = false;

    @Override
    public void run()
    {
        System.out.println("REMEMBER: XBOX button labels. (X is not X, X is SQUARE)");

        ValueListener Ly = new LeftThumbYListener(this);
        //ValueListener Lx = new LeftThumbXListener(this);
        ValueListener Ry = new RightThumbYListener(this);
        ValueListener Rx = new RightThumbXListener(this);
        ButtonListener L1 = new LeftShoulderListener(this);
        ButtonListener R1 = new RightShoulderListener(this);
        ButtonListener X = new XButtonListener(this);
        ButtonListener Y = new YButtonListener(this);
        ButtonListener A = new AButtonListener(this);
        ButtonListener B = new BButtonListener(this);
        Controller c1 = Controller.C1;

        c1.leftThumbY.addValueChangedListener(Ly);
        //c1.leftThumbX.addValueChangedListener(Lx);
        c1.rightThumbY.addValueChangedListener(Ry);
        c1.rightThumbX.addValueChangedListener(Rx);
        c1.buttonLeftShoulder.addButtonPressedListener(L1);
        c1.buttonRightShoulder.addButtonPressedListener(R1);
        c1.buttonX.addButtonPressedListener(X);
        c1.buttonY.addButtonPressedListener(Y);
        c1.buttonA.addButtonPressedListener(A);
        c1.buttonB.addButtonPressedListener(B);
        c1.buttonLeftShoulder.addButtonReleasedListener(L1);
        c1.buttonRightShoulder.addButtonReleasedListener(R1);
        c1.buttonX.addButtonReleasedListener(X);
        c1.buttonY.addButtonReleasedListener(Y);
        c1.buttonA.addButtonReleasedListener(A);
        c1.buttonB.addButtonReleasedListener(B);

        for (;;)
        {
            // run
            printDegree();
            printButtons();
        }
    }

    public void printButtons()
    {
        if (btnL1 && !L1pressed)
        {
            System.out.println("L1");
            L1pressed = true;
        }
        else if (!btnL1)
        {
            L1pressed = false;
        }
        if (btnR1 && !R1pressed)
        {
            System.out.println("R1");
            R1pressed = true;
        }
        else if (!btnR1)
        {
            R1pressed = false;
        }
        if (btnX && !Xpressed)
        {
            System.out.println("X");
            Xpressed = true;
        }
        else if (!btnX)
        {
            Xpressed = false;
        }
        if (btnY && !Ypressed)
        {
            System.out.println("Y");
            Ypressed = true;
        }
        else if (!btnY)
        {
            Ypressed = false;
        }
        if (btnA && !Apressed)
        {
            System.out.println("A");
            Apressed = true;
        }
        else if (!btnA)
        {
            Apressed = false;
        }
        if (btnB && !Bpressed)
        {
            System.out.println("B");
            Bpressed = true;
        }
        else if (!btnB)
        {
            Bpressed = false;
        }
    }

    public void printDegree()
    {
        angle = this.FindDegree(btnRx, btnRy);
        if (angle != lastAngle)
        {
            System.out.println(angle);
            this.lastAngle = angle;
        }

    }

    public static int FindDegree(int x, int y)
    {
        float value = (float) ((Math.atan2(x, y) / Math.PI) * 180f);
        if ((x < 98 && x > -98) && (y < 98 && y > -98))
        {
            value = lastVal;
        }
        else if (value < 0)
        {
            value += 360f;
        }
        lastVal = value;
        return Math.round(value);
    }

    public void setFinished()
    {
        this.finished = true;
    }

    public int getBtnLy()
    {
        return btnLy;
    }

    public void setBtnLy(int btnLy)
    {
        this.btnLy = btnLy;
    }

    public int getBtnLx()
    {
        return btnLx;
    }

    public void setBtnLx(int btnLx)
    {
        this.btnLx = btnLx;
    }

    public int getBtnRy()
    {
        return btnRy;
    }

    public void setBtnRy(int btnRy)
    {
        this.btnRy = btnRy;
    }

    public int getBtnRx()
    {
        return btnRx;
    }

    public void setBtnRx(int btnRx)
    {
        this.btnRx = btnRx;
    }

    public boolean isBtnL1()
    {
        return btnL1;
    }

    public void setBtnL1(boolean btnL1)
    {
        this.btnL1 = btnL1;
    }

    public boolean isBtnR1()
    {
        return btnR1;
    }

    public void setBtnR1(boolean btnR1)
    {
        this.btnR1 = btnR1;
    }

    public boolean isBtnX()
    {
        return btnX;
    }

    public void setBtnX(boolean btnX)
    {
        this.btnX = btnX;
    }

    public boolean isBtnY()
    {
        return btnY;
    }

    public void setBtnY(boolean btnY)
    {
        this.btnY = btnY;
    }

    public boolean isBtnA()
    {
        return btnA;
    }

    public void setBtnA(boolean btnA)
    {
        this.btnA = btnA;
    }

    public boolean isBtnB()
    {
        return btnB;
    }

    public void setBtnB(boolean btnB)
    {
        this.btnB = btnB;
    }

}
