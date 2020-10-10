/*
Madhu Sivapragasam
Dec 27
The class that controls the attributes and behaviours of all hospital buildings, extends the government zone class
 */
package tmmcity;

import org.newdawn.slick.Image;

/**
 *
 * @author Taylor
 */

public class Hospital extends GovernmentZone{


    public Hospital(Image tileImg, int xGrid, int yGrid, int difficulty) {
        super(tileImg, xGrid, yGrid, 75, difficulty); //the 75 is the attraction value of the hospital
    }

    public Hospital(int residentCapacity, int maintenanceCost, int attract, Image tileImg, int xGrid, int yGrid) {
        super(maintenanceCost, attract, tileImg, xGrid, yGrid);
    }

    @Override
    public String toString() {
        return "Hospital" + super.toString();
    }
    
    
    

}
