/*
 * Madhu Sivapragasam
   Dec 27
   The class that controls the attributes and behaviours of water buildings, extends the government zone class
 */
package tmmcity;

import org.newdawn.slick.Image;

/**
 *
 * @author Taylor
 */
public class Water extends GovernmentZone{

	private int hydro;		     //hydro is the amount of houses that this hydro plant can service, once hydro reaches 0, no
    public Water(Image tileImg, int xGrid, int yGrid, int difficulty) {
        super(tileImg, xGrid, yGrid, randomIntBetween(70, 60), difficulty);
		hydro = randomIntBetween(10, 2);										
    }

    public Water(int maintenanceCost, int attract, Image tileImg, int xGrid, int yGrid, int h) {
        super(maintenanceCost, attract, tileImg, xGrid, yGrid);
		hydro = h;		  
    }
    
    public int getHydro () {
        return hydro;
    }
    
    public void setHydro (int w) {
        hydro = w;
    }		

    @Override
    public String toString() {
        return "Water Service Center" + "\nHydro Available = " + hydro + super.toString();
    }
    
    
    
}
