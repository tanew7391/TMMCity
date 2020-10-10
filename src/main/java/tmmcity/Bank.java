/*
 * Madhu Sivapragasam
 * Dec 21 2018
 * Bank class for in game money system
 */
package tmmcity;
import java.text.DecimalFormat;
/**
 *
 * @author Madhu
 */
public class Bank {
    //declare all variables
    private Double money; //total money
    private Double tax; //tax rate
    private Double revenue; //revenue
    private Double bigLoan; // large loan
    private Double mediumLoan; // medium loan
    private Double smallLoan; // small loan
    private int bigTerm; //term for the biggest loan
    private int mediumTerm; //term for the medium loan
    private int smallTerm; //term for the smallest loan
    private int residentialTaxes;
    private Double expense; //expenses
    private int difficulty;
    private double interest;
    
    DecimalFormat moneyformat = new DecimalFormat("0.00");
    
    public Bank () { //primary constructor
        money = 0.0;
        tax = 0.05;
        revenue = 0.0;
        bigLoan = 0.0; //numbers subject to change
        mediumLoan = 0.0;
        smallLoan = 0.0;
        bigTerm = 0;
        mediumTerm = 0;
        smallTerm = 0;
        expense = 0.0;
        residentialTaxes = 0;
        difficulty = 3;
        interest = 0.01;
       

    }
    
    public Bank (double m, double t, double r, double l, double e) { //secondary constructor
        money = m; 
        tax = t;
        revenue = r;
        bigLoan = 0.0; //big loan, medium loan and small loan represent monthly payments
        mediumLoan = 0.0;
        smallLoan = 0.0;
        bigTerm = 0;//big term, medium term and small term represent the number of months remaining
        mediumTerm = 0;
        smallTerm = 0;
        expense = e;
        difficulty = 3;
        interest = 0.01;
    }
    
    public Double getMoney () {//get method for money
        return money;
    }
    
    public void setMoney (Double m) { //set method for money
        money = m;
    }
    
    public Double getTax () { //get method for tax
        return tax;
    }
    
    public void setTax (Double t) { //set method for tax
        tax = t;
    }
    
    public Double getRevenue () { //get method for revenue
        return revenue;
    }
    
    public void setRevenue (double r) { //set method for revenue
        revenue = r;
    }
 
    public Double getExpense () {  //get method for expenses
        return expense;
    }
    
    public void setExpense (double e) { //set method for expense
        expense = e;
    }
    
    
    public void smallTax () { //method that sets tax rate for small tax
        tax = 0.05;
    }
    
    public void mediumTax () { //method that sets tax rate for medium tax
        tax = 0.10;
    }
    
    public void bigTax () { //method that sets tax rate for big tax
        tax = 0.15;
    }

    public int getResidentialTaxes() { //method that returns residential tax
        return residentialTaxes;
    }

    public void setResidentialTaxes(int residentialTaxes) { //method that sets resdiential tax
        this.residentialTaxes = residentialTaxes;
    }
    
    public void calculateInterest (int d) { //method that calculates interest depending on difficulty
        difficulty = d; 
        if (d == 2) {
            interest = 0.01;
        }
        else if (d==3) {
            interest = 0.05;
        }
        else if (d==4) {
            interest = 0.1;
        }
        
        
    }
    
    
    
    public void bigLoan () { //method for the big loan
        if (bigTerm == 0) { //if the term is zero, the user can withdraw another loan
            int loan = 10000; //loan value is 10000
            money += loan; //loan is added to money total
            bigLoan = (loan + (loan*interest))/12; //monthly payment is calculated
            bigTerm = 12; //term length is set to 12 months
        }
    }
    
    public void mediumLoan () {//method for the medium loan
        if (mediumTerm == 0) { //if the term is zero, the user can withdraw another loan
            int loan = 5000; //loan value is 5000
            money += loan; //loan is added to money total
            mediumLoan = (loan + (loan*interest))/12;  //monthly payment is calculated
            mediumTerm = 12; //term length is set to 12 months
        
        }
    }
    
    public void smallLoan () { //method for the medium loan
        if (smallTerm == 0) { //if the term is zero, the user can withdraw another loan
            int loan = 1000; //loan value is 1000
            money += loan; //loan is added to money total
            smallLoan = (loan+ (loan*interest))/12; //monthly payment is calculated
            smallTerm = 12; //term length is set to 12 months
        
        }
    }
    
    public void monthEnd () { //method which calculates total money at months end by adding revenue, deducting expenses and loans
        System.out.println("ResidentialTaxes: " + residentialTaxes + ", Tax: " + tax + ", revenue: " + revenue + ", increase: " + ((revenue*tax) + (residentialTaxes * tax)) + ", Decrease: " + (expense + smallLoan + mediumLoan + bigLoan));
        money += (revenue*tax) + (residentialTaxes * tax) - expense - smallLoan - mediumLoan - bigLoan; 
        
        
        //if statements which reduce one month off of each term if there is more than 0 months remaining
        if (bigTerm > 0) {
            bigTerm -= 1;
        }
        
        if (mediumTerm > 0) {
            mediumTerm -= 1;
        }
        
        if (smallTerm > 0) {
            smallTerm -=1;
        }
        
        
        //if statements which get rid of loan payments if the term runs out
        if (bigTerm == 0) {
            bigLoan = 0.0;
        }
        
        if (mediumTerm == 0) {
            mediumLoan= 0.0;
        }
        
        if (smallTerm == 0) {
            smallLoan = 0.0;
        }
        
        if (money < 0) { //if money drops below 0l interest is added
            money -= money * 0.05;
        }
    }
 
    public String totalMoney () { //string method which prints out total money
       return moneyformat.format(money); 
    }
    public String toString () { //to string method
        String message = "";
        if (difficulty != 1) {
            message = "Total money: " + moneyformat.format(money) + "\n";   
        }
        else {
            message = "Total money: LOL"+ "\n"; 
        }
        message +=  "Current tax rate: " + moneyformat.format(tax) + "\n" 
                + "Current money being generated from taxes per month: " + moneyformat.format((revenue*tax) + (residentialTaxes * tax)) + "\n"
                + "Current revenue " + moneyformat.format(revenue) + "\n"
                + "Current montly loan payment: " + moneyformat.format((bigLoan + mediumLoan + smallLoan)) + "\n"
                + "Total loan payment outstanding: " + moneyformat.format((((bigLoan * bigTerm) + (mediumLoan * mediumTerm) + (smallLoan * smallTerm)))) + "\n"
                + "Total expenses: " + moneyformat.format(expense);

        return message;
                
    }
    
    
    

}

