/*
 * Matthew Reid
 * January 2, 2018
 * Game state for credits screen diplays credits as text
 */
package tmmcity;

//Requried input
import java.io.InputStream;
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
import java.awt.Font;

public class TMMcredits extends BasicGameState {

    // Constructor for game state
    public TMMcredits() {
    }

    private TrueTypeFont font;
    private TrueTypeFont fontSmall;
    private Font awtFont;
    private boolean goBack;

    // Initializer for game state
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        // Try to init font
        try {
            // Get font .tff as input stream
            InputStream inputStream = ResourceLoader.getResourceAsStream("tmmcity\\Adore64.ttf");

            // Sets font as awt font
            awtFont = java.awt.Font.createFont(Font.TRUETYPE_FONT, inputStream);
            // Derive awtFont for large font
            awtFont = awtFont.deriveFont(35f); // set font size
            // Set font as awtfont
            font = new TrueTypeFont(awtFont, false);
            // Derive awtfont for smaller font
            awtFont = awtFont.deriveFont(20f); // set font size
            // Set fontsmall as new awtfont
            fontSmall = new TrueTypeFont(awtFont, false);

        }
        // Catch any exceptions and print stack trace
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        goBack = false;
    }

    // Renders credit text
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

        // Set font to large
        g.setFont(font);

        // Draw strings for credits
        g.drawString("TMM CITY CREDITS", 10, 175);
        g.drawString("Project Manager: Matt Reid", 10, 225);
        g.drawString("Lead Programmer: Taylor Newman", 10, 275);
        g.drawString("Systems Analyst: Madhu Sivapragasam", 10, 325);
        g.drawString("Technical Writer: Madhu Sivapragasam", 10, 375);
        g.drawString("Programmers: Matt Reid and Madhu Sivapragasam", 10, 425);
        g.drawString("Graphical Design: Matt Reid and Taylor Newman", 10, 475);
        g.drawString("Teacher: Brad Cutten", 10, 525);
        g.drawString("Made for the ICS4U Final Project", 10, 625);
        g.drawString("BY TMM GAMES", 10, 675);

        // Set font to small
        g.setFont(fontSmall);

        // Draw strings for credits
        g.drawString("Icons made by https://www.freepik.com/" + "\nFreepik from www.flaticon.com", 10, 775);
        g.drawString("Icons made by https://www.flaticon.com/authors/eucalyp " + "from www.flaticon.com", 10, 800);

        // Set font to large
        g.setFont(font);

        // Draw string for credits
        g.drawString("Right click to go back", 10, 900);
    }

    @Override
    public void mousePressed(int button, int xpos, int ypos) {
        System.out.println("Here! " + button);
        if (button == Input.MOUSE_RIGHT_BUTTON) {
            goBack = true;
        }
    }

    // Updates mouse input tracking
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        if (goBack) {
            // Enter menu state
            sbg.enterState(TMMcity.menu, new FadeOutTransition(), new FadeInTransition());
        }
    }

    // Gets the ID of the game state
    public int getID() {
        return 3;
    }
}
