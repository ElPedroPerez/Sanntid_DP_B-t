/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guisystem;
import InputController.InputController;

/**
 *
 * @author Haakon
 */
public class GUISystem  
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        Thread inputController = new Thread(new InputController());
        inputController.start();
    }
    
}
