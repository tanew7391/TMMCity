/*
Madhu Sivapragasam
Dec 27
The class that controls the attributes and behaviours of all police buildings, extends the government zone class
 */
package tmmcity;

import org.newdawn.slick.Image;

/**
 *
 * @author Taylor
 */
public class Police extends GovernmentZone{

    public Police(Image tileImg, int xGrid, int yGrid, int difficulty) {
        super(tileImg, xGrid, yGrid, 50, difficulty); //the 50 is the attraction value of the police station
        
    }
    
    
    public Police(int maintenanceCost, int attract, Image tileImg, int xGrid, int yGrid) {
        super(maintenanceCost, attract, tileImg, xGrid, yGrid);
    }

    @Override
    public String toString() {
        return "Police Station" + super.toString();
    }
    
    
    
}
