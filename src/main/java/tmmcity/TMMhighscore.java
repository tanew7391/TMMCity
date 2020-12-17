/*
 * Matthew Reid
 * January 10, 2018
 * Highscore game state allows user to view highscores from Highscore.txt
 */
package tmmcity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.Input;
import org.newdawn.slick.util.ResourceLoader;

public class TMMhighscore extends BasicGameState {

    // Constructor for game state
    public TMMhighscore() {
    }

    // Declare font
    TrueTypeFont font;

    String highScores;
    String[] tempHighScore;

    // Initializer for game state
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {

        // Try to init font
        try {
            InputStream inputStream = ResourceLoader.getResourceAsStream("tmmcity\\Adore64.ttf");

            java.awt.Font awtFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, inputStream);
            awtFont = awtFont.deriveFont(35f); // set font size
            font = new TrueTypeFont(awtFont, false);

        } catch (Exception e) { // Catch any exceptions and print stack trace
            e.printStackTrace();
        }

        boolean endOfFile = false;
        String sHs = "";
        highScores = "";
        tempHighScore = new String[10];
        int loopCount = 0;

        // Initiate file reader and buffered readers to read the txt file
        try {
            // Declare file and buffered reader to read highscore txt file
            FileInputStream in = new FileInputStream(System.getProperty("user.dir") + "/highscores/Highscores.txt");
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(isr);

            // loop until we reach the end of the file
            while (!endOfFile) {
                // read one item
                sHs = br.readLine();

                // if the item doesnt exist
                if (sHs == null) {
                    // Set end of file to true
                    endOfFile = true;
                }
                // Otherwise
                else {
                    if (loopCount < tempHighScore.length) {
                        highScores += sHs + "\n";
                        tempHighScore[loopCount] = sHs;
                        loopCount++;
                    }

                }
            }
            // Close the file connection
            br.close();

        } catch (Exception e) { // Catch any exceptions and print stack trace
            e.printStackTrace();
        }
    }

    // Renders game state graphics
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

        // Set the font as font
        g.setFont(font);
        // For the length of the high score arraylist
        for (int i = 0; i < tempHighScore.length; i++) {
            // If i is not 9,
            if (i != 9) {
                g.drawString((i + 1) + ".  " + tempHighScore[i], 10, 250 + (i * 50));
            }
            // Otherwise when i is 10, delete a space so that spacing remains consistent
            else {
                g.drawString((i + 1) + ". " + tempHighScore[i], 10, 250 + (i * 50));
            }
        }

        // Draw string
        g.drawString("TMM CITY HIGH SCORES - Right click to go back", 10, 175);

    }

    // Updates game state
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {

        // Input tracker to get mouse input
        Input input = gc.getInput();

        // If the mouse is right clicked
        if (input.isMouseButtonDown(1)) {
            // Enter menu state
            sbg.enterState(1, new FadeOutTransition(), new FadeInTransition());
        }
    }

    // Gets the ID of the game state
    public int getID() {
        return 4;
    }
}
