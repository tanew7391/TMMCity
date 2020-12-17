/*
 * Taylor Newman
 * Dec 21
 * The ZoneInterface interface provides a structure for the ZoneType objects to follow so that the game can build and place all zone objects the same.
 */
package tmmcity;

import org.newdawn.slick.Image;

/**
 *
 * @author Taylor
 */
public interface ZoneInterface {
    public int getXGrid();

    public int getYGrid();

    public void setXGrid(int x);

    public void setYGrid(int y);

    public Image getImage();

    public void setImage(Image img);

    public double getAttraction();

    public void setAttraction(double a);

    @Override
    public String toString();

}
