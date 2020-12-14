/*
 * Matthew Reid and Madhu Sivapragasam
 * January 16, 2018
 * Settings game state allows user to choose difficulty of game
 */
package tmmcity;

//Required Imports
import java.io.InputStream;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;
import org.newdawn.slick.state.transition.*;
import org.newdawn.slick.util.ResourceLoader;

//Public class TMMSetting extends BasicGameState from the slick library
public class TMMsettings extends BasicGameState {

    // Constructor for game state
    public TMMsettings() {
    }

    // Declare image
    private Image settingsGraphic;

    // Declare font
    private TrueTypeFont font;

    // Declare attributes
    private int xpos, ypos, mouseCooldownTime;
    private static int difficulty; // declares difficulty setting
    private static boolean settingsChange; // declares difficulty boolean which is true when settings have been changed

    // Initializer for game state
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {

        // Try to init font
        try {
            // Gets font .tff file as input stream
            InputStream inputStream = ResourceLoader.getResourceAsStream("tmmcity\\Adore64.ttf");

            java.awt.Font awtFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, inputStream);
            awtFont = awtFont.deriveFont(42f); // set font size
            font = new TrueTypeFont(awtFont, false);

        } catch (Exception e) { // Catch any exceptions and print stack trace
            e.printStackTrace();
        }
        // Set dificulty to Normal (3)
        difficulty = 3;
        settingsChange = false;

        settingsGraphic = new Image("tmmcity\\Settings.png");

    }

    // Renders game state graphics
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

        // Settings
        g.drawImage(settingsGraphic, 0, 0);
        g.setFont(font);

        switch (difficulty) {
            case 1:
                g.drawString("Sandbox", 700, 933);
                break;
            case 2:
                g.drawString("Easy", 700, 933);
                break;
            case 4:
                g.drawString("Hard", 700, 933);
                break;
            default:
                g.drawString("Normal", 700, 933);
                break;
        }

    }

    // Updates game state
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {

        Input input = gc.getInput();
        xpos = Mouse.getX();
        ypos = Mouse.getY();

        // cooldowntime reduces the spam created by running mouseClick many times a
        // second while the mouse is down
        // Subtract delta from mouse cooldown time (delta is time in ms since last
        // update loop)
        mouseCooldownTime -= delta;
        // If the mouse is clicked and the cooldown is less than or equal to 0
        if (input.isMouseButtonDown(0) && mouseCooldownTime <= 0) {
            // Call mouse click method
            mouseClick(sbg);
            // Set mouse cooldown time to 150
            mouseCooldownTime = 150;
        }

    }

    // Gets the ID of the game state
    public int getID() {
        return 5;
    }

    // Method for mouse click inputs
    public void mouseClick(StateBasedGame sbg) throws SlickException {
        if (ypos < 940 && ypos > 245) {
            // SANDBOX BTN
            if (xpos < 420 && xpos > 37) {
                // Set difficulty to 1
                difficulty = 1;
                // Setting change is true
                settingsChange = true;
            }

            // EASY BTN
            else if (xpos < 824 && xpos > 443) {
                // Set difficulty to 2
                difficulty = 2;
                // Setting change is true
                settingsChange = true;
            }

            // MEDIUM BTN
            else if (xpos < 1232 && xpos > 850) {
                // Set difficulty to 3
                difficulty = 3;
                // Setting change is true
                settingsChange = true;
            }

            // HARD BTN
            else if (xpos < 1639 && xpos > 1257) {
                // Set difficulty to 4
                difficulty = 4;
                // Setting change is true
                settingsChange = true;
            }
        }

        // MAIN MENU BUTTON
        if ((ypos < 170 && ypos > 90) && (xpos < 1540 && xpos > 1130)) {
            sbg.enterState(1, new FadeOutTransition(), new FadeInTransition());
        }
    }

    // Getter for difficulty
    public static int getDifficulty() {
        return difficulty;
    }

    // Getter for settings chage
    public static boolean getSettingsChange() {
        return settingsChange;
    }

    // Setter for setting change
    public static void setSettingsChange(boolean s) {
        settingsChange = s;
    }

}