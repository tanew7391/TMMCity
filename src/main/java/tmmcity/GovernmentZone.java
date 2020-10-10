/*
 * Madhu Sivapragasam
   Dec 21
   The abstract class that controls some of the attributes of all government zone buildings and is extended to all government zone buildings, extends the zone type class
 */
package tmmcity;

import org.newdawn.slick.Image;

/**
 *
 * @author Taylor
 */
abstract public class GovernmentZone extends ZoneType{
    private int maintenanceCost; //variable which holds maintenance cost
    private static int expense; //variable which holds the expense value of all government zone buildings
    
        
    public GovernmentZone(Image tileImg, int xGrid, int yGrid, int attract, int difficulty) {
        super(tileImg, xGrid, yGrid, attract);
        this.maintenanceCost = randomIntBetween(500, 1); //maintenance cost is decided randomly and then adjusted depending on difficulty
        if (difficulty == 1) {
            this.maintenanceCost = 0;
        } 
        else {
            this.maintenanceCost = maintenanceCost * ((difficulty - 1)/2);
        }
        expense += maintenanceCost;
    }
    
    public GovernmentZone(int maintenanceCost, int attract, Image tileImg, int xGrid, int yGrid) {
        super(tileImg, xGrid, yGrid, attract);
        this.maintenanceCost = maintenanceCost;
        expense += maintenanceCost;
    }
    
    public int getMaintenanceCost() {
        return maintenanceCost;
    }
    
    public void setMaintenanceCost(int m) {
        maintenanceCost = m;
    }

    @Override
    public String toString() {
        return "\nMaintenance Cost = " + maintenanceCost + super.toString();
    }
    
    public static int getExpense() {
        return expense;
    }
    
    public static void setExpense (int e) {
        expense = e;
    }
    
    
    
}
