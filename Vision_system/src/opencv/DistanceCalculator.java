/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opencv;

import static java.lang.Math.asin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toDegrees;

/**
 *
 * @author rocio
 */
public class DistanceCalculator
{

    double Y = 0;
   
    double angle = 0;

    public void distanceToShip(double X, double distance)
    {
        //finding Y
        //sqrt(x*x-ditance * distance)
        Y = sqrt(X * X - distance * distance);
//        angle = distance/Y;
//        angle = asin(angle);
//        angle = toDegrees(angle);
//        System.out.println("Angle is: " + angle);
        System.out.println("Y is :" + Y);        
    }
}
