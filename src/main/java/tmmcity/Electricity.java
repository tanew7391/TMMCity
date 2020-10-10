/*
 * Madhu Sivapragasam
   Dec 27
   The class that controls the attributes and behaviours of electricity buildings, extends the government zone class
 */
package tmmcity;

import org.newdawn.slick.Image;

/**
 *
 * @author Taylor
 */
public class Electricity extends GovernmentZone{
	private int power;

    public Electricity(Image tileImg, int xGrid, int yGrid, int difficulty) {
        super(tileImg, xGrid, yGrid, randomIntBetween(70, 60), difficulty);
		power = randomIntBetween(10, 1);										 
    }

    public Electricity(int maintenanceCost, int attract, Image tileImg, int xGrid, int yGrid, int p) {
        super(maintenanceCost, attract, tileImg, xGrid, yGrid);
		power = p;		  
    }
    
   
    public int getPower () {
        return power;
    }
    
    public void setPower (int p) {
        power = p;
    }	

    @Override
    public String toString() {
        return "Electric Plant" + "\nPower = " + power + super.toString();
    }
    
    
}
