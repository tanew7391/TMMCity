/*
 * Taylor Newman
 * December 21, 2018 
 * The non-abstract class which controls the behaviour and attributes of industry zones on the game grid.
 * This class is an extension of the abstract ZoneType class, and is a part of the zone system
 */
package tmmcity;

import org.newdawn.slick.Image;

/**
 *
 * @author Taylor
 */
public class IndustrialZone extends ZoneType {

    private int commercialIncrease; //every time new industry is created, more jobs are available
    private static int totalCommercialAvailable = 0;

    public IndustrialZone(Image tileImg, int xGrid, int yGrid) {
        super(tileImg, xGrid, yGrid, randomIntBetween(49, 40));
        this.commercialIncrease = randomIntBetween(10, 1); //commercial increase is anywhere between 10 and 1
        totalCommercialAvailable += commercialIncrease; //static variable which keeps track of all industry created jobs                                    //Taylor
    }

    
    public IndustrialZone(int attract, int commercialIncrease, Image tileImg, int xGrid, int yGrid) {
        super(tileImg, xGrid, yGrid, attract);
        this.commercialIncrease = commercialIncrease;
        totalCommercialAvailable += commercialIncrease;
    }

    public int getCommercialIncrease() {
        return commercialIncrease;
    }

    public void setCommercialIncrease(int c) {
        commercialIncrease = c;
    }

    @Override
    public String toString() {
        return "Industrial Area" + "\nCommercial Increase = " + commercialIncrease + super.toString();
    }

    public static int getTotalCommercialAvailable() {
        return totalCommercialAvailable;
    }

    public static void setTotalCommercialAvailable(int totalCommercialAvailable) {
        IndustrialZone.totalCommercialAvailable = totalCommercialAvailable;
    }
    
    

}
