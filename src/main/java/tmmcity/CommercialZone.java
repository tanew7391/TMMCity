/*
 * Taylor Newman
 * December 21, 2018 
 * The non-abstract class which controls the behaviour and attributes of in game 'stores'
 * This class is an extension of the abstract ZoneType class, and is a part of the zone system
 */
package tmmcity;

import java.util.ArrayList;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Taylor
 */
public class CommercialZone extends ZoneType {

    private int revenuePerYear; //revenue that this store can produce with full workers
    private int revenuePerYearAvailable; //calculated revenue that the store produces with its available workers
    private int jobsCreated; //max amount of jobs that this CommercialZone can support, directly related to revenuePerYear
    private int jobsInUse; //current workers actually working at this store, gets smaller with a smaller population. Used to calculte revenuePerYearAvailable
    private static int totalJobs = 0; //overall, the total amount of jobs of all combined CommercialZones(static)
    private static int totalJobsAvailable = 0; //overall, the total amount of jobs currently available for residents, this is limited by IndustrialZone jobs
    private static int totalRevenueAvailable = 0; //total revenue that all stores are contributing to the city
    private static ArrayList<CommercialZone> commercialStack = new ArrayList<>(); //an array of all CommercialZone pointers

    public CommercialZone(Image tileImg, int xGrid, int yGrid) throws SlickException { //main constructor
        super(tileImg, xGrid, yGrid, randomIntBetween(70, 50));   //random attractiveness between 100 and 50
        this.revenuePerYear = this.revenuePerYearAvailable = randomIntBetween(10000, 1000); //every business can create a revenue between 10000 and 1000
        this.jobsCreated = revenuePerYear / 1000; //jobs are based off of revenue                                                                                               //Taylor
        CommercialZone.totalJobs += this.jobsCreated; //increase the total job ount
        commercialStack.add(this); //add this new zone to the commercialZone list
        totalJobCalc();
        limitCommerce();
    }

    public CommercialZone(Boolean active, int attract, int revenuePerYear, Image tileImg, int xGrid, int yGrid, int totalJobs, int jobsCreated) { //secondary constructor incase you need to clone
        super(tileImg, xGrid, yGrid, attract);
        this.revenuePerYear = this.revenuePerYearAvailable = revenuePerYear;
        this.jobsCreated = jobsCreated;
        CommercialZone.totalJobs += this.jobsCreated;
        commercialStack.add(this);
        totalJobCalc();
        limitCommerce();
    }

    public static void totalJobCalc() { //calculate the amount of jobs the city can sustain, jobs are limited by the lowest zone, if industry jobs = 3 and commercial jobs = 5, then there are only 3 available jobs
        int industrialJobs = IndustrialZone.getTotalCommercialAvailable();
        if (totalJobs >= industrialJobs) {
            totalJobsAvailable = industrialJobs;   //grab whichever value is lower, they limit themselves                                                                     //Taylor
        } else {
            totalJobsAvailable = totalJobs;
        }
    }

    public static void limitCommerce() { //takes the total amount of residents and goes through each commercial zone giving the oldest commercialZones workers. If resident count is less than all commercial zone jobs, then the newest commercial zones will be limited of workers and revenue
        totalRevenueAvailable = 0;
        int residentsTemp = ResidentialZone.getTotalJobsInUse(); //set a temporary resident value
        for (int i = 0; i < commercialStack.size(); i++) { //loop through every CommercialZone instance
            CommercialZone tempZone = commercialStack.get(i); 
            if (residentsTemp <= 0) { // check if in the previous loop the temporary residentsTemp was set below 0, signifying that there are no more workers that can work at other ComemrcialZones, everything else in the commercial zone array is set to 0 revenue and 0 jobs
                tempZone.jobsInUse = 0;
                tempZone.revenuePerYearAvailable = 0;
            } else { //if there are still workers available

                if (tempZone.jobsCreated < residentsTemp) { //check if there are enough available workers to support the commercial zone i (the loop)                         //Taylor
                    tempZone.revenuePerYearAvailable = tempZone.revenuePerYear; //if so then revenue and jobs remain the same
                    tempZone.jobsInUse = tempZone.jobsCreated;
                    residentsTemp -= tempZone.jobsCreated; //remove the jobs from this store from residentsTemp

                } else {
                    tempZone.revenuePerYearAvailable = residentsTemp * (tempZone.revenuePerYear / tempZone.jobsCreated); //if the residents available is less than the store's jobs, the store will have a fraction of the workers, this is the calculation of that fraction
                    tempZone.jobsInUse = residentsTemp; 
                    residentsTemp = 0; //set available workers to 0
                }
            }
            commercialStack.set(i, tempZone); //set the temporary zone as the one we are working on at i
        }
        
        for (int i = 0; i < commercialStack.size(); i++) { //loop to calculate the total revenue
            if (commercialStack.get(i).revenuePerYearAvailable == 0) { //if the busines has no revenue, then the rest of business will also have no revenue, so we cn break the loop
                break;
            }
            totalRevenueAvailable += commercialStack.get(i).revenuePerYearAvailable;
        }

    }
    
    public static ArrayList<CommercialZone> getCommercialStack() {
        return commercialStack;
    }

    public static void setCommercialStack(ArrayList<CommercialZone> commercialStack) {
        CommercialZone.commercialStack = commercialStack;
    }

    public static void removeCommercialStack(CommercialZone x) {
        commercialStack.remove(x);
    }

    public int getRevenuePerYear() {
        return revenuePerYear;
    }

    public void setRevenuePerYear(int r) {
        revenuePerYear = r;
    }

    public int getRevenuePerYearAvailable() {
        return revenuePerYearAvailable;
    }

    public void setRevenuePerYearAvailable(int revenuePerYearAvailable) {
        this.revenuePerYearAvailable = revenuePerYearAvailable;
    }

    public static int getTotalRevenueAvailable() {
        return totalRevenueAvailable;
    }

    public static void setTotalRevenueAvailable(int totalRevenueAvailable) {
        CommercialZone.totalRevenueAvailable = totalRevenueAvailable;
    }

    public int getJobsCreated() {
        return jobsCreated;
    }

    public void setJobsCreated(int jobsCreated) {
        this.jobsCreated = jobsCreated;
    }

    @Override
    public String toString() {
        return "Commercial Area" + "\nRevenue per year: " + revenuePerYearAvailable + "\nEmployees working/Employee limit : " + this.jobsInUse + "/" + this.jobsCreated + super.toString();
    }

    public static int getTotalJobs() {
        return totalJobs;
    }

    public static void setTotalJobs(int totalJobs) {
        CommercialZone.totalJobs = totalJobs;
    }

    public static int getTotalJobsAvailable() {
        return totalJobsAvailable;
    }

    public static void setTotalJobsAvailable(int totalJobsAvailable) {
        CommercialZone.totalJobsAvailable = totalJobsAvailable;
    }
    
    

}
