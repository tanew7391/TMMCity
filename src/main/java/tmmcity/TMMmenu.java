/*
 * Matthew Reid
 * December 21, 2018
 * Main Menu game state allows user to start new game, continue game, view settings,
 * view highscores, view credits, or exit the game
 */
package tmmcity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.state.transition.RotateTransition;
import org.newdawn.slick.Input;

//Required Imports
/*
import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;
import org.newdawn.slick.state.transition.*;
*/

//TMMmenu class extends BasicGameState from Slick2D library
public class TMMmenu extends BasicGameState {

    // Boolean to track if it is the first game

    // Declare images
    private Image intrographic;
    private Image continueButton;
    private Image confirmNewGame;

    // Declare boolean for new game button being pressed
    private boolean confirmResetGame;
    private boolean continueGamePressed;
    private boolean settingsPressed;
    private boolean highScorePressed;
    private boolean creditsPressed;
    private boolean exitGamePressed;
    private boolean firstGame;
    private boolean yesPressed;
    private boolean noPressed;
    private boolean startGame;

    private static int[] xBounds = {422, 823, 854, 1257};
    private static int[] yBounds = {363, 457, 501, 595, 635, 730, 770, 862};
    private static int[] confirmMenuXBounds = {420, 801, 874, 1256};
    private static int[] confirmMenuYBounds = {551, 633};

    // Constructor for menu state
    public TMMmenu() {

    }

    // Initializes the menu state
    // sbg is the base game
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {

        // Create new image objects
        intrographic = new Image("tmmcity\\mainMenu.png");
        continueButton = new Image("tmmcity\\continueButton.png");
        confirmNewGame = new Image("tmmcity\\newGameConfirm.png");

        // Set first game to true
        firstGame = true;

        // Set new game pressed to false
        confirmResetGame = false;
    }

    // Renders the menu state
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

        // Draw the menu backgroud graphic at 0,0
        g.drawImage(intrographic, 0, 0);

        // If first game is false, darkens continue button as it now can be used
        if (!firstGame) {
            g.drawImage(continueButton, 0, 0);
        }

        // If new game pressed is true
        if (confirmResetGame) {
            // Dispaly confirmation graphic
            g.drawImage(confirmNewGame, 0, 0);
        }
    }

    @Override
    public void mousePressed(int button, int xpos, int ypos) {
        System.out.println(xpos + " " + ypos);
        if (button == Input.MOUSE_LEFT_BUTTON) {
            if (!confirmResetGame) {
                if (xpos > xBounds[0] && xpos < xBounds[3]) {
                    // If the mouse is on the Start Game button
                    if (ypos > yBounds[0] && ypos < yBounds[1]) {
                        // If the mouse is clicked
                        if (firstGame == true) {
                            startGame = true;
                            firstGame = false;
                        } else {
                            confirmResetGame = true;
                        }
                    }

                    // Continue Button
                    else if (ypos > yBounds[2] && ypos < yBounds[3]) {
                        // If it is not the first game
                        if (!firstGame) {
                            // Enter the game state
                            continueGamePressed = true;
                        }
                    }
                }

                if (ypos > yBounds[4] && ypos < yBounds[5]) {
                    // If the mouse is on the settings button
                    if (xpos > xBounds[0] && xpos < xBounds[1]) {
                        // If mouse is clicked
                        settingsPressed = true;
                    }
                    // If the mouse is on the high score button
                    else if (xpos > xBounds[2] && xpos < xBounds[3]) {
                        highScorePressed = true;
                    }

                }

                if (ypos > yBounds[6] && ypos < yBounds[7]) {
                    // If the mouse is on the Credits Button
                    if (xpos > xBounds[0] && xpos < xBounds[1]) {
                        // If the mouse is clicked
                        // Enter the credits game state
                        creditsPressed = true;
                    }

                    // If the mouse is on the exit game button
                    else if (xpos > xBounds[2] && xpos < xBounds[3]) {
                        // If the mouse is clicked
                        // Exit the game
                        System.exit(0);
                    }
                }
            }

            // If new game pressed is true
            else {
                // If the mouse is on the yes or no button and is clicked
                if (ypos > confirmMenuYBounds[0] && ypos < confirmMenuYBounds[1]) {
                    // If the mouse is on the no button
                    if (xpos > confirmMenuXBounds[2] && xpos < confirmMenuXBounds[3]) {
                        // Set newgame pressed to false
                        confirmResetGame = false;
                    }

                    // Otherwise, if the mouse is on the yes button
                    else if (xpos > confirmMenuXBounds[0] && xpos < confirmMenuXBounds[1]) {
                        yesPressed = true;
                        // Set firstGame to false
                        firstGame = false;
                        // Set newGamePressed to false
                        confirmResetGame = false;
                    }
                }
            }
        }
    }

    // Updates menu state
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        if (startGame) {
            sbg.getState(TMMcity.game).init(gc, sbg); // Reinitialize the game state
            sbg.enterState(TMMcity.game, new FadeOutTransition(), new FadeInTransition()); // Enter the game state
            TMMsettings.setSettingsChange(false);
            startGame = false;
        } else if (continueGamePressed) {
            sbg.enterState(TMMcity.game, new FadeOutTransition(), new FadeInTransition());
            continueGamePressed = false;
        } else if (settingsPressed) {
            sbg.enterState(TMMcity.settings, new FadeOutTransition(), new FadeInTransition());
            settingsPressed = false;
        } else if (highScorePressed) {
            // Reinitialize the game state
            sbg.getState(TMMcity.high).init(gc, sbg);
            // Enter the game state
            sbg.enterState(TMMcity.high, new FadeOutTransition(), new FadeInTransition());
            highScorePressed = false;
        } else if (creditsPressed) {
            sbg.enterState(TMMcity.credits, new EmptyTransition(), new RotateTransition());
            creditsPressed = false;
        } else if (exitGamePressed) {
            exitGamePressed = false;
        } else if (yesPressed) {
            // Call write highscore from TMMgame
            TMMgame.writeHighScore();
            // Reinitialize the game state
            sbg.getState(TMMcity.game).init(gc, sbg);
            // Enter the game state
            sbg.enterState(TMMcity.game, new FadeOutTransition(), new FadeInTransition());
            TMMsettings.setSettingsChange(false);
            yesPressed = false;
        } else if (noPressed) {
            noPressed = false;
        }
    }

    // Setter for firstGame
    public void setFirstGame(boolean f) {
        firstGame = f;
    }

    // Gets ID of the menu state
    public int getID() {
        return 1;
    }
}