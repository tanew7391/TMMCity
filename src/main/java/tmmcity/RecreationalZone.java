/*
Madhu Sivapragasam
Dec 21
The class that controls the attributes and behaviours of all recreational buildings, extends the Zone type class
 */
package tmmcity;

import org.newdawn.slick.Image;

/**
 *
 * @author Taylor
 */
public class RecreationalZone extends ZoneType {

    private int maintenanceCost; //variable which holds the maintenance cost of each individual recreational zone object
    private String name; //variable which holds the name of the recreational object
    private static int expense; //variable which holds the combined expense of all recreational zone objects

    public RecreationalZone(Image tileImg, int xGrid, int yGrid, int attractionMax, int difficulty) {
        super(tileImg, xGrid, yGrid, randomIntBetween(attractionMax, attractionMax - 10));
        if (difficulty == 1) { //maintenance cost is decided depending on difficulty and attraction 
            this.maintenanceCost = 0;
        }
        else {
        
            if (this.getAttraction() < 60) {
                this.maintenanceCost = randomIntBetween(150, 50);
            } else if (this.getAttraction() < 70) {
                this.maintenanceCost = randomIntBetween(400, 200);   //Taylor
            } else {
                this.maintenanceCost = randomIntBetween(500, 400);
            }
            
            this.maintenanceCost = maintenanceCost * ((difficulty - 1)/2);
        }
        expense += maintenanceCost;

        if (attractionMax == 60) { //name is decided based on attraction
            this.name = "Park";
        } else if (attractionMax == 70) {
            this.name = "Campsite";
        } else {
            this.name = "Stadium";
        }
    }

    public RecreationalZone(int maintenanceCost, int attract, Image tileImg, int xGrid, int yGrid) {
        super(tileImg, xGrid, yGrid, attract); 
        this.maintenanceCost = maintenanceCost;
        expense += maintenanceCost;
    }

    public int getMaintenanceCost() {
        return maintenanceCost;
    }

    public void setMaintenanceCost(int maintenanceCost) {
        this.maintenanceCost = maintenanceCost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Recreational Area: " +  name +"\nmaintenanceCost = " + maintenanceCost + super.toString();
    }
    
    public static int getExpense() {
        return expense;
    }
    
    public static void setExpense(int e) {
        expense = e;
    }

    
    
    
}
