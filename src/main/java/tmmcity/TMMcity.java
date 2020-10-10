/*
 * Matthew Reid
 * December 21,2018 
 * Main class that runs on start and initializes the game
 */
package tmmcity;

//Imports from Slick2D library
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

//Class TMMcity extends StateBasedGame from Slick2D library to allow menu to function
public class TMMcity extends StateBasedGame {

    //Game name and state identifiers
    public static final String gamename = "TMM City";
    public static final int splash = 0;
    public static final int menu = 1;
    public static final int game = 2;
    public static final int credit =3;
    public static final int high = 4;
    public static final int settings = 5;
    
    //Game properites
    public static final int width = 1680;
    public static final int height = 1080;
    
    //Double for version number (Final Release 1.0)
    public static final double version = 1.0;
    
    //Class constructor for TMMcity game
    public TMMcity(String gamename)
    {
        super(gamename);
        this.addState(new TMMsplash(splash));
        this.addState(new TMMmenu(menu));
        this.addState(new TMMgame(game));
        this.addState(new TMMcredits(credit));
        this.addState(new TMMhighscore(high));
        this.addState(new TMMsettings(settings));
    }
    
    //Initialize game states (calls init function in all game states)
    public void initStatesList(GameContainer gc) throws SlickException
    {
        this.getState(splash).init(gc,this);
        this.getState(menu).init(gc, this);
        this.getState(game).init(gc, this);
        this.getState(credit).init(gc,this);
        this.getState(high).init(gc,this);
        this.getState(settings).init(gc,this);
        //Start by entering the splash screen state
        this.enterState(splash);
    }    
    
    //Main mehtod
    public static void main(String[] args)
    {
        //Declare app container
        AppGameContainer appgc;
        
        //Try
        try
        {
            //Make a new AGC with gamename and version number
            appgc = new AppGameContainer(new TMMcity(gamename + " " + version));
            //Set display mode to correct ascept ratio, false fullscreen
            appgc.setDisplayMode (width, height, false);
            //Disable FPS display
            appgc.setShowFPS(false);
            //Start the game container
            appgc.start();
        }
        //Catch any exceptions
        catch(SlickException e)
        {
            //Print the exception
            e.printStackTrace();
        }
    }
    
}