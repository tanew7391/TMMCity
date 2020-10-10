/*
 * Matthew Reid
 * December 21, 2018
 * Game state for the splash screen which displays group logo on start
 */
package tmmcity;

//Required imports
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;
import org.newdawn.slick.state.transition.*;
        
//TMMgame class extends BasicGameState from Slick2D library
public class TMMsplash extends BasicGameState {
    
    //Declare int for elapsed time
    int timeElapsed = 0;
    
    //Constructor for game state
    public TMMsplash(int state){
    }
    
    //Initializer for game state
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException{
       
    }
    
    //Renders splash screen state graphics
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException{
        
         //Declare new image object for the graphic
        Image splashgraphic = new Image("tmmcity\\splashLogo.png");
        
        //Draws the splash screen graphic
        g.drawImage(splashgraphic, 0, 0);
    }
    
    //Updates game state
    public void update (GameContainer gc, StateBasedGame sbg, int delta) throws SlickException{
        
        //Adds delta to time elapsed(delta is milliseconds since last update)
        timeElapsed+=delta;
        
        //When timeElapsed is 2000ms or 2 seconds,
        if (timeElapsed>2000)
        {
            //Enter the menu state with split transition
            sbg.enterState(1, new EmptyTransition(), new HorizontalSplitTransition());
        }
        
    }
    
    //Gets the ID of the game state
    public int getID()
    {
        return 0;
    }
}