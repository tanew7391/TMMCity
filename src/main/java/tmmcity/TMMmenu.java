/*
 * Matthew Reid
 * December 21, 2018
 * Main Menu game state allows user to start new game, continue game, view settings,
 * view highscores, view credits, or exit the game
 */
package tmmcity;

//Required Imports
import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;
import org.newdawn.slick.state.transition.*;
        
//TMMmenu class extends BasicGameState from Slick2D library
public class TMMmenu extends BasicGameState {
    
    //Boolean to track if it is the first game
    public static boolean firstGame;
    
    //Declare images
    private Image intrographic, continueButton, confirmNewGame;
    
    //Declare boolean for new game button being pressed
    private boolean newGamePressed;
    
    //Declare integers for mouse xpos and ypos
    private int xpos, ypos;
    
    //Declare mouse input tracker
    private Input input;
    
    //Constructor for menu state
    public TMMmenu (int state){
    }
    
    //Initializes the menu state
    //sbg is the base game
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException{  
        
        //Create new image objects
        intrographic = new Image("tmmcity\\mainMenu.png");
        continueButton = new Image("tmmcity\\continueButton.png");
        confirmNewGame= new Image ("tmmcity\\newGameConfirm.png");
        
        //Set first game to true
        firstGame = true;
        
        //Set new game pressed to false
        newGamePressed = false;
    }
    
    //Renders the menu state
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException{
        
        //Draw the menu backgroud graphic at 0,0
        g.drawImage(intrographic, 0, 0);
        
        //If first game is false, darkens continue button as it now can be used
        if(!firstGame)
        {
            g.drawImage(continueButton,0,0);
        }
        
        //If new game pressed is true
        if (newGamePressed)
        {
            //Dispaly confirmation graphic
            g.drawImage(confirmNewGame,0,0);
        }
    }
    
    //Updates menu state
    public void update (GameContainer gc, StateBasedGame sbg, int delta) throws SlickException{
        
        //Variables to get mouse input
            input = gc.getInput();
            xpos = Mouse.getX();
            ypos = Mouse.getY();

        
        if (!newGamePressed)
        {
            if(xpos>420 && xpos<1260) 
            {
                //If the mouse is on the Start Game button
                if (ypos>620 && ypos<715)
                {
                    //If the mouse is clicked
                    if (input.isMouseButtonDown(0)) 
                    {
                        //Reinitialize the game state
                            sbg.getState(TMMcity.game).init(gc, sbg);
                            //Enter the game state
                            sbg.enterState(TMMcity.game,new FadeOutTransition(), new FadeInTransition());
                            TMMsettings.setSettingsChange(false);
                            firstGame = false;
                    }
                }

                //If the mouse is on the Continue Button
                else if (ypos>485 && ypos<580)
                {
                    //If the mouse is clicked
                    if (input.isMouseButtonDown(0)) 
                    {
                        //If it is not the first game
                        if (!firstGame)
                        {
                            //Enter the game state
                            sbg.enterState(2, new FadeOutTransition(), new FadeInTransition());
                        }
                    }
                }
            }

            if (ypos>350&&ypos<445)
            {
            //If the mouse is on the settings button
                if (xpos>420&&xpos<823)
                {
                    //If mouse is clicked
                    if (input.isMouseButtonDown(0)) 
                    {
                        sbg.enterState(5, new FadeOutTransition(), new FadeInTransition());
                    }
                }
                //If the mouse is on the high score button
                else if (xpos>855&&xpos<1260)
                 {
                    if (input.isMouseButtonDown(0)) 
                    {
                        //Reinitialize the game state
                            sbg.getState(TMMcity.high).init(gc, sbg);
                            //Enter the game state
                            sbg.enterState(TMMcity.high,new FadeOutTransition(), new FadeInTransition());
                    }
                }

            }

            if (ypos>215&&ypos<310)
            {
                //If the mouse is on the Credits Button
                if (xpos>420 && xpos<823)
                {
                    //If the mouse is clicked
                    if (input.isMouseButtonDown(0)) 
                    {
                        //Enter the credits game state
                        sbg.enterState(3, new EmptyTransition(), new RotateTransition());
                    }
                }

                //If the mouse is on the exit game button
                else if (xpos>855 && xpos<1260)
                {
                    //If the mouse is clicked
                    if (input.isMouseButtonDown(0)) 
                    {   
                        //Exit the game
                        System.exit(0);
                    }
                }
            } 
        }
            
        //If new game pressed is true
        else
        {
            //If the mouse is on the yes or no button and is clicked
            if (ypos>445 && ypos<530)
            {
                if (input.isMouseButtonDown(0))
                {
                    //If the mouse is on the no button
                    if (xpos<1255&&xpos>874)
                    {
                        //Set newgame pressed to false
                        newGamePressed=false;
                    }
                    
                    //Otherwise, if the mouse is on the yes button
                    else if (xpos<800&&xpos>419)
                    {
                        //Call write highscore from TMMgame
                        TMMgame.writeHighScore();
                        
                        //Reinitialize the game state
                        sbg.getState(TMMcity.game).init(gc, sbg);
                        //Enter the game state
                        sbg.enterState(TMMcity.game,new FadeOutTransition(), new FadeInTransition());
                        TMMsettings.setSettingsChange(false);
                        //Set firstGame to false
                        firstGame = false;
                        //Set newGamePressed to false
                        newGamePressed=false;
                    }        
                }
            }
        }
        
    } 
    
    //Setter for firstGame
    public static void setFirstGame (boolean f) {
        firstGame = f;
    }
    
    //Gets ID of the menu state
    public int getID()
    {
        return 1;
    }
}