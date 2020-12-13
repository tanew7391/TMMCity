/*
 * Matthew Reid, Taylor Newman, Madhu Sivapragasam
 * December 21, 2018
 * Game state for game screen runs the actual game
 * Holds many of the main methods, and integrates all zone classes to create the game gui
 */
package tmmcity;

//Required imports
import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;
import java.awt.Font;
import java.io.*;
import java.util.ArrayList;
import org.newdawn.slick.state.transition.*;
import org.newdawn.slick.util.ResourceLoader;
import static tmmcity.ZoneType.randomIntBetween;

//TMMgame class extends BasicGameState from Slick2D library
public class TMMgame extends BasicGameState {

    // Constructor for game state
    public TMMgame(int state) {
    }

    private static final int gridSpacing = 50; // grid spacing in pixels, set to 50, if need to change, many dimensions
                                               // need to change

    // Declare variables
    private static String infoLine1, infoLine2, infoLine2Default; // Information strings

    private int mousey;
    private int xpos;
    private int ypos;
    private int gridX;
    private int gridY;
    private int buildCost;
    private int buildType;
    private int mouseCooldownTime;
    private int updateSpeed;
    private int clockUpdateTime;
    private int recAttractionMax;
    private int infoCounter;
    private int info2Counter;
    private int hospitalBuildCost;
    private int policeBuildCost;
    private int endGameCooldownTime; // Integers

    private boolean inGrid;
    private boolean buildItemSelected;
    private boolean firstRoad;
    private boolean displayLoan;
    private boolean displayTax;
    private boolean displayEndGameConfirm;
    private boolean displayGameLossP;
    private boolean displayGameLossD;
    private boolean displayGameWin; // Booleans

    private Input input; // Input tracking object
    private static ZoneType[][] gameGrid; // 2d array for zonetypes on the grid
    private Image mouseImage;
    private Image resImg;
    private Image comImg;
    private Image indImg;
    private Image policeImg;
    private Image recImg;
    private Image roadImg;
    private Image hospitalImg;
    private Image electricImg;
    private Image waterImg;
    private Image recImg1;
    private Image recImg2;
    private Image bullDozerImg;
    private Image backgroundgraphic;
    private Image loanGraphic;
    private Image taxGraphic;
    private Image gameWin;
    private Image gameLossP;
    private Image gameLossD;
    private Image noPolice;
    private Image noRevenue;
    private Image noWater;
    private Image noElectricity;
    private Image noHospital;
    private Image endGameConfirm; // images
    private SpriteSheet icons; // Spritesheet with game icons
    private TrueTypeFont Adore64; // Font object
    private static Bank gameBank; // Bank object
    private ArrayList<int[]> gridStack, warningStack; // Arraylist for grid stack, and warning icons
    private static Clock gameClock; // Clock object
    private static int difficulty;
    private double score;

    // Initializer for game state
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {

        // game play goes from top right at (1680, 9) to bottom left at (229, 887)

        // reset all of the static variables
        ResidentialZone.setTotalJobsInUse(0); // reset population
        CommercialZone.setTotalJobsAvailable(0);
        CommercialZone.setTotalRevenueAvailable(0);
        CommercialZone.setTotalJobs(0);
        ArrayList<CommercialZone> commercialStack = new ArrayList<>();
        CommercialZone.setCommercialStack(commercialStack);
        IndustrialZone.setTotalCommercialAvailable(0);
        RecreationalZone.setExpense(0);
        GovernmentZone.setExpense(0);

        // Create new clock object
        gameClock = new Clock();

        // Create new gridStack array list
        gridStack = new ArrayList<>();
        warningStack = new ArrayList<>();

        // Initialize variables
        mouseCooldownTime = 0;
        clockUpdateTime = 0;
        infoCounter = 5000;
        info2Counter = 5000;
        updateSpeed = 25;
        hospitalBuildCost = 0;
        policeBuildCost = 0;
        endGameCooldownTime = 1000;

        // Create new Zone Type array
        gameGrid = new ZoneType[(int) ((1680 - 229) / gridSpacing)][(int) ((927 - 9) / gridSpacing)];

        // Create new game bank object
        gameBank = new Bank();

        setResources();
        setSprites();

        // Initialize strings for information output
        infoLine2Default = "Total industrial: " + IndustrialZone.getTotalCommercialAvailable() + "\nTotal Commercial: "
                + CommercialZone.getTotalJobs() + "\nJobs available for residents "
                + CommercialZone.getTotalJobsAvailable() + "\nJobs in use: " + ResidentialZone.getTotalJobsInUse();
        infoLine1 = "";
        infoLine2 = ""; // Taylor

        // Initialize booleans
        displayLoan = false;
        displayTax = false;
        inGrid = true;
        buildItemSelected = false;
        firstRoad = true;
        displayGameWin = false;
        displayGameLossD = false;
        displayGameLossP = false;

        // Try to initialize the font Adore64
        try {
            // Get font .tff as input stream
            InputStream inputStream = ResourceLoader.getResourceAsStream("tmmcity\\Adore64.ttf");

            // Set font as awtFont
            Font awtFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);

            // Derive the awtFont with size 20
            awtFont = awtFont.deriveFont(20f); // set Adore64 size

            // Create new Adore64 font object
            Adore64 = new TrueTypeFont(awtFont, false);

        } // Catch and print stack trace of exceptions
        catch (Exception e) {
            e.printStackTrace();
        }

        // set difficulty depending on settings choice
        difficulty = TMMsettings.getDifficulty();

        // Set gamebank starting money based on difficulty
        switch (difficulty) {
            case 1:
                gameBank.setMoney(100000.0);
                break;
            case 2:
                gameBank.setMoney(15000.0);
                break;
            case 3:
                gameBank.setMoney(10000.0);
                break;
            case 4:
                gameBank.setMoney(10000.0);
                break;
            default:
                break;
        }

        // Calculate gameBank interest based on difficulty
        gameBank.calculateInterest(difficulty);

    }

    /**
     * Get and declare icons from spritesheet
     * Icons made by https://www.flaticon.com/authors/smashicons Smashicons
     * Icon made by https://www.flaticon.com/authors/eucalyp Eucalyp
     * Icons made by https://www.freepik.com/ Freepik
     * www.flaticon.com
     */
    private void setSprites() {
        try {
            icons = new SpriteSheet("tmmcity\\zone\\spriteSheet.png", 50, 50);  // set tile size as 50x50, this is what they are rendered as in file
            resImg = icons.getSprite(0, 0);
            comImg = icons.getSprite(2, 2);
            indImg = icons.getSprite(0, 1);
            recImg = icons.getSprite(1, 0); 
            policeImg = icons.getSprite(0, 2);
            hospitalImg = icons.getSprite(2, 1);
            waterImg = icons.getSprite(1, 2);
            electricImg = icons.getSprite(1, 1);
            roadImg = icons.getSprite(2, 0);
            recImg1 = icons.getSprite(3, 0);
            recImg2 = icons.getSprite(3, 1);
            bullDozerImg = icons.getSprite(3, 2);
            noPolice = icons.getSprite(4, 0);
            noRevenue = icons.getSprite(4, 2);
            noWater = icons.getSprite(4, 3);
            noElectricity = icons.getSprite(0, 3);
            noHospital = icons.getSprite(1, 3);
        } catch (SlickException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Get background image GameBackground.png and create new image object
     */
    private void setResources() {
        try {
            backgroundgraphic = new Image("tmmcity\\gameBackground.png");
            loanGraphic = new Image("tmmcity\\loanButtons.png");
            taxGraphic = new Image("tmmcity\\taxButtons.png");
            gameWin = new Image("tmmcity\\TMMGameOverWin.png");
            gameLossD = new Image("tmmcity\\GameOverLossDebt.png");
            gameLossP = new Image("tmmcity\\GameOverLossPop.png");
            endGameConfirm = new Image("tmmcity\\endGameConfirm.png");
        } catch (SlickException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    //Renders game state
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

        //Draw background image at 0,0
        g.drawImage(backgroundgraphic, 0, 0);

        //Set graphics colour to white
        //Draw strings
        g.drawString(infoLine1, 600, 935);
        g.drawString(infoLine2, 1180, 945);

        //Draw the grid
        for (int i = 229; i < 1680; i = i + gridSpacing) { //start at 229, which is the left padding                                                                                        //Taylor
            g.drawLine(i, 9, i, 909);
        }
        for (int i = 9; i < 927; i = i + gridSpacing) {
            g.drawLine(229, i, 1680, i);
        }

        if (displayTax == true && infoLine1 == "") { //if the user wants to display taxes, make sure nothing is using infoLine1
            displayLoan = false;
            g.drawImage(taxGraphic, 0, 0);
        }

        //Set the font to Adore64
        g.setFont(Adore64);

        //Draw string reprentation of the clock date and time
        g.drawString(gameClock.toStringDate(), 40, 960);
        g.drawString(gameClock.toStringTime(), 90, 1020);

        //Draw total money in the game bank
        if (difficulty == 1) {
            g.drawString("LOL", 395, 1028); //if in sandbox mode, display LOL
        } else {
            g.drawString("" + gameBank.totalMoney(), 395, 1028);
        }

        //When loan button is pressed
        if (displayLoan == true && infoLine1 == "") {
            displayTax = false;
            //Buttons that allow users to withdraw loan
            g.drawImage(loanGraphic, 0, 0);
        }

        //Draw total residential population of the city
        g.drawString("" + ResidentialZone.getTotalJobsInUse(), 395, 950);

        //For the size of the grid stack array,
        for (int i = 0; i < gridStack.size(); i++) {
            g.drawImage(gameGrid[gridStack.get(i)[0]][gridStack.get(i)[1]].getImage().getScaledCopy(gridSpacing - 1, gridSpacing - 1), //draw the build objects to the screen
                    (gridStack.get(i)[0] * gridSpacing) + 230, (gridStack.get(i)[1] * gridSpacing) + 10); //this only needs to cycle through gridStack, not gameGrid, reducing resource usage           //Taylor** grid stuff
        }

        for (int i = 0; i < warningStack.size(); i++) { //loop through the warning stack
            Image tempImg;
            switch (warningStack.get(i)[2]) {
                case 0:
                    tempImg = noWater;
                    break;
                case 1:
                    tempImg = noElectricity;   //switch case to index the different warning types                                                                                              //Taylor
                    break;
                case 2:
                    tempImg = noHospital;
                    break;
                case 3:
                    tempImg = noPolice;
                    break;
                case 4:
                    tempImg = noRevenue;
                    break;
                default:
                    tempImg = noWater;
            }
            g.drawImage(tempImg.getScaledCopy(gridSpacing - 1, gridSpacing - 1), (warningStack.get(i)[0] * gridSpacing) + 230, (warningStack.get(i)[1] * gridSpacing) + 10); //draw image that is scaled down to 49x49, place it at it's grid position multiplied by the grid spacing and added with the left or bottom bumper
        }

        //If a build item is selected,
        if (buildItemSelected) {
            //Draw image of selcted build object on the mouse
            g.drawImage(mouseImage.getScaledCopy(gridSpacing, gridSpacing), xpos, mousey);                                                                                                        //Taylor
        }
        //If money is less than -5000 (bad ending)
        if (displayGameLossD) {
            //Draw debt game loss image
            g.drawImage(gameLossD, 500, 200);
            //Draw score
            score = calculateScore();
            g.drawString("" + score, 800, 610);
        }

        //If it has been a year and population is 0
        if (displayGameLossP) {
            //Draw pop game loss image
            g.drawImage(gameLossP, 500, 200);
            //Draw score
            score = calculateScore();
            g.drawString("" + score, 800, 610);
        }

        //If the year is 2000 (good ending)
        if (displayGameWin) {
            //Draw the game win image
            g.drawImage(gameWin, 500, 200);
            //Draw score
            score = calculateScore();
            g.drawString("" + score, 800, 605);
        }
        //If display end game is true
        if (displayEndGameConfirm) {
            //Draw the end game confirm image
            g.drawImage(endGameConfirm, 0, 0);

        }
    }

    //Updates game state
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {

        //Variables for tracking the mouse
        mousey = gc.getHeight() - Mouse.getY(); //grab the position at which objects are drawn at (different than where the mouse is located at)
        xpos = Mouse.getX(); //mouse x
        ypos = Mouse.getY();//mouse y
        input = gc.getInput(); //mouse click
        inGrid = true;

        //Variable to track grid x and y
        gridX = (int) (xpos - 229) / gridSpacing;
        gridY = (int) (mousey - 9) / gridSpacing;                                                                                                                                   //Taylor

        //tells the mouse listener whether the mouse is in the grid, and won't execute if the mouse is out of the grid
        if (gridY < 0 || gridY > 18 || gridX > 28 || gridX < 0 || xpos < 229
                || xpos > 1679 || mousey < 9 || mousey > 908) { //uses both grid position and mouse position                                                                        //Taylor
            inGrid = false;
        }

        //Add delta to time since last clock update (delta is milliseconds since update was last run)
        clockUpdateTime += delta;

        //cooldowntime reduces the spam created by running mouseClick many times a second while the mouse is down
        //Subtract delta from mouse cooldown time (delta is time in ms since last update loop)
        mouseCooldownTime -= delta;
        //If the mouse is clicked and the cooldown is less than or equal to 0
        if (input.isMouseButtonDown(0) && mouseCooldownTime <= 0 && !displayEndGameConfirm) {
            //Call mouse click method
            mouseClick(sbg);
            //Set mouse cooldown time to 150
            mouseCooldownTime = 150;
        }

        if (displayEndGameConfirm) { //when the end of the game has been reached
            //If the mouse is on the yes or no buttons and is clicked
            if (ypos > 445 && ypos < 530) 
            {
                if (input.isMouseButtonDown(0)) 
                {
                    //If it is on the no button
                    if (xpos < 1255 && xpos > 874) 
                    {
                        //Set display end game to false
                        displayEndGameConfirm = false;   
                    } 
                    
                    //Otherwise, if it is on the yes button 
                    else if (xpos < 800 && xpos > 419) 
                    { 
                        
                        //Write highscore
                        writeHighScore();
                        
                        //Set firstGame to true to disable continue button
                        TMMmenu.firstGame = true;
                        
                        //Enter menu state
                        sbg.enterState(1, new FadeOutTransition(),
                                new FadeInTransition());
                        
                        //Set a new mouse cooldown to stop accidently button press
                        mouseCooldownTime = 1500;
                        
                        //set display end game to false
                        displayEndGameConfirm = false;
                    }
                }
            }
        }

        //If right click is inputted (for right click to stop building)
        if (input.isMouseButtonDown(1)) {
            //Set build item selected to false                                                                                                          //Taylor
            buildItemSelected = false;
            //set info line default to proper amount
            infoLine2Default = "Total industrial: " + IndustrialZone.getTotalCommercialAvailable() + "\nTotal Commercial: " + CommercialZone.getTotalJobs() + "\nJobs available for residents " + CommercialZone.getTotalJobsAvailable() + "\nJobs in use: " + ResidentialZone.getTotalJobsInUse();
            //Clear the info string
            infoLine2 = infoLine2Default;
        }

        //If the update speed of the clock is not 0 or paused
        if (updateSpeed != 0) {

            //If the clock update time is greater than the speed the clock is set to update,
            if (clockUpdateTime > updateSpeed) {
                //Call updateClock method for the clock object
                gameClock.updateClock();
                //Reset update time counter to 0
                clockUpdateTime = 0;
            }
        }
        //While it is the first road,
        while (firstRoad) {                                                                                                                                 //Taylor
            //Build the starting road
            roadBuildItemSelected();
            buildItem(0, 9, buildType);
            firstRoad = false;
            buildItemSelected = false;
        }

        //If a build item is selected
        if (buildItemSelected) {
            //Display right click to stop building info
            infoLine2 = "Right click to\nstop building.";
        }

        //If infoCounter is 0 or less,
        if (infoCounter <= 0) {
            //Clear info line
            infoLine1 = "";
            //Reset counter to 5000ms
            infoCounter = 5000;
            //Set display loan and display tax to false
            displayLoan = false;
            displayTax = false;
        }
        
        //If infocounter 2 is 0 or less
        if (info2Counter <= 0) {
            //Set InfoLine2 to default
            infoLine2Default = 
            infoLine2 = infoLine2Default;
            //Reset infocounter 2 to 5000;
            info2Counter = 5000;
        }

        //If the infoline is not empty
        if (infoLine1 != "") {
            //Subtract delta from the info counter
            infoCounter -= delta;
        }

        if (infoLine2 != infoLine2Default) {
            info2Counter -= delta;
        }

        //If it is the end of the month,
        if (gameClock.getMonthEnd()) {
            //Call month end method
            monthEnd();
        }

        //If it is the end of the day
        if (gameClock.getDayEnd()) {
            //Call day end method
            dayEnd();
        }

        //If the year is 2000
        if (gameClock.getYear() == 2000) {
            //Set update speed to 0
            updateSpeed = 0;
            //Subtract delta from endgame cooldown
            endGameCooldownTime -= delta;
            //Set first game to true to disable continue button
            TMMmenu.firstGame = true;
            //set display game win graphic to true
            displayGameWin = true;

            //If the cooldown is less than 0
            if ((endGameCooldownTime <= 0) && (input.isMouseButtonDown(0))) {
                //Set display game win graphic to false
                displayGameWin = false;
                //Call writeHighScore;
                writeHighScore();
                //Enter the menu state
                sbg.enterState(1, new FadeOutTransition(), new FadeInTransition());  
            }

        }
        
        //If the gamebanks has less than -5000
        if (gameBank.getMoney() <= -5000) {
            //Set update speed to 0
            updateSpeed = 0;
            //Subtract delta from endgame cooldown
            endGameCooldownTime -= delta;
            //Set first game to true to disable continue button
            TMMmenu.firstGame = true;
            //set display debt game loss graphic to true
            displayGameLossD = true;

            //If the cooldown is less than 0
            if ((endGameCooldownTime <= 0) && (input.isMouseButtonDown(0))) {
                //Set display game debt loss graphic to false
                displayGameLossD = false;
                //Call writeHighScore;
                writeHighScore();
                //Enter the menu state
                sbg.enterState(1, new FadeOutTransition(), new FadeInTransition());  
            }
        }

        //If it has been 1 year and the population is still 0
        if ((gameClock.getYear() == 1951) && (ResidentialZone.getTotalJobsInUse() == 0)) {
            //Set update speed to 0
            updateSpeed = 0;
            //Subtract delta from endgame cooldown
            endGameCooldownTime -= delta;
            //Set first game to true to disable continue button
            TMMmenu.firstGame = true;
            //set display pop game loss graphic to true
            displayGameLossP = true;

            //If the cooldown is less than 0
            if ((endGameCooldownTime <= 0) && (input.isMouseButtonDown(0))) {
                //Set display game pop loss graphic to false
                displayGameLossP = false;
                //Call writeHighScore;
                writeHighScore();
                //Enter the menu state
                sbg.enterState(1, new FadeOutTransition(), new FadeInTransition());  
            }
        }

        if (difficulty == 1 && gameBank.getMoney() < 100000) { //this is for sandbox mode, if the unseen money value ever falls below 100000, just top it up againt
            gameBank.setMoney(gameBank.getMoney() + 100000);
        }

    }

    //Method for mouse click inputs
    void mouseClick(StateBasedGame sbg) throws SlickException {
        if ((xpos > 9) && (xpos < 106) && ypos > 407) {
            if ((ypos > 860) && (ypos < 957)) {
                residentBuildItemSelected();
            } else if ((ypos > 748) && (ypos < 846)) {
                industrialBuildItemSelected();
            } else if ((ypos > 636) && (ypos < 733)) {
                hospitalBuildItemSelected();
            } else if ((ypos > 521) && (ypos < 619)) {
                electricBuildItemSelected();
            } else if ((ypos > 407) && (ypos < 505)) {                                                                                                                              //Taylor
                roadBuildItemSelected();
            }
        } else if ((xpos > 119) && (xpos < 216) && ypos > 407) {  //need to put the ypos>407 so that buttons below the build area are available after the else if statement
            if ((ypos > 860) && (ypos < 957)) {
                commercialBuildItemSelected();
                //System.out.println("com");
            } else if ((ypos > 748) && (ypos < 846)) {
                policeBuildItemSelected();
                //System.out.println("gov");
            } else if ((ypos > 636) && (ypos < 733)) {
                waterBuildItemSelected();
                //System.out.println("water");
            } else if ((ypos > 512) && (ypos < 619)) {
                recreationalBuildItemSelected();
                //System.out.println("recreational");
            } else if ((ypos > 407) && (ypos < 505)) {
                removeBuildItemSelected();
                //System.out.println("road");
            }
        } else if (xpos > 14 && xpos < 214 && ypos > 169 && ypos < 219) {
            skipMonthEnd(); //skip to month end button
        } else if (inGrid && buildItemSelected) { //check if mouse is in the grid with a build object
            if (gameBank.getMoney() >= buildCost || buildType == 9) { //check if either the player has enough money to build, or they are destroying something
                if (gameGrid[gridX][gridY] == null && (roadCheck(gridX, gridY) || buildType == 9)) { //check if grid is empty, and the location is next to an existing road
                    if (buildType != 9) {
                        buildItem(gridX, gridY, buildType);
                    } else {
                        infoLine1 = "Nothing here to demolish!"; //if selected grid is null
                    }
                } else if (buildType == 9) {
                    if (!(gridX == 0 && gridY == 9)) {
                        buildItem(gridX, gridY, buildType); //if demolishing an object, build type is 9

                    } else {                                                                                                                            //Taylor
                        infoLine1 = "Cannot remove first road";  //error message for trying to demolish first road
                    }
                } else if (!roadCheck(gridX, gridY)) { //if no road nearby, and not demolishing, throw this error
                    infoLine1 = "There must be a road nearby to build here";

                } else { //if there is a road, the player is not demolishing, but the grid square is not null, then throw this error
                    //spot already taken error message
                    infoLine1 = "Grid square already occupied!";
                    System.out.println(infoLine1);
                }
            } else { //if game money is not larger than build cost
                //insufficient funds
                infoLine1 = "Insufficient funds!";
                System.out.println(infoLine1);
            }
        } else if (inGrid && gameGrid[gridX][gridY] != null) { //print the discription of the build type to the bottom box
            infoLine1 = gameGrid[gridX][gridY].toString();

            //MAIN MENU BUTTON
        } else if ((xpos > 1468 && xpos < 1670) && (ypos > 87 && ypos < 155)) {
            sbg.enterState(1, new FadeOutTransition(), new FadeInTransition());

            //END GAME BUTTON
        } else if ((xpos > 1468 && xpos < 1670) && (ypos > 10 && ypos < 79)) {
            displayEndGameConfirm = true;

        } else if (xpos > 283 && xpos < 329) {

            //PLAY BUTTON
            if (ypos > 110 && ypos < 155) {
                updateSpeed = 10;
                System.out.println("play");

                //PLAY 2x BUTTON
            } else if (ypos > 60 && ypos < 105) {
                updateSpeed = 5;
                System.out.println("play x2");

                //PAUSE BUTTON
            } else if (ypos > 10 && ypos < 55) {
                updateSpeed = 0;
                System.out.println("pause");
            }
        } else if (xpos > 597 && xpos < 810 && displayLoan == true) { //loan buttons
            if (ypos < 143 && ypos > 106) { //small loan
                gameBank.smallLoan();
            } else if (ypos < 98 && ypos > 62) { //medium loan
                gameBank.mediumLoan();
            } else if (ypos < 51 && ypos > 15) { //big loan
                gameBank.bigLoan();
            }
            displayLoan = false;
        }else if (xpos > 597 && xpos < 810 && displayTax == true) { //loan buttons
            if (ypos < 143 && ypos > 106) { //small loan
                gameBank.smallTax();
            } else if (ypos < 98 && ypos > 62) { //medium loan
                gameBank.mediumTax();
            } else if (ypos < 51 && ypos > 15) { //big loan
                gameBank.bigTax();
            }
            displayLoan = false; 
        }else if (xpos > 9 && xpos < 216) { //get loan, set tax rate and view bank info buttons
            if (ypos < 386 && ypos > 340) {
                infoLine1 = gameBank.toString();
                //Bank information will be output
            } else if (ypos < 334 && ypos > 287 && difficulty != 1) {
                if (displayLoan == false) {
                    displayLoan = true;
                } else {
                    displayLoan = false;
                }
                //tax rate window will popup allowing you to adjust tax rate

            } else if (ypos < 282 && ypos > 235) {
                if (displayTax == false) {
                    displayTax = true;
                } else {
                    displayTax = false;
                }

                //tax rate window will popup allowing you to adjust tax rate
            }
        }
    }

    void buildItem(int x, int y, int buildType) throws SlickException { //build item method which sets coordinates on the grid to one of the build objects
        switch (buildType) { //the build type determines the type of object, the x and y coordinates determine location. Mouse image is the image set by the zone build item select methods
            case 0:  //switch statement is used to run through each case
                gameGrid[x][y] = new ResidentialZone(mouseImage, x, y);
                break;
            case 1:
                gameGrid[x][y] = new CommercialZone(mouseImage, x, y);
                break;
            case 2:
                gameGrid[x][y] = new IndustrialZone(mouseImage, x, y);
                break;
            case 3:
                policeBuildItemSelected(); //just to push the increased value
                policeBuildCost += 300; //increases each time you build
                gameGrid[x][y] = new Police(mouseImage, x, y, difficulty);
                break;
            case 4:
                hospitalBuildItemSelected(); //just to push the increased value
                hospitalBuildCost += 300; //increases each time you build
                gameGrid[x][y] = new Hospital(mouseImage, x, y, difficulty);                                                                                                //Taylor
                break;                                              
            case 5:
                gameGrid[x][y] = new Electricity(mouseImage, x, y, difficulty);
                break;
            case 6:
                gameGrid[x][y] = new Water(mouseImage, x, y, difficulty);
                break;
            case 7:
                gameGrid[x][y] = new RecreationalZone(mouseImage, x, y, recAttractionMax, difficulty); //recAttractionMax 
                break;
            case 8:
                if (roadCheck(x, y) || firstRoad) { //check if the road is being placed near another road, or if it is the first road built
                    gameGrid[x][y] = new Road(mouseImage, x, y);
                    infoLine1 = "";
                } else {
                    infoLine1 = "Please place near road";
                }
                break;
            case 9: //removal case
                if (gameGrid[x][y] instanceof CommercialZone) { //if removing a commercial zone
                    CommercialZone.setTotalJobs(CommercialZone.getTotalJobs() - ((CommercialZone) gameGrid[x][y]).getJobsCreated()); //remove jobs created by this commercialZone
                    CommercialZone.removeCommercialStack((CommercialZone) gameGrid[x][y]); //remove the commercial zone located at x, y
                    CommercialZone.limitCommerce(); //calculate new jobs and limit the commerce respectfully
                } else if (gameGrid[x][y] instanceof ResidentialZone) {
                    ResidentialZone.setTotalJobsInUse(ResidentialZone.getTotalJobsInUse() - ((ResidentialZone) gameGrid[x][y]).getResidentCount()); //remove residents from homes
                    CommercialZone.limitCommerce(); //calculate the new jobs
                    if (((ResidentialZone) gameGrid[x][y]).getWaterLoc()[0] != -1) { //make sure the residential zone has a water station.
                        int[] tempLoc = ((ResidentialZone) gameGrid[x][y]).getWaterLoc(); //get location of the water source this residentialZone was using
                        ((Water) gameGrid[tempLoc[0]][tempLoc[1]]).setHydro(((Water) gameGrid[tempLoc[0]][tempLoc[1]]).getHydro() + 1);  //take the water station located at x,y and increase its hydro count by 1 because we are removing the residential zone which was using it. The getWaterLoc is the array which holds the coordinates of the water station the residential zone was using.
                    }
                    if (((ResidentialZone) gameGrid[x][y]).getElectricityLoc()[0] != -1) { //make sure the residentialZone had a electricity station
                        int[] tempLoc = ((ResidentialZone) gameGrid[x][y]).getElectricityLoc(); //same thing as above, with electricity
                        ((Electricity) gameGrid[tempLoc[0]][tempLoc[1]]).setPower(((Electricity) gameGrid[tempLoc[0]][tempLoc[1]]).getPower() + 1); //does same as above, but with electricity
                    }
                } else if (gameGrid[x][y] instanceof IndustrialZone) {
                    IndustrialZone.setTotalCommercialAvailable(IndustrialZone.getTotalCommercialAvailable() - ((IndustrialZone) gameGrid[x][y]).getCommercialIncrease()); //remove industrial jobs from the static variable
                } else if (gameGrid[x][y] instanceof Road) {
                    gameGrid[x][y] = null; //set to null before roadDelete, as this is necessary
                    roadDelete(x, y); //remove surrounding buildings that are anchored to the road being deleted
                }

                gameGrid[x][y] = null; //remove the object
                break;
        }
        if ((roadCheck(x, y) || firstRoad) && buildType != 9) { //this if statement forces buildings to be built next to other roads, unless it is the first road built

            //buildItemSelected = false;
            int[] arg1 = {x, y}; //the coordinates of the build object are added to an array

            gridStack.add(arg1);

        }

        if (buildType == 9) { //if the remove build type option was chosen, or case 10, the object is removed from the array
            //System.out.println(gridStack.indexOf(arg1)); //this only returns a -1, have to search
            for (int i = 0; i < gridStack.size(); i++) {
                if (x == gridStack.get(i)[0] && y == gridStack.get(i)[1]) {  //unfortunatly you are unable to use gridStack.remove(int[] object) for some reason, so you must do a quick search for the value
                    gridStack.remove(i); //use indexOf??, index of only returns a -1
                }
            }
            for (int i = 0; i < warningStack.size(); i++) {
                if (warningStack.get(i)[0] == x && warningStack.get(i)[1] == y) {
                    warningStack.remove(i);
                    break;
                }
            }
        }

        for (int i = 0; i < gridStack.size(); i++) { //for loops go through all build object coordinates using gridstack array list

            int[] arg1 = gridStack.get(i); //int arg1 represents the current gridstack coordinate in the for loop

            if (gameGrid[arg1[0]][arg1[1]] instanceof ResidentialZone) {
                ((ResidentialZone) gameGrid[arg1[0]][arg1[1]]).calculateProx(); //calculate prox when a new build item is created.
                residentialWarning((ResidentialZone) gameGrid[arg1[0]][arg1[1]]); //check if residential buildings have new warnings, no water, no hostpial etc
            }
            if (gameGrid[arg1[0]][arg1[1]] instanceof CommercialZone) {
                commercialWarning((CommercialZone) gameGrid[arg1[0]][arg1[1]]); //check if the commercial zones are making revenue, and if not, display icon
            }
        }

        if (!firstRoad) { //the first road built in the game does not cost
            gameBank.setMoney(gameBank.getMoney() - buildCost); //money is deducted from the game bank in order to build the item
        }
    }

    void residentBuildItemSelected() { //methods which set build item selected to true and then initialize the variables required to build each object
        buildItemSelected = true;      //The update method checks continiously to see if build item selected is true, if so the object is built using these variables
        buildType = 0;
        buildCost = 200;
        mouseImage = resImg;
    }

    void commercialBuildItemSelected() {
        buildItemSelected = true;
        buildType = 1;
        buildCost = 200;
        mouseImage = comImg;
    }

    void industrialBuildItemSelected() {
        buildItemSelected = true;
        buildType = 2;
        buildCost = 200;
        mouseImage = indImg;
    }

    void policeBuildItemSelected() {
        buildItemSelected = true;
        buildType = 3;
        buildCost = 500 + policeBuildCost;
        mouseImage = policeImg;
    }

    void hospitalBuildItemSelected() {
        buildItemSelected = true;
        buildType = 4;
        buildCost = 500 + hospitalBuildCost;
        mouseImage = hospitalImg;
    }

    void electricBuildItemSelected() {
        buildItemSelected = true;
        buildType = 5;
        buildCost = 2000;
        mouseImage = electricImg;
    }

    void waterBuildItemSelected() {
        buildItemSelected = true;
        buildType = 6;
        buildCost = 2000;
        mouseImage = waterImg;
    }

    void recreationalBuildItemSelected() {
        buildItemSelected = true;
        buildType = 7;
        int rand = randomIntBetween(2, 0);
        System.out.println(rand);
        switch (rand) {
            case 0:
                mouseImage = recImg; //make a different recreational image for each of these situations, just to add variety
                recAttractionMax = 60;
                buildCost = 100;
                break;
            case 1:
                mouseImage = recImg1;
                buildCost = 200;
                recAttractionMax = 70;
                break;
            case 2:
                mouseImage = recImg2;
                buildCost = 500;
                recAttractionMax = 80;
                break;
            default:
                break;
        }
    }

    void removeBuildItemSelected() {
        buildItemSelected = true;
        buildType = 9;
        buildCost = -100;
        mouseImage = bullDozerImg;
    }

    void roadBuildItemSelected() {
        buildItemSelected = true;
        buildType = 8;
        buildCost = 100;
        mouseImage = roadImg;
    }

    void residentialWarning(ResidentialZone tempResZone) { //residential warning symbols
        int[] tempInt;
        if (tempResZone.getWaterProx() == -1) {
            tempInt = new int[]{tempResZone.getXGrid(), tempResZone.getYGrid(), 0}; //set the array to the position of the fed residential zone, plus the index 0 as to indicate that the warning is no water
            checkAndRemove(tempInt);
            warningStack.add(tempInt); //adds the location of the res zone to the warning stack, add the water label to it. 0 is the index of the water warning
        } else if (tempResZone.getElectricityProx() == -1) {
            tempInt = new int[]{tempResZone.getXGrid(), tempResZone.getYGrid(), 1}; //1 to indicated no electricity
            checkAndRemove(tempInt); //remove the previous error from this location, only 1 error can be present at a time, not the most efficient
            warningStack.add(tempInt);
        } else if (tempResZone.getHospitalProx() == 20) {
            tempInt = new int[]{tempResZone.getXGrid(), tempResZone.getYGrid(), 2}; //2= no hospital                                                                        //Taylor
            checkAndRemove(tempInt);
            warningStack.add(tempInt);
        } else if (tempResZone.getPoliceProx() == 20) {
            tempInt = new int[]{tempResZone.getXGrid(), tempResZone.getYGrid(), 3}; // 3 = no police
            checkAndRemove(tempInt);
            warningStack.add(tempInt);
        } else {
            checkAndRemove(new int[]{tempResZone.getXGrid(), tempResZone.getYGrid()}); //this clears all errors
        }
    }

    void checkAndRemove(int[] tempInt) { //feed it an int[] because that is what is being sent to the warningStack, easier than re formatting
        for (int i = 0; i < warningStack.size(); i++) {
            if (warningStack.get(i)[0] == tempInt[0] && warningStack.get(i)[1] == tempInt[1]) { //removal of an item from warningStack
                warningStack.remove(i);
                break;
            }
        }
    }

    void commercialWarning(CommercialZone tempComZone) {
        System.out.println("per year available: " + tempComZone.getRevenuePerYearAvailable());
        checkAndRemove(new int[]{tempComZone.getXGrid(), tempComZone.getYGrid()}); //remove all warnings, have to do this before the revenue check or it will not remove.
        if (tempComZone.getRevenuePerYearAvailable() <= 0) {
            warningStack.add(new int[]{tempComZone.getXGrid(), tempComZone.getYGrid(), 4}); //adds the location of the res zone to the warning stack, add the water label to it. 0 is the index of the water warning
        }

    }

    public static ZoneType[][] getGameGrid() { //get method to return game grid
        return gameGrid;
    }

    //Gets the ID of the game state
    public int getID() {
        return 2;
    }

    public static String getInfoLine1() {
        return infoLine1;
    }

    public static void setInfoLine1(String infoLine1) {
        TMMgame.infoLine1 = infoLine1;
    }

    public static String getInfoLine2() {
        return infoLine2;
    }

    public static void setInfoLine2(String infoLine2) {
        TMMgame.infoLine2 = infoLine2;
    }

    public static boolean roadCheck(int a, int b) {
        for (int x = -1; x <= 1; x++) { //check a 3 by 3 grid around the zone, but only check the top, bottom, left, and right side
            for (int y = -1; y <= 1; y++) {
                if (a + x >= 0 && a + x <= 28 && b + y >= 0 && b + y <= 17) { //make sure its checking within the barriers of the grid as to avoid an array out of bounds exception             //Taylor
                    if (gameGrid[a + x][b + y] != null) {
                        if (gameGrid[a + x][b] instanceof Road) { //check if left and right side is a road
                            return true;
                        }
                        if (gameGrid[a][b + y] instanceof Road) { //check if top and bottom side is a road
                            return true;
                        }
                    }
                }
            }
        }
        return false; //failed to find a road
    }

    public void roadDelete(int a, int b) throws SlickException {
        for (int x = -1; x <= 1; x++) { //check 
            for (int y = -1; y <= 1; y++) {
                if ((a + x >= 0 && a + x <= 28 && b + y >= 0 && b + y <= 17)) { //make sure its checking within the barriers of the grid as to avoid an array out of bounds exception

                    if (gameGrid[a + x][b + y] != null) { //make sure the grid it is checking is not empty
                        if (!(gameGrid[a + x][b] instanceof Road && roadCheck(a + x, b)) && (x != 0)) {  //if the location around a road that is being removed is either another road or a building that has another road to anchor to, then don't delete it
                            this.removeBuildItemSelected(); //select buildType 9, deletion
                            this.buildItem(a + x, b, buildType); //otherwise, when a road is deleted, buildings connected to it are also deleted.

                        }

                        if (!(gameGrid[a][b + y] instanceof Road && roadCheck(a, b + y)) && (y != 0)) { //check if top and bottom side is a road                                                //Taylor
                            this.removeBuildItemSelected();
                            this.buildItem(a, b + y, buildType);

                        }
                    }

                }
            }
        }
    }

    public void monthEnd() { //end of the month method
        int revenue = CommercialZone.getTotalRevenueAvailable(); //int which holds revenue values from all commercial zones
        System.out.println("Business revenue: " + revenue);
        int expense = RecreationalZone.getExpense() + GovernmentZone.getExpense(); //int which holds all expense values from all commercial zones

        double commercialTax = gameBank.getTax();

        for (int i = 0; i < gridStack.size(); i++) { //loop through all gameGrid elements on the screen (gridStack)
            int[] arg1 = gridStack.get(i);

            if (gameGrid[arg1[0]][arg1[1]] instanceof CommercialZone) {
                if (commercialTax == 0.05) {
                    gameGrid[arg1[0]][arg1[1]].setAttraction(gameGrid[arg1[0]][arg1[1]].getAttraction() - 5);
                } else if (commercialTax == 0.1) {
                    gameGrid[arg1[0]][arg1[1]].setAttraction(gameGrid[arg1[0]][arg1[1]].getAttraction() - 10);   //set attraction based off of taxes
                } else if (commercialTax == 0.15) {
                    gameGrid[arg1[0]][arg1[1]].setAttraction(gameGrid[arg1[0]][arg1[1]].getAttraction() - 15);
                }
            }
        }

        gameBank.setRevenue(revenue); //sets the banks revenue value
        gameBank.setExpense(expense); //sets the banks expense value to current expense
        gameBank.monthEnd(); //Completes all of the banks end of the month calculation
        gameClock.setMonthEnd(false); //sets the end of the month to false
    }

    public void dayEnd() { //day end method
        int totalPopulation = ResidentialZone.getTotalJobsInUse(); //total population is set to the population of the residential zones
        gameBank.setResidentialTaxes(0);

        for (int i = 0; i < gridStack.size(); i++) { //for loops go through all build object coordinates using gridstack array list

            int[] arg1 = gridStack.get(i); //int arg1 represents the current gridstack coordinate in the for loop

            if (gameGrid[arg1[0]][arg1[1]] instanceof ResidentialZone) { //if that coordinate is a residential zone

                ResidentialZone tempResidentialZone; //a temp residential zone is declared
                tempResidentialZone = (ResidentialZone) gameGrid[arg1[0]][arg1[1]]; //temp residential zone is then initialized to the existing object     
                tempResidentialZone.happy(gameBank.getTax() * 75, tempResidentialZone.attractionCalc()); //happiness calculations are peformed for that zone, 50 multiplied by tax
                tempResidentialZone.residency(); //residency is calculated for that zone
                gameBank.setResidentialTaxes(gameBank.getResidentialTaxes() + tempResidentialZone.getResidentIncome());
                residentialWarning(tempResidentialZone);
                gameGrid[arg1[0]][arg1[1]] = (ResidentialZone) tempResidentialZone; //the exisiting object is initialized to the temp residential zone
            } else if (gameGrid[arg1[0]][arg1[1]] instanceof CommercialZone) {
                commercialWarning((CommercialZone) gameGrid[arg1[0]][arg1[1]]);
            }

            infoLine2Default = "Total industrial: " + IndustrialZone.getTotalCommercialAvailable() + "\nTotal Commercial: " + CommercialZone.getTotalJobs() + "\nJobs available for residents " + CommercialZone.getTotalJobsAvailable() + "\nJobs in use: " + ResidentialZone.getTotalJobsInUse();  //job message on second smaller info board
        }

        if (totalPopulation == 0 || gameBank.getMoney() < -1000) { //game over check which checks to see if your total population is zero at the end of each day, or your money has reched -1000
            if (!(gameClock.getDay() <= 5 && gameClock.getMonth() == 1 && gameClock.getYear() == 1920)) {  //make sure that the game isn't within the first 5 days, a grace period
                //print game over screen
                //System.out.println("Sorry, everyone left your town, gameover or you have too much debt");
            }
        }

        CommercialZone.totalJobCalc(); //commercial zone job function
        CommercialZone.limitCommerce(); //commercial zone limit function

        for (int i = 0; i < gridStack.size(); i++) { //for loops go through all build object coordinates using gridstack array list

            int[] arg1 = gridStack.get(i); //int arg1 represents the current gridstack coordinate in the for loop
            if (gameGrid[arg1[0]][arg1[1]] instanceof CommercialZone) {
                commercialWarning((CommercialZone) gameGrid[arg1[0]][arg1[1]]);   //seperate the loops so that commercialZone will display a warning after commerce has been limited
            }
        }

        gameClock.setDayEnd(false); //game clock day end variable is set to false
    }

    public void skipMonthEnd() { //skip month end method 
        for (int i = 0; i < 30 - gameClock.getDay(); i++) { //for loop goes through all of the remaining days of the month and performs the day end check
            dayEnd();
        }
        monthEnd(); //month end method is run since it is the end of the month
        if (gameClock.getMonth() != 12) { //if it is not end of the year 
            gameClock.setMonth(gameClock.getMonth() + 1); //month variable is set to the following month                                                                                //Taylor
        } else {
            gameClock.setMonth(1); //if it is the end of the year, month is set to 1
            gameClock.setYear(gameClock.getYear() + 1); //year is set to following year
        }
        gameClock.setDay(1); //all other variables are set to 1
        gameClock.setHour(1);
        gameClock.setMin(1);
    }

    public static String[] readHighScore() { //method that returns an array carrying all the high scores
        String[] highScore = new String[10]; //String array which will carry the high scores. 
        int loopCount = 0; //loop counter which is used to count different versions of a loop
        String score = ""; //string which will carry individual scores
        boolean endOfFile; //loop check

        endOfFile = false; //loop check is initialized to false

        try { //to open the stream to the file
            FileInputStream in = new FileInputStream(System.getProperty("user.dir") + "/highscores/Highscores.txt");
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(isr);
            //loop until the end of the file is reached
            while (!endOfFile) {
                //A line from the file is read to fill out the score field
                score = br.readLine();
                if (score == null) {
                    //The file is finished if there is no score to be read
                    endOfFile = true;
                } else { //otherwise, if the loop count is below 10 the read score is added to the high score
                    if (loopCount < 10) {
                        highScore[loopCount] = score;
                        loopCount++; //loop count is increased by 1
                    }
                }
            }
            br.close(); //close the stream

        } catch (IOException e) { //An io exception
            //print the error
            System.out.println("Error: " + e);
        }

        return highScore; //the array for high scores is returned
    }

    public static String[] writeHighScore() { //method that adds new high scores to the array and returns the array
        double score = calculateScore();
        String[] highScore = readHighScore(); //The high scores array list is declared and initialized to the current list of high scores using the readHighScore method
        boolean loopCheck = true; //loop check is declared and initialized to true

        //for loop which checks to see if new high score is valid
        for (int i = 0; i < highScore.length && loopCheck == true; i++) { //for loop goes through each element of the highScore array to see if the new score is less than the old scores and checks to see if the loop is running
            if (Double.parseDouble(highScore[i]) < score) { //if statement which checks to see if new score is high enough 
                for (int k = i; k < (highScore.length); k++) { //for loop which then moves down every following element in the array
                    Double temp = Double.parseDouble(highScore[k]); //temp is initialized to the former score at that indice in the array
                    highScore[k] = Double.toString(score); //The indice on the highScore array is then set to the new score
                    score = temp; //score is then initialized to temp
                }
                loopCheck = false; //loopcheck is set to false to end the loop if the score is high enough

            }
        }

        try { //to open the stream to the file
            FileOutputStream out = new FileOutputStream(System.getProperty("user.dir") + "/highscores/Highscores.txt");
            OutputStreamWriter isw = new OutputStreamWriter(out);
            BufferedWriter bw = new BufferedWriter(isw);
            //actually write the list to the file
            for (int i = 0; i < highScore.length; i++) { //for loop adds every element of the array to the list
                bw.write(highScore[i]); //the string at each indice is added
                bw.newLine(); //a new line is added to the text file  
            }
            //close the connection and flush the stream
            bw.close();
        } catch (IOException e) { //any IO errors that occur
            //error is printed
            System.out.println("Error: " + e);
        }

        return highScore; //high score is returned
    }

    public static Double calculateScore() { //method that calculates your high score
        Double score = 0.0;
        if (difficulty != 1) { //if on sandbox mode score is zero otherwise score is calculated based on difficulty
            if (difficulty != 4) {
                score = (gameBank.getMoney() + (ResidentialZone.getTotalJobsInUse() * 1000) + ((((gameClock.getDay() / gameClock.getMonthLength()) / 12) + (gameClock.getMonth() / 12) + gameClock.getYear()) * 1000)) * ((difficulty - 1) / 2); //The score that may be input into the highscores text file is declared and initialized
            } else {
                score = (gameBank.getMoney() + (ResidentialZone.getTotalJobsInUse() * 1000) + ((((gameClock.getDay() / gameClock.getMonthLength()) / 12) + (gameClock.getMonth() / 12) + gameClock.getYear()) * 1000)) * 2;
            }
        }
        return score;
    }

}
