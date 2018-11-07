/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InputController;

import com.exlumina.j360.ButtonListener;

/**
 *
 * @author Haakon
 */
class YButtonListener implements ButtonListener
{
    @Override
    public void button(boolean pressed)
    {
        System.out.println("Y button was pressed.");
    }
}