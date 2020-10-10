/*
 * Taylor Newman
 * Dec 26
 * This is the class which sets the attributes and behaviours for road objects. It extends GovernmentZone which extends ZoneType. It extends government zone so that it can have a maintenence cost.
 */
package tmmcity;

//Required Import
import org.newdawn.slick.Image;

/**
 *
 * @author Taylor
 */
public class Road extends GovernmentZone {

    //Constructor
    public Road(Image tileImg, int xGrid, int yGrid) {
        super(randomIntBetween(10, 1), 50, tileImg, xGrid, yGrid); //set custom maintenence cost between 10 and 1, and set a default attractiveness of 50
    }

    //String representation of road class
    @Override
    public String toString() {
        return "Road" + super.toString();
    }

}
