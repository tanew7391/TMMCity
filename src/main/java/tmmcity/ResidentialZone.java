/*
 * Taylor Newman
 * December 21, 2018 
 * The non-abstract class which controls the behaviour and attributes of residential zones on the game grid, the houses. Many different calculations take place in this class, including proximity, area attractiveness, and resident count.
 * This class is an extension of the abstract ZoneType class, and is a part of the zone system
 */
package tmmcity;

import org.newdawn.slick.Image;

/**
 *
 * @author Taylor
 */
public class ResidentialZone extends ZoneType {

    private int residentCount; //Resident count of this object
    private double happiness; //happiness of this object
    private double attractCalcValue;
    private int policeProx = 20; //police proximity of this object
    private int hospitalProx = 20; //hospital proximity of this object
    private int waterProx = -1; //water treatment facility proximity of this object
    private int electricityProx = -1; //power plant proximity of this object
    private String name; //name of this object (townhouse)
    private static int totalJobsInUse = 0; //static variable for total jobs in use, also the population
    private int[] waterLoc = {-1, -1};
    private int[] electricityLoc = {-1, -1};
    private int residentIncome;
//private boolean nearHospital, nearPolice, nearWater, nearElectricity;

    public ResidentialZone(Image tileImg, int xGrid, int yGrid) { //primary constructor
        super(tileImg, xGrid, yGrid, 50);
        this.happiness = 50;
        if (CommercialZone.getTotalJobsAvailable() - totalJobsInUse > 6) {
            this.residentCount = randomIntBetween(6, 1); //1-6 person house
        } else {
            this.residentCount = randomIntBetween(CommercialZone.getTotalJobsAvailable() - totalJobsInUse, 0); //available jobs - 0 person house                        //Taylor
        }
        totalJobsInUse += this.residentCount;
        this.name = "Town house";

        attractionCalc(); //attraction calculations, proximity calculations and residency calculations are performed upon construction
        calculateProx();
        residency();
        residentIncome = (randomIntBetween(400, 10) * residentCount) + (1000 * residentCount);
    }

    public ResidentialZone(int residentCount, int happiness, Image tileImg, int xGrid, int yGrid, String name, int attraction) { //secondary constructor
        super(tileImg, xGrid, yGrid, attraction);
        this.residentCount = residentCount;
        this.happiness = happiness;
        this.name = name;
        attractionCalc(); //attraction calculations, proximity calculations and residency calculations are performed upon construction
        calculateProx();
        residency();
        totalJobsInUse += this.residentCount;
    }

    public int getResidentCount() {  //get and set methods for variables
        return residentCount;
    }

    public void setResidentCount(int r) {
        residentCount = r;
    }

    public double getHappiness() {
        return happiness;
    }

    public void setHappiness(double h) {
        happiness = h;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static int getTotalJobsInUse() {
        return totalJobsInUse;
    }

    public int[] getWaterLoc() {
        return waterLoc;
    }

    public void setWaterLoc(int[] waterLoc) {
        this.waterLoc = waterLoc;
    }

    public int[] getElectricityLoc() {
        return electricityLoc;
    }

    public void setElectricityLoc(int[] electricityLoc) {
        this.electricityLoc = electricityLoc;
    }

    public static void setTotalJobsInUse(int totalJobsInUse) {
        ResidentialZone.totalJobsInUse = totalJobsInUse;
    }

    public int getResidentIncome() {
        return residentIncome;
    }

    public void setResidentIncome(int residentIncome) {
        this.residentIncome = residentIncome;
    }

    public int getPoliceProx() {
        return policeProx;
    }

    public void setPoliceProx(int policeProx) {
        this.policeProx = policeProx;
    }

    public int getHospitalProx() {
        return hospitalProx;
    }

    public void setHospitalProx(int hospitalProx) {
        this.hospitalProx = hospitalProx;
    }

    public int getWaterProx() {
        return waterProx;
    }

    public void setWaterProx(int waterProx) {
        this.waterProx = waterProx;
    }

    public int getElectricityProx() {
        return electricityProx;
    }

    public void setElectricityProx(int electricityProx) {
        this.electricityProx = electricityProx;
    }
    
    

    public void residency() { //method that adjusts population depending on happiness

        if (happiness < 50 && residentCount > 0) { //numbers are subject to change
            int rand = randomIntBetween(residentCount, 1); //chooses random number to deduct or add to population if happiness is less than 50
            residentCount -= rand; //population and total jobs in use are deducted
            totalJobsInUse -= rand;
        } else if (happiness >= 50 && residentCount < 6 && CommercialZone.getTotalJobsAvailable() - totalJobsInUse > 0) { //if happiness is greater than 50
            int rand = randomIntBetween(6 - residentCount, 0); //random number is initialized
            while (rand > CommercialZone.getTotalJobsAvailable() - totalJobsInUse) { //while random number is greater than the total jobs available commercially
                rand = randomIntBetween(6 - residentCount, 0); //random number must be reset to a number lower than the total jobs available                                    //Taylor

                System.out.println(rand + ", " + residentCount);
            }
            System.out.println(rand + ", " + residentCount);
            residentCount += rand; //resident count, total jobs in use and total population are all increased
            totalJobsInUse += rand;

        }

        if (totalJobsInUse < 0) { //just a check, just in case
            totalJobsInUse = 0;
        }

        if (electricityProx < 0 || waterProx < 0) { //just an idea of how to use proximity, using 0 as placeholder, can be changed - Madhu
            //if there are no power plants or water treatment facilities nearby, population of this object is set to 0
            //CommercialZone.setTotalJobsAvailable(CommercialZone.getTotalJobsAvailable()+residentCount); 
            totalJobsInUse -= residentCount;
            residentCount = 0;
        }

        residentIncome = (randomIntBetween(400, 10) * residentCount) + (1000 * residentCount);
    }

    public void happy(double tax, double attraction) { //method that adjusts happiness depending on attraction and tax, police proximity and hospital proximity                         //Taylor
        this.happiness = attraction - tax - this.policeProx - this.hospitalProx;
    }

    public double attractionCalc() { //method to calculate attraction
        int gridX = this.getXGrid(); // integers which hold the x and y values of the current objects grid location
        int gridY = this.getYGrid();
        double average = 0;
        int averageCount = 0;
        ZoneType[][] gameGridTemp = TMMgame.getGameGrid(); //temporary grid is declared and initialized
        if (TMMgame.roadCheck(gridX, gridY)) { //if there is a road nearby
            for (int x = -5; x <= 5; x++) {  //checks a 10 by 10 grid space around the residential zone                                                                                 //Taylor
                for (int y = -5; y <= 5; y++) {
                    if (gridX + x > 0 && gridX + x < 28 && gridY + y > 0 && gridY + y < 18) {
                        if (gameGridTemp[gridX + x][gridY + y] != null) { //make sure the grid square is not empty, or nullpointerexception will occur
                            if (gameGridTemp[gridX + x][gridY + y].getAttraction() != 50) {//if the attraction of an object is 50, then don't calculate it, 50 is the median attractiveness and the default for roads and houses
                                average += gameGridTemp[gridX + x][gridY + y].getAttraction(); //the attraction value is added to the average
                                averageCount++;
                            }
                        }
                    }
                }
            }
        }

        this.attractCalcValue = average / averageCount; //the average attraction is calculated
        //System.out.println(average + " / " + averageCount + " = " + this.attractCalcValue);
        return this.attractCalcValue;
    }

    public void calculateProx() { //method to calculate proximity
        int gridX = this.getXGrid(); // integers which hold the x and y values of the current objects grid location
        int gridY = this.getYGrid();
        boolean waterFound = false, electricityFound = false, hospitalFound = false, policeFound = false;
        ZoneType[][] gameGridTemp = TMMgame.getGameGrid(); //temporary grid is declared and initialized
        for (int x = -10; x <= 10; x++) { //for loop goes through 20 by 20 grid space around the object
            for (int y = -10; y <= 10; y++) {
                if (gridX + x >= 0 && gridX + x <= 28 && gridY + y >= 0 && gridY + y <= 17) {
                    if (gameGridTemp[gridX + x][gridY + y] != null) { //if the grid spot is not null
                        if (gameGridTemp[gridX + x][gridY + y] instanceof Police) { //if statements check if it is a police, hospital, water treatment plant or power plant             //Taylor
                            if ((int) (Math.hypot(x, y)) < policeProx || policeProx == -1) { //if so proximity is calculated using the hypoteuse
                                policeProx = (int) (Math.hypot(x, y));

                            }
                            policeFound = true;

                        } else if (gameGridTemp[gridX + x][gridY + y] instanceof Hospital) {
                            if ((int) (Math.hypot(x, y)) < hospitalProx || hospitalProx == 20) {
                                hospitalProx = (int) (Math.hypot(x, y));
                                System.out.println("yep");

                            }
                            hospitalFound = true;

                        } else if (gameGridTemp[gridX + x][gridY + y] instanceof Water) {

                            if (((int) (Math.hypot(x, y)) < waterProx || waterProx == -1) && ((Water) gameGridTemp[gridX + x][gridY + y]).getHydro() > 0) {

                                waterProx = (int) (Math.hypot(x, y)); //distance to the water station (Pythagoreans formula cast to an int)
                                if (waterLoc[0] != -1) {
                                    ((Water) gameGridTemp[waterLoc[0]][waterLoc[1]]).setHydro(((Water) gameGridTemp[waterLoc[0]][waterLoc[1]]).getHydro() + 1); //sets the previous water station to one more, as the residential zone won't use it anymore
                                }
                                ((Water) gameGridTemp[gridX + x][gridY + y]).setHydro(((Water) gameGridTemp[gridX + x][gridY + y]).getHydro() - 1); //sets the hydro for a water station to one less
                                waterLoc[0] = gridX + x; //sets the location of the water station this residential zone is using
                                waterLoc[1] = gridY + y;

                            } else if (((Water) gameGridTemp[gridX + x][gridY + y]).getHydro() <= 0) { //if the water station has no more hydro it cannot service anymore residents
                                TMMgame.setInfoLine1("Water Station Near house cannot service anymore residents");
                            }
                            waterFound = true;

                        } else if (gameGridTemp[gridX + x][gridY + y] instanceof Electricity) {
                            if (((int) (Math.hypot(x, y)) < electricityProx || electricityProx == -1) && ((Electricity) gameGridTemp[gridX + x][gridY + y]).getPower() > 0) { //if the distance between this electric station and the one already registered to it is less, reasign to the closer one, or if -1 then it is not assigned. If the station has no more capacity, then skip over it
                                electricityProx = (int) (Math.hypot(x, y));
                                if (electricityLoc[0] != -1) { //check if the residential zone already had an electricity plant
                                    ((Electricity) gameGridTemp[electricityLoc[0]][electricityLoc[1]]).setPower(((Electricity) gameGridTemp[electricityLoc[0]][electricityLoc[1]]).getPower() + 1); //sets the previous electric station to one more

                                }

                                ((Electricity) gameGridTemp[gridX + x][gridY + y]).setPower(((Electricity) gameGridTemp[gridX + x][gridY + y]).getPower() - 1); //sets the power for a electricity station to one less

                                electricityLoc[0] = gridX + x; //set location 
                                electricityLoc[1] = gridY + y;

                            } else if (((Electricity) gameGridTemp[gridX + x][gridY + y]).getPower() <= 0) {
                                TMMgame.setInfoLine1("Electricity Station Near house cannot service anymore residents");
                            }
                            electricityFound = true;

                        }

                    }

                }

            }

        }

        if (!policeFound) {
            policeProx = 20;
        }
        if (!hospitalFound) {  //default distances, 20 for police and hospital because it is possible to have residents while having no police station or hopital, so just make it a far distance
            hospitalProx = 20;
        }
        if (!waterFound) {
            waterProx = -1;
        }
        if (!electricityFound) {
            electricityProx = -1;
        }

    }

    @Override
    public String toString() { //to string method
        String policeString = "" + policeProx;
        String hospitalString = "" + hospitalProx;
        String waterString = "" + waterProx;
        String electricityString = "" + electricityProx;
        if (policeProx == 20) {
            policeString = "No Police Nearby";
        }
        if (hospitalProx == 20) {
            hospitalString = "No hospital Nearby"; //replace the default placeholder values with these strings
        }
        if (waterProx == -1) {
            waterString = "No water station nearby";
        }
        if (electricityProx == -1) {
            electricityString = "No electricity station nearby";
        }
        return "Residential Zone, ResidentCount = " + residentCount + ", Name = " + name + "\nHappiness = " + (int) happiness + ", Surrounding Attraction = " + (int) attractCalcValue + "\nDistance from Police = " + policeString + ", Resident income: " + this.residentIncome + "\nDistance from Hospital = " + hospitalString + "\nDistance from Water = " + waterString + "\nDistance from Electricity = " + electricityString;
    }

}
