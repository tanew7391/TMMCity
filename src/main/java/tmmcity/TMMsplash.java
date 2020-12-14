/*
 * Matthew Reid
 * December 21, 2018
 * Game state for the splash screen which displays group logo on start
 */
package tmmcity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.state.transition.HorizontalSplitTransition;

//TMMgame class extends BasicGameState from Slick2D library
public class TMMsplash extends BasicGameState {

    // Declare int for elapsed time
    int timeElapsed = 0;

    // Constructor for game state
    public TMMsplash() {
        super();
    }

    // Initializer for game state
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {

    }

    // Renders splash screen state graphics
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        // Draws the splash screen graphic
        g.drawImage(new Image("tmmcity\\splashLogo.png"), 0, 0);//TODO: move this into resources, and replace string with constant of file location.
    }

    // Updates game state
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {

        // Adds delta to time elapsed(delta is milliseconds since last update)
        timeElapsed += delta;

        // When timeElapsed is 2000ms or 2 seconds,
        if (timeElapsed > 2000) {
            // Enter the menu state with split transition
            sbg.enterState(TMMcity.menu, new EmptyTransition(), new HorizontalSplitTransition());
        }

    }

    // Gets the ID of the game state
    public int getID() {
        return 0;
    }
}