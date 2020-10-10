/*
 * Matthew Reid
 * Dec 21, 2018
 * Clock object for game time
 */

package tmmcity;

//Import required for text formatting
import java.text.*;

public class Clock {
    
    //Decimal format for hours on clock
    DecimalFormat hourformat = new DecimalFormat("00");
    
    //Declare attributes
    private int min; 
    private int hour; 
    private int day; 
    private int month; 
    private int year; 
    private boolean monthEnd;
    private boolean dayEnd;
    
    //Declare array of month names
    String[] months = {"Jan","Feb","Mar","Apr","May","Jun", "Jul","Aug","Sep",
        "Oct", "Nov","Dec"};
    
    //Declare array of month lengths
    Integer[] monthlength = {31,28,31,30,31,30,31,31,30,31,30,31};
    
    //Primary clock object contructor
    public Clock()
    {
        min = 1;
        hour = 1;
        day = 1;
        month = 1;
        year = 1950;
        monthEnd = false;
        dayEnd = false;
    }
    
    //Secondary constructor
    public Clock(int min, int hour, int day, int month, int year)
    {
        min = this.min;
        hour = this.hour;
        day = this.day;
        month = this.month;
        year = this.year;
        monthEnd = false;
        dayEnd = false;
    }
    
    //Method to update the clock
    public void updateClock()
    {
        //Increase minute counter by 1
        min++;
        
        //If the minute counter is 60 (60mins=1hrs)
        if (min==60)
        {
            //Resest Minute counter to 1
            min=1;
            
            //Increase hour counter by 1
            hour++;
            
            //If the hour counter is 24 (24hrs=1day)
            if (hour == 24)
            {
                //Set dayEnd to true, used in the update method in TMMgame class
                dayEnd = true; 
                
                //Reset hour counter to 1
                hour = 1;
                
                //Increase day counter by 1
                day++;
                
                //If the day counter is the length of the month
                if (day == getMonthLength())
                {
                    //Set monthEnd to true, used in the update method in TMMgame class
                    monthEnd = true;
                    
                    //Reset day counter to 1
                    day=1;
                    
                    //Increase month counter by 1
                    month++;
                    
                    //If the month counter is 12 (12months=1year)
                    if (month == 12)
                    {
                        //Reset month counter to 1
                        month = 1;
                        //Increase year counter by 1
                        year++;
                    }
                }
            }
        }
    }
    
    //Getter for month name
    public String getMonthName()
    {
        //Return the name of the month using month int (-1 since array starts at 0)
        return months[month-1];
    }
    
    //Setter for month name
    public void setMonthName(String n)
    {
        months[month-1]= n;
    }
    
    //Getter for the length of the month
    public int getMonthLength()
    {
        return monthlength[month-1];
    }
    
    //Setter for the length of the month
    public void setMonthLength(int l)
    {
        monthlength[month-1]= l;
    }
    
    //Getter for the month end boolean
    public boolean getMonthEnd () {
        return monthEnd;   
    }
    
    //Getter for the day end boolean
    public boolean getDayEnd () {
        return dayEnd;   
    }
    
    //Setter for the month end boolean
    public void setMonthEnd(boolean m) {
        monthEnd = m;
    }
    
    //Setter for the day end boolean
    public void setDayEnd(boolean d) {
        dayEnd = d;
    }

    //String representation of date
    public String toStringDate()
    {
        String message = "" + getMonthName() + "." + day + "," + year;
        
        return message;
    }
    
    //String representation of time
    public String toStringTime()
    {
        String message = "" + hourformat.format(hour) + ":00";
        
        return message;
    }

    //Getter for minute
    public int getMin() {
        return min;
    }

    //Setter for minute
    public void setMin(int min) {
        this.min = min;
    }

    //Getter for hour
    public int getHour() {
        return hour;
    }

    //Setter for hour
    public void setHour(int hour) {
        this.hour = hour;
    }

    //Getter for day
    public int getDay() {
        return day;
    }

    //Setter for day
    public void setDay(int day) {
        this.day = day;
    }

    //Getter for Month
    public int getMonth() {
        return month;
    }

    //Setter for month
    public void setMonth(int month) {
        this.month = month;
    }

    //Getter for year
    public int getYear() {
        return year;
    }

    //Setter for year
    public void setYear(int year) {
        this.year = year;
    }
    
    
}
