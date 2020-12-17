/*
 * Matthew Reid
 * December 21,2018
 * Main class that runs on start and initializes the game
 */
package tmmcity;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

//Class TMMcity extends StateBasedGame from Slick2D library to allow menu to function
public class TMMcity extends StateBasedGame {

    //Game name and state identifiers
    public static final String gamename = "TMM City";
    public static final int splash = 0;
    public static final int menu = 1;
    public static final int game = 2;
    public static final int credits = 3;
    public static final int high = 4;
    public static final int settings = 5;

    //Double for version number (Final Release 1.0): extented release = 1.01
    public static final double version = 1.01;

    
    //Game properites
    private int width;
    private int height;
    
    private TMMsplash splashBaseGameState;
    private TMMmenu menuBaseGameState;
    private TMMgame gameBaseGameState;
    private TMMcredits creditsBaseGameState;
    private TMMhighscore highscoreBaseGameState;
    private TMMsettings settingsBaseGameState;


    //Class constructor for TMMcity game
    public TMMcity(String gamename)
    {
        super(gamename);
        width = 1680;
        height = 1050;
        initStates(); //must call before adding states
        addStates();
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int newWidth) {
		width = newWidth;
    }

    public int getHeight() {
		return height;
	}

    public void setHeight(int newHeight) {
		height = newHeight;
    }

    public TMMmenu getMenuBaseGameState() {
        return this.menuBaseGameState;
    }

    public void setMenuBaseGameState(TMMmenu menuBaseGameState) {
        this.menuBaseGameState = menuBaseGameState;
    }

    private void initStates(){
        splashBaseGameState = new TMMsplash();
        menuBaseGameState = new TMMmenu();
        gameBaseGameState = new TMMgame(this);
        creditsBaseGameState = new TMMcredits();
        highscoreBaseGameState = new TMMhighscore();
        settingsBaseGameState = new TMMsettings();
    }

    private void addStates(){
        this.addState(splashBaseGameState);
        this.addState(menuBaseGameState);
        this.addState(gameBaseGameState);
        this.addState(creditsBaseGameState);
        this.addState(highscoreBaseGameState);
        this.addState(settingsBaseGameState);
    }

    //Initialize game states (calls init function in all game states)
    public void initStatesList(GameContainer gc) throws SlickException {
        this.getState(splash).init(gc,this);
        this.getState(menu).init(gc, this);
        this.getState(game).init(gc, this);
        this.getState(credits).init(gc,this);
        this.getState(high).init(gc,this);
        this.getState(settings).init(gc,this);
        //Start by entering the splash screen state
        this.enterState(splash);
    }

    //Main mehtod
    public static void main(String[] args) {
        //Declare app container
        AppGameContainer appgc;
        TMMcity gameInstance = new TMMcity(gamename + " " + version);
        try
        {
            //Make a new AGC with gamename and version number
            appgc = new AppGameContainer(gameInstance);
            //gameInstance.setHeight(appgc.getScreenHeight());
            //gameInstance.setWidth(appgc.getScreenWidth());
            appgc.setVSync(true);
            //Set display mode to correct ascept ratio, false fullscreen
            appgc.setDisplayMode (gameInstance.getWidth(), gameInstance.getHeight(), false);
            //Disable FPS display
            appgc.setShowFPS(true);
            //Start the game container
            appgc.start();
        }
        catch(SlickException e)
        {
            e.printStackTrace();
        }
    }

}