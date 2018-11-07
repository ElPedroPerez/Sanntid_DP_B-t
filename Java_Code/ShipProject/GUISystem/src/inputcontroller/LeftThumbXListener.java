/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InputController;

import com.exlumina.j360.ValueListener;

/**
 *
 * @author Haakon
 */
class LeftThumbXListener implements ValueListener
{
    @Override
    public void value(int newValue)
    {
        System.out.printf("Lx: " + "%6d\n", newValue);
    }
}