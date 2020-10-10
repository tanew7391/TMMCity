/*
 * Taylor Newman
 * Dec 21
 * Abstract class that every zone type object extends from. Every placeable object has its own class which extends ZoneType. Implements ZoneInterface
 */
package tmmcity;

import org.newdawn.slick.Image; //because one of the attributes of every zone is a slick2d image, we must import the image library

/**
 *
 * @author Taylor
 */
abstract public class ZoneType implements ZoneInterface { 

    private Image tileImg;
    private int xGrid;
    private int yGrid;
    private double attraction;

    public ZoneType(Image tileImg, int xGrid, int yGrid , int attraction) {
        this.attraction = attraction;
        this.tileImg = tileImg;
        this.xGrid = xGrid;
        this.yGrid = yGrid;
    }

    @Override
    public int getXGrid() {
        return xGrid;
    }

    @Override
    public void setXGrid(int xGrid) {
        this.xGrid = xGrid;
    }

    @Override
    public int getYGrid() {
        return yGrid;
    }

    @Override
    public void setYGrid(int yGrid) {
        this.yGrid = yGrid;
    }

    @Override
    public Image getImage() {
        return this.tileImg;
    }

    @Override
    public void setImage(Image tileImg) {
        this.tileImg = tileImg;
    }
    
    @Override
    public double getAttraction(){
    return attraction;
    }
    
    @Override
    public void setAttraction(double a){
    this.attraction = a;
    }
    
    public static int randomIntBetween(int a, int b){  //random integer command which is called many times throughout all of the subclasses which extend ZoneType. The max int is A and the min is B        //Taylor
    return (int) (Math.random()*((a+1)-b))+b;
    }

    @Override
    public String toString() {
        return "\nX Position = " + xGrid + " Y Position = " + yGrid + "\nAttraction = " + attraction;
    }
    


}
