/**
 * Dope Wars
 *
 * Author:  Mark Sohm
 *
 * Changes: 1.0 Initial release
 */

package com.msohm.pub.dopeWars;

import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.system.*;
import net.rim.device.api.util.*;
import java.util.*;
import net.rim.plazmic.mediaengine.*;
import net.rim.device.api.synchronization.*;
import com.msohm.dopeWarsLibrary.*;

public class DopeWars extends UiApplication
{
    private Drug[] drugs = new Drug[12];            //Drugs
    private Location[] locations = new Location[8]; //Locations.
    private static Player player;                   //The game player.
    private static Player cop;                      //Officer Hardass.
    private MainScreen introScreen;                 //The game intro screen.
    private MediaPlayer mediaPlayer;
    private MediaManager mediaManager;
    private MainScreen gameScreen;                  //The game playing screen.
    private int day;                                //The current game date.
    private HorizontalFieldManager backGround;      //Background horizontal field manager.
    private VerticalFieldManager leftColumn;        //Left side vertical field manager.
    private VerticalFieldManager rightColumn;       //Right side vertical field manager.
    private LabelField cashLabel;
    private LabelField slotsLabel;
    private LabelField bankLabel;
    private LabelField loanLabel;
    private LabelField leftSpacerLabel;
    private LabelField rightSpacerLabel;
    private DrugLabelField[] drugLabels = new DrugLabelField[12];
    private int currentLocation;
    private int[] currentDrugs;                     //Drugs available in the current location.
    private int[] drugInventory = new int[12];
    private MenuItem inventory;
    private MenuItem prices;
    private MenuItem buy;
    private MenuItem sell;
    private MenuItem jet;
    private static Random random = new Random();
    private static PersistentObject store;          //Stores high scores.
    private DopeWarsHighScores dopeWarsHighScores;
    private String[] highScoreNames = {"", "", "", "", ""};
    private int highScoreValues[] = {0, 0, 0, 0, 0};
    private HighScoreScreen highScoreScreen;
    private boolean pricesMenu;
    
    static 
    {
        //Long value = com.msohm.dopeWars
        store = PersistentStore.getPersistentObject(0x120d120fa2875f50L);
    }

    public static void main(String[] args)
    {
        boolean startup = false;
        
        //Check parameters to see if the application was entered through
        //the alternate application entry point.
        for (int i=0; i<args.length; ++i) 
        {
            if (args[i].startsWith("AutoStart")) 
            {
                startup = true;
            }
        }
        
        if (startup) 
        {
            //Entered through the alternate application entry point.
            //Enable application for synchronization on startup.
            SyncManager.getInstance().enableSynchronization(new DopeWarsSyncItem(), true, 0);
        } else 
        {
            DopeWars theGame = new DopeWars();
            theGame.enterEventDispatcher();
        }        
    }    
    
    public DopeWars()
    {
        //Retrieve high scores or set to default values.    
        synchronized (store) 
        {
            if (store.getContents() == null) 
            {
                dopeWarsHighScores = new DopeWarsHighScores(highScoreNames, highScoreValues);               
                store.setContents(dopeWarsHighScores);
                store.commit();
            }
            else
            {
                dopeWarsHighScores = (DopeWarsHighScores)store.getContents();
                highScoreNames = dopeWarsHighScores.getNames();
                highScoreValues = dopeWarsHighScores.getScores();
            }
        }        
        
        Object mediaFile;
        
        introScreen = new MainScreen();        
        mediaPlayer = new MediaPlayer();
        mediaManager = new MediaManager();
        
        initializeDrugs();
        initializeLocations();        
    
        //Start a new game.
        MenuItem startGame = new MenuItem("New Game", 10, 10) 
        {
            public void run() 
            {
                newGame();
                UiApplication.getUiApplication().popScreen(introScreen);
            }
        };
        
        //The High Scores menu item.
        MenuItem highScores = new MenuItem("High Scores", 20, 20)
        {
            public void run() 
            {
                highScoreScreen = new HighScoreScreen(highScoreNames, highScoreValues);
                pushScreen(highScoreScreen);
            }
        };
                                        
        //Add the custom menu items to the screen.
        introScreen.addMenuItem(startGame);
        introScreen.addMenuItem(highScores);
        
        try
        {
            //Download the Plazmic media file to display.
            mediaFile = mediaManager.createMedia("cod://DopeWarsLibrary/IntroScreen.pmb", "application/x-vnd.rim.pme.b");

            //Set the media in the player.
            mediaPlayer.setMedia(mediaFile);                        
        }
        catch (Exception ex)
        {
            Dialog.alert("Could not load media file.");
        }
        
        //The MediaPlayer can create a Ui Field we can use to 
        //display the media. Get the Object from the Player.
        Object uiObject = mediaPlayer.getUI();
        
        //Add the Object to the MainScreen.
        introScreen.add((Field)uiObject);        
                
        pushScreen(introScreen);
        
        //If the MediaPlayers state is REALIZED, the media is loaded.
        //We can start the animation.
        if (mediaPlayer.getState() == MediaPlayer.REALIZED)
        {
            try
            {
                mediaPlayer.start();
            } catch (Exception e){
                Dialog.alert("Could not load media file.");
            }
        }
    }

    private void initializeDrugs()
    {
        drugs[0] = new Drug("Acid", 1000, 4400, true, "The market is flooded with cheap home-made acid!");
        drugs[1] = new Drug("Cocaine", 15000, 29000, false, null);
        drugs[2] = new Drug("Hashish", 480, 1280, true, "The Marrakesh Express has arrived!");
        drugs[3] = new Drug("Heroin", 5500, 13000, false, null);
        drugs[4] = new Drug("Ludes", 11, 60, true, "Rival drug dealers raided a pharmacy and are selling cheap ludes!");
        drugs[5] = new Drug("MDA", 1500, 4400, false, null);
        drugs[6] = new Drug("Opium", 540, 1250, false, null);
        drugs[7] = new Drug("PCP", 1000, 2500, false, null);
        drugs[8] = new Drug("Peyote", 220, 700, false, null);
        drugs[9] = new Drug("Shrooms", 630, 1300, false, null);
        drugs[10] = new Drug("Speed", 90, 250, false, null);
        drugs[11] = new Drug("Weed", 315, 890, true, "Columbian freighter dusted the Coast Guard!  Weed prices have bottomed out!");
    }

    private void initializeLocations()
    {
        locations[0] = new Location("Bronx", 8, 12);
        locations[1] = new Location("Ghetto", 6, 12);
        locations[2] = new Location("Central Park", 5, 8);
        locations[3] = new Location("Manhattan", 7, 10);
        locations[4] = new Location("Coney Island", 5, 9);
        locations[5] = new Location("Brooklyn", 9, 12);
        locations[6] = new Location("Queens", 6, 9);
        locations[7] = new Location("Staten Island", 4, 10);
    }        
    
    protected void onExit()
    {
        //On exit stop the media player and remove the downloaded file.
        mediaPlayer.stop();
        mediaPlayer.close();
        mediaManager.dispose();
    }    
    
    private void newGame()
    {
        int count;
        
        gameScreen = new MainScreen(MainScreen.VERTICAL_SCROLL){
            public boolean onClose()
            {
                //Switch menu and labels from inventory to drug list if the player
                //is currently viewing the prices menu.
                if (!pricesMenu)
                {
                    pricesAction();
                }
                else
                {
                    //The inventory screen wasn't up.  Ask the user if they want to quit.
                    if (Dialog.ask(Dialog.D_YES_NO, "Quit game?") == Dialog.YES)
                    {
                        pushScreen(introScreen);
                        popScreen(gameScreen);
                    }
                }
                return true;
            }            
        };
        
        player = new Player("Player", 2000, 5500, 0, 100, 100, 100, Weapons.NONE);
        cop = new Player("Officer Hardass", 0, 0, 0, 0, 0, 100, Weapons.BARETTA);
        day = 1;
        backGround = new HorizontalFieldManager(HorizontalFieldManager.VERTICAL_SCROLL);
        leftColumn = new VerticalFieldManager();
        rightColumn = new VerticalFieldManager();
        currentLocation = 0;
        pricesMenu = true;
        
        //Add the two vertical columns to the horizontal field manager.
        backGround.add(leftColumn);
        backGround.add(rightColumn);
        
        cashLabel = new LabelField("Cash: $" + CurrencyConverter.convertCurrency(player.getCash())){
            public void paint(Graphics graphics)
            {
                //Change the colour of the text in the LabelField to green.
                graphics.setColor(0x00008800);
                super.paint(graphics);
            }
        };
        
        slotsLabel = new LabelField("Pockets: " + (player.getTotalSlots() - player.getFreeSlots())
            + "/"+ player.getTotalSlots());
        
        bankLabel = new LabelField("Bank: $" + CurrencyConverter.convertCurrency(player.getBank())){
            public void paint(Graphics graphics)
            {
                //Change the colour of the text in the LabelField to blue.
                graphics.setColor(0x000000A0);
                super.paint(graphics);
            }
        };

        loanLabel = new LabelField("Loan: $" + CurrencyConverter.convertCurrency(player.getLoan())){
            public void paint(Graphics graphics)
            {
                //Change the colour of the text in the LabelField to red.
                graphics.setColor(0x00FF0000);
                super.paint(graphics);
            }
        };
        
        //Get the size of a space in the current font.
        int size = gameScreen.getGraphics().getFont().getAdvance(' ');
        
        //Determine the number of spaces that will fit in half the screen.
        size = (Graphics.getScreenWidth() / size / 2);

        //Create a StringBuffer with size number of spaces.
        StringBuffer spaceBuffer = new StringBuffer();            
        for(count = 0; count < size; ++count)
        {
            spaceBuffer.append(' ');
        }
        
        //Create label fields of size spaces long and add it to each column.
        leftSpacerLabel = new LabelField(spaceBuffer.toString());
        rightSpacerLabel = new LabelField(spaceBuffer.toString());
        
        //Initialize the drug labels and drug inventory.
        for (count = 0; count < 12; ++count)
        {
            drugLabels[count] = new DrugLabelField("");
            drugInventory[count] = 0;
            
        }
        
        //Get a new random array of drugs.        
        currentDrugs = generateRandomDrugList(locations[currentLocation].getRandomNumDrugs());
    
        //Define the inventory menu item.
        inventory = new MenuItem("Inventory", 100, 100) 
        {
            public void run() 
            {
                inventoryAction();
            }
        };

        //Define the prices menu item.
        prices = new MenuItem("Prices", 100, 100) 
        {
            public void run() 
            {
                pricesAction();
            }
        };
        
        //Define the prices menu item.
        buy = new MenuItem("Buy", 20, 20) 
        {
            public void run() 
            {
                buyAction();
            }
        };

        //Define the sell menu item.
        sell = new MenuItem("Sell", 30, 30) 
        {
            public void run() 
            {
                sellAction();
            }
        };
        
        //Define the jet menu item.
        jet = new MenuItem("Jet", 40, 40) 
        {
            public void run() 
            {
                jetAction();
            }
        };       
        
        //Add the top status labels to the columns.
        leftColumn.add(cashLabel);
        rightColumn.add(slotsLabel);
        leftColumn.add(bankLabel);
        rightColumn.add(loanLabel);
        leftColumn.add(leftSpacerLabel);
        rightColumn.add(rightSpacerLabel);

        //Add the menu items to the screen.
        gameScreen.addMenuItem(inventory);
        gameScreen.addMenuItem(buy);
        gameScreen.addMenuItem(sell);
        gameScreen.addMenuItem(jet);
        
        //Add the horizontal field manager to the screen.
        gameScreen.add(backGround);
        
        gameScreen.setTitle("Day " + day + "/31 Health: " + player.getHealth());
        
        //Update the game screen (only adding drug labels here).
        updateGameScreen();
        
        //Push the screen onto the stack.
        pushScreen(gameScreen);
    }
    
    //Returns an array of integers in numerical order between 0 and drugs.count 
    //of which the size is specified by numberOfDrugs.
    private int[] generateRandomDrugList(int numberOfDrugs)
    {
        int randsCreated = 0;
        int numbers[] = new int[drugs.length - numberOfDrugs];
        int retVal[] = new int[numberOfDrugs];
        int rand;
        int retCount = 0;
        Random randValue = new Random();
        boolean duplicate;
        
        //Initialize the exclusion array.
        for(int count = 0; count > numbers.length; ++count)
        {
            numbers[count] = -1;
        }
        
        //Keep generating random numbers until we have numbers.length unique numbers.
        while (randsCreated < numbers.length)
        {
            //Generate a random number.
            rand = randValue.nextInt(drugs.length);
            
            duplicate = false;
            //Check to ensure this number wasn't already selected.
            for(int count = 0; count < numbers.length; ++count)
            {
                if (rand == numbers[count])
                    duplicate = true;
            }
            
            //If it wasn't a duplicate, add it to the end of the list.
            if (!duplicate)
            {
                numbers[randsCreated] = rand;
                ++randsCreated;
            }
        }
        
        //Build up the array to return.
        for (int count = 0; count < drugs.length; ++count)
        {
            duplicate = false;
            
            //Check to see if the current count value is excluded.
            for(int countb = 0; countb < numbers.length; ++countb)
            {
                if (count == numbers[countb])
                {
                    duplicate = true;
                }
            }
            
            //If it is not a duplicate drug add it to the list and randomize the price.
            if (!duplicate)
            {
                retVal[retCount] = count;
                ++retCount;
                drugs[count].randomizePrice();
            }
        }
        
        return retVal;
    }
    
    //Called when the user clicks on the prices menu item.
    private void pricesAction()
    {
        pricesMenu = true;
        
        //Remove the current drugLabels from the screen.
        int leftFields = leftColumn.getFieldCount() - 3;
        leftColumn.deleteRange(3, leftFields);

        int rightFields = rightColumn.getFieldCount() - 3;
        rightColumn.deleteRange(3, rightFields);

        for (int count = 0; count < locations[currentLocation].getCurrentNumDrugs(); ++count)
        {
            //Highlight labels if the player has some of that drug.
            if (drugInventory[currentDrugs[count]] > 0)
            {
                drugLabels[count].setHighlight(true);
            }
            else
            {
                drugLabels[count].setHighlight(false);
            }            

            drugLabels[count].setText(drugs[currentDrugs[count]].getName() + " $" + 
                CurrencyConverter.convertCurrency(drugs[currentDrugs[count]].getCurrentPrice()));
            drugLabels[count].setDrugIndex(currentDrugs[count]);
            
            if (count % 2 == 0)
                leftColumn.add(drugLabels[count]);
            else
                rightColumn.add(drugLabels[count]);
        }

        //Add the Inventory, buy and sell menu item back to the menu.
        gameScreen.addMenuItem(inventory);
        gameScreen.addMenuItem(buy);
        gameScreen.addMenuItem(sell);
        
        //Remove the prices menu item.
        gameScreen.removeMenuItem(prices);
    }// end pricesAction
    
    //Called when the user clicks on the inventory menu item.
    private void inventoryAction()
    {
        pricesMenu = false;
        //Remove the current drugLabels from the screen.
        int leftFields = leftColumn.getFieldCount() - 3;
        leftColumn.deleteRange(3, leftFields);
        
        int rightFields = rightColumn.getFieldCount() - 3;
        rightColumn.deleteRange(3, rightFields);
        
        //Re-add the fields showing drug inventory.
        for (int count = 0; count < 12; ++count)
        {
            drugLabels[count].setText(drugs[count].getName() + " " + drugInventory[count]);
            drugLabels[count].setHighlight(false);
            
            if (count % 2 == 0)
                leftColumn.add(drugLabels[count]);
            else
                rightColumn.add(drugLabels[count]);
        }
        
        //Remove the Inventory, buy  and sellmenus item from the menu.
        gameScreen.removeMenuItem(inventory);
        gameScreen.removeMenuItem(buy);
        gameScreen.removeMenuItem(sell);
        
        //Add the prices menu item.
        gameScreen.addMenuItem(prices);
    }//end inventoryAction
    
    private void buyAction()
    {
        final int drugIndex;
        final int drugUnits;
        int amount = 0;
        PromptDialog buyDrugsDialog;
        
        boolean keepGoing = true;
        DrugLabelField drugLabelField;
        int price = 0;
        
        if ((drugIndex = getSelectedDrug()) == -1)
        {
            //Something strange happened.
            keepGoing = false;
            Dialog.alert("Select a drug first.");
        }
        
        //Ensure the player can afford the selected drug.
        if (keepGoing && player.getCash() < drugs[drugIndex].getCurrentPrice())
        {
            keepGoing = false;
            Dialog.alert("You can't afford " + drugs[drugIndex].getName() + ".");
        }
        else if (keepGoing)
        {
            drugUnits = player.getCash() / drugs[drugIndex].getCurrentPrice() ;
            
            if (drugUnits <= player.getFreeSlots())
            {
                buyDrugsDialog = new PromptDialog("Purchase " + 
                    drugs[drugIndex].getName() + " ($" + 
                    CurrencyConverter.convertCurrency(drugs[drugIndex].getCurrentPrice()) +
                    ")", "You can afford " + 
                    drugUnits + ": ", drugUnits, true);
            }
            else
            {
                buyDrugsDialog = new PromptDialog("Purchase " + 
                    drugs[drugIndex].getName() + " ($" + 
                    CurrencyConverter.convertCurrency(drugs[drugIndex].getCurrentPrice()) +
                    ")", "You can afford " + 
                    drugUnits + ": ", player.getFreeSlots(), true);
            }
                
            buyDrugsDialog.show();
            
            //Set the amount to 0 if the user cancels the prompt.
            if(buyDrugsDialog.isCancelled()) 
            {
                amount = 0;
            }
            else
            {
                amount = Math.abs(Integer.parseInt(buyDrugsDialog.getData()));
            }

            if ((price = amount * drugs[drugIndex].getCurrentPrice()) > player.getCash())
            {
                keepGoing = false;
                
                Dialog.alert("You can't afford " + amount + " " + 
                    drugs[drugIndex].getName() + ".");
            }
        }  // end else if (keepGoing)
        
        //Ensure the user has room for what they want to buy.
        if (keepGoing && (player.getFreeSlots() < amount))
        {
            keepGoing = false;
            Dialog.alert("You don't have room for " + amount + " " + 
                drugs[drugIndex].getName() + ".");
        }
        
        if (keepGoing)
        {
            //All tests passed, complete the purchase!
            
            //Fill up their trenchcoat.
            player.setFreeSlots(player.getFreeSlots() - amount);
            
            //Take their money.
            player.setCash(player.getCash() - price);
            
            //Give them their drugs.
            drugInventory[drugIndex] += amount;
            
            //Update the screen to show the new values.
            updateGameScreen();
        }

    }// end BuyAction
    
    
    //Called when the user clicks on the sell menu item.
    private void sellAction()
    {
        final int drugIndex;
        int amount = 0;
        
        boolean keepGoing = true;
        
        if ((drugIndex = getSelectedDrug()) == -1)
        {
            //Something strange happened.
            keepGoing = false;
            
            Dialog.alert("Select a drug first.");
        }
        
        //Ensure the player has some of this drug.
        if (drugInventory[drugIndex] <= 0 && keepGoing)
        {
            keepGoing = false;
            
            Dialog.alert("You don't have any " + drugs[drugIndex].getName() + " to sell.");
        }
        else if (keepGoing)
        {
            PromptDialog sellDrugsDialog = new PromptDialog("Sell " + 
                drugs[drugIndex].getName() + " ($" + 
                CurrencyConverter.convertCurrency(drugs[drugIndex].getCurrentPrice()) +
                ")", "You have " + 
                drugInventory[drugIndex] + ": ", drugInventory[drugIndex], true);
                
            sellDrugsDialog.show();
            
            //Set the amount to 0 if the user cancels the prompt.
            if(sellDrugsDialog.isCancelled()) 
            {
                amount = 0;
            }
            else
            {
                amount = Math.abs(Integer.parseInt(sellDrugsDialog.getData()));
            }

            if (amount > drugInventory[drugIndex])
            {
                keepGoing = false;

                Dialog.alert("You don't have " + amount + " " + 
                    drugs[drugIndex].getName() + ".");
            }
        }//end else if (keepGoing)
        
        if (keepGoing)
        {
            //All tests passed, complete the purchase!
            
            //Take away their drugs.
            drugInventory[drugIndex] -= amount;
            
            //Give them their money.
            player.setCash(player.getCash() + (amount * drugs[drugIndex].getCurrentPrice()));
            
            //Empty their trenchcoat.
            player.setFreeSlots(player.getFreeSlots() + amount);
            
            //Update the screen to show the new values.
            updateGameScreen();
        
        }
    }// end sellAction
    
    //Updates the player status LabelFields on the screen.
    private void updateGameScreen()
    {
        gameScreen.setTitle("Day " + day + "/31 Health: " + player.getHealth());
        cashLabel.setText("Cash: $" + CurrencyConverter.convertCurrency(player.getCash()));
        slotsLabel.setText("Pockets: " + (player.getTotalSlots() - player.getFreeSlots())
            + "/"+ player.getTotalSlots());
        bankLabel.setText("Bank: $" + CurrencyConverter.convertCurrency(player.getBank()));
        loanLabel.setText("Loan: $" + CurrencyConverter.convertCurrency(player.getLoan()));
        
        //Remove the current drugLabels from the screen.
        int leftFields = leftColumn.getFieldCount() - 3;
        leftColumn.deleteRange(3, leftFields);
        
        int rightFields = rightColumn.getFieldCount() - 3;
        rightColumn.deleteRange(3, rightFields);
        
        for (int count = 0; count < currentDrugs.length; ++count)
        {
            drugLabels[count].setText(drugs[currentDrugs[count]].getName() + 
                " $" + CurrencyConverter.convertCurrency(drugs[currentDrugs[count]].getCurrentPrice()));
                    
            //Set the index value of the drug in the drugs array.
            drugLabels[count].setDrugIndex(currentDrugs[count]);
            
            //Highlight labels if the player has some of that drug.
            if (drugInventory[currentDrugs[count]] > 0)
            {
                drugLabels[count].setHighlight(true);
            }
            else
            {
                drugLabels[count].setHighlight(false);
            }
            
            if (count % 2 == 0)
                leftColumn.add(drugLabels[count]);
            else
                rightColumn.add(drugLabels[count]);
                
        }                
        
        gameScreen.invalidate();
    }//end updateGameScreen
    
    //Returns the index of currently selected drug.
    private int getSelectedDrug()
    {
        int drugIndex = -1;
        DrugLabelField drugLabelField;
        
        if (leftColumn.getFieldWithFocusIndex() != -1)
        {
            drugLabelField = (DrugLabelField)leftColumn.getFieldWithFocus();
            drugIndex = drugLabelField.getDrugIndex();
        }
        else if (rightColumn.getFieldWithFocusIndex() != -1)
        {
            drugLabelField = (DrugLabelField)rightColumn.getFieldWithFocus();
            drugIndex = drugLabelField.getDrugIndex();
        }
        
        return drugIndex;

    }
    
    //Called when the user clicks on the jet menu item.
    private void jetAction()
    {
        final String[] locationNames = new String[locations.length];
        final int[] locationIndexes = new int[locations.length];
        int choice, freeDrugs, randValue, weapon;
        int amount = 0;
        int count;
        final String[] bankChoices = {"Deposit", "Withdraw"};
        final String[] sharkChoices = {"Payback Loan", "Take out loan"};
        int highLowDrug;    //The Bottomed or topped out drug.
        
        //Build the string array of locations to be passed to the Dialog.ask method.
        for (count = 0; count < locations.length; ++count)
        {
            if (count == currentLocation)
            {
                locationNames[count] = (count + 1) + ". " + "*" + locations[count].getName();
            }
            else
            {
                locationNames[count] = (count + 1) + ". " + locations[count].getName();
            }
            
            locationIndexes[count] = count;
        }

        //Prompt the user for where they want to jet.
        //Ensure the user selected a new location.
        if ((choice = Dialog.ask("Where to, dude?", locationNames, locationIndexes, currentLocation))
            != currentLocation)
        {
            //Update the player's location.
            currentLocation = choice;
            
            //Get a new list of random drugs.
            currentDrugs = generateRandomDrugList(locations[currentLocation].getRandomNumDrugs());            
            
            //Switch menu and labels from inventory to drug list if the player
            //is currently viewing the prices menu.
            if (!pricesMenu)
            {
                pricesAction();
            }
            
            //If they went to the Bronx allow the user to visit the loan shark and bank.
            if(choice == 0)
            {
                //If the player still has a loan to repay prompt them to go to the loan shark.
                if (player.getLoan() > 0)
                {
                    if ((choice = Dialog.ask(Dialog.D_YES_NO, "Do you want to visit the loan shark?"))
                        == Dialog.YES)
                    {
                        //Player wants to visit the loan shark.
                        if ((choice = Dialog.ask("What do you want to do?", sharkChoices, 0)) == 0)
                        {
                            //Player wants to pay back their loan.
                        
                            //Set the max amount to the loan value if they can afford to pay it all back.
                            //Or their max cash value if they don't have enough.
                            if (player.getCash() > player.getLoan())
                            {
                                amount = player.getLoan();
                            }
                            else
                            {
                                amount = player.getCash();
                            }
                        
                            PromptDialog payLoanDialog = new PromptDialog("Payback loan.",
                                "Repay loan ($" + CurrencyConverter.convertCurrency(amount) + "): ", amount, true);
                                
                            payLoanDialog.show();
                            
                            //Set the amount to 0 if the user cancels the prompt.
                            if(payLoanDialog.isCancelled()) 
                            {
                                amount = 0;
                            }
                            else
                            {
                                amount = Integer.parseInt(payLoanDialog.getData());
                            }                        
    
                            if (amount > player.getCash())
                            {
                                //The player doesn't have that much.
                                Dialog.alert("You don't have $" + 
                                CurrencyConverter.convertCurrency(amount) + ".");
                            }
                            else if (amount < 0)
                            {
                                Dialog.alert("The loan shark says \"Do you think you can trick me?\"");
                            }
                            else
                            {
                                player.setCash(player.getCash() - amount);
                                player.setLoan(player.getLoan() - amount);
                                
                                if (player.getLoan() < 0)
                                {
                                    player.setLoan(0);
                                    Dialog.alert("The loan shark says: \"Thanks for the tip.\"");
                                }
                            }
                        }//end if ((choice = Dialog.ask("What do you....
                        else
                        {
                            //The player wants to take our more money.
                            
                            //No loans can be taken out after the 20th day (to prevent cheating).
                            if (day > 20)
                            {
                                Dialog.alert("The loan shark is closed.");
                            }
                            //Stop loan withdrawls after it has reached 2 million.
                            else if (player.getLoan() > 2000000)
                            {
                                Dialog.alert("The loan shark refused to give you more money.");
                            }
                            //Allow the player to withdraw more.
                            else
                            {
                                PromptDialog payLoanDialog = new PromptDialog("Take out loan.",
                                    "Take out loan ($" + CurrencyConverter.convertCurrency(2000000) + "): ", 2000000, true);
                                
                                payLoanDialog.show();
                                
                                //Set the amount to 0 if the user cancels the prompt.
                                if(payLoanDialog.isCancelled()) 
                                {
                                    amount = 0;
                                }
                                else
                                {
                                    amount = Integer.parseInt(payLoanDialog.getData());
                                }
                                
                                if (amount > 2000000)
                                {
                                    Dialog.alert("The load shark says: \"I'm not giving you that much!\"");
                                    amount = 2000000;
                                }
                                
                                player.setLoan(player.getLoan() + amount);
                                player.setCash(player.getCash() + amount);
                                
                            }//end else
                        }//end else
                        
                    }// end if (Dialog.ask
                }// end if (player.getLoan() > 0)
                
                if ((choice = Dialog.ask(Dialog.D_YES_NO, "Do you want to visit the bank?"))
                    == Dialog.YES)
                {
                    if ((choice = Dialog.ask("What do you want to do?", bankChoices, 0)) == 0)
                    {
                        //Player is going to deposit.
                        
                        PromptDialog payLoanDialog = new PromptDialog("Deposit into bank.",
                            "Deposit amount ($" + CurrencyConverter.convertCurrency(player.getCash()) + 
                            "): ", player.getCash(), true);
                        payLoanDialog.show();

                        //Set the amount to 0 if the user cancels the prompt.
                        if(payLoanDialog.isCancelled()) 
                        {
                            amount = 0;
                        }
                        else
                        {
                            amount = Integer.parseInt(payLoanDialog.getData());
                        } 
                        
                        if (amount > player.getCash())
                        {
                            //Player doesn't have that much.
                            Dialog.alert("You don't have $" + 
                            CurrencyConverter.convertCurrency(amount) + ".");
                        }
                        else
                        {
                            player.setCash(player.getCash() - amount);
                            player.setBank(player.getBank() + amount);
                        }
                    }//end if (Dialog.ask...
                    else
                    {
                        //Player is going to withdraw.
                        PromptDialog payLoanDialog = new PromptDialog("Withdraw from bank.",
                            "Withdraw amount ($" + 
                            CurrencyConverter.convertCurrency(player.getBank()) + "): ", player.getBank(), true);
                            
                        payLoanDialog.show();
                        
                        //Set the amount to 0 if the user cancels the prompt.
                        if(payLoanDialog.isCancelled()) 
                        {
                            amount = 0;
                        }
                        else
                        {
                            amount = Integer.parseInt(payLoanDialog.getData());
                        }
                            
                            
                        if (amount > player.getBank())
                        {
                            //Player doesn't have that much.
                            Dialog.alert("You don't have $" + 
                            CurrencyConverter.convertCurrency(amount) + " in the bank.");
                        }
                        else
                        {
                            player.setBank(player.getBank() - amount);
                            player.setCash(player.getCash() + amount);
                        }
                    }//end else
                }
            }
            
            if (locations[currentLocation].copPresent())
            {
                //Shit, a cop!
                //Call a cop encounter if the cop isn't dead.
                if (cop.getHealth() > 0)
                    copEncounter();
            }
            else if ((freeDrugs = locations[currentLocation].freeDrugs()) > 0)
            {
                //Player found some free drugs!
                //Randomly choose which one.
                randValue = random.nextInt(drugs.length);
                
                //Ensure we have pocket space.
                if (player.getFreeSlots() > 0)
                {
                    if (freeDrugs > player.getFreeSlots() && player.getFreeSlots() > 0)
                    {
                        freeDrugs = player.getFreeSlots();
                    }

                    drugInventory[randValue] += freeDrugs;
                    player.setFreeSlots(player.getFreeSlots() - freeDrugs);
                    
                    Dialog.alert("You found " + freeDrugs + " " + drugs[randValue].getName() + 
                        " on a dead guy in the alley.");
                }
            }
            else if (locations[currentLocation].trenchCoatForSale())
            {
                //Player can buy a new trench coat.
                randValue = random.nextInt(100);
                randValue += (((int)(player.getTotalSlots() % 100) / 10) + 1) * 200;
                
                if ((choice = Dialog.ask(Dialog.D_YES_NO, "Buy a new trenchcoat for $" + 
                    CurrencyConverter.convertCurrency(randValue) + "?"))
                    == Dialog.YES)
                {
                    //Can the player afford the new trench coat?
                    if (player.getCash() >= randValue)
                    {
                        player.setCash(player.getCash() - randValue);
                        player.setTotalSlots(player.getTotalSlots() + 10);
                        player.setFreeSlots(player.getFreeSlots() + 10);
                    }
                    else
                    {
                        Dialog.alert("You don't have $" + 
                            CurrencyConverter.convertCurrency(randValue) + ".");
                    }
                }
            }            
            else if (locations[currentLocation].gunForSale())
            {
                //Player can buy a new gun.
                weapon = Weapons.getRandomWeapon();
                
                if ((choice = Dialog.ask(Dialog.D_YES_NO, "Buy a " + 
                    Weapons.getName(weapon) + " for $" +
                    CurrencyConverter.convertCurrency(Weapons.getPrice(weapon)) + "?")) == Dialog.YES)
                {
                    if (player.getCash() >= Weapons.getPrice(weapon))
                    {
                        player.setWeapon(weapon);
                        player.setCash(player.getCash() - Weapons.getPrice(weapon));
                    }
                    else
                    {
                        Dialog.alert("You don't have $" + 
                            CurrencyConverter.convertCurrency(Weapons.getPrice(weapon)) + ".");
                    }
                    
                }
            }
            else if (locations[currentLocation].getMugged())
            {
                //The player was mugged.
                //Let them get away if they have less than $50.
                if (player.getCash() > 50)
                {
                    //A random value up to 60% of their cash is stolen.
                    randValue = player.getCash() * (random.nextInt(60) + 1) / 100;
                    player.setCash(player.getCash() - randValue);
                    Dialog.alert("You were mugged.  $" + 
                        CurrencyConverter.convertCurrency(randValue) + " was stolen.");
                }
                else
                {
                    Dialog.alert("Someone tried to mug you but you kicked them in the balls and got away.");
                }
                
            }
            else if (locations[currentLocation].randomEncounter())
            {
                //Someone will babble at the player.
                
                Dialog.alert(Quotes.getPerson(random.nextInt(Quotes.PEOPLE_COUNT)) + 
                    Quotes.getAction(random.nextInt(Quotes.ACTION_COUNT)) + ": \"" +
                    Quotes.getQuote(random.nextInt(Quotes.QUOTE_COUNT)) + "\"");
            }
            //Increment the day.
            ++day;
            
            //Add 10% interest to the player's loan.
            player.setLoan((int)(player.getLoan() * 1.1));
            
            //Add 5% interest to the player's bank.
            player.setBank((int)(player.getBank() * 1.05));
            
            //Randomly select if any prices bottomed or topped out.
            if (random.nextInt(10) == 0)
            {
                //Randomly choose if a drug tops or bottoms out.
                if (random.nextInt(2) == 0)
                {
                    //Drug bottomed out.
                    //Determine which drugs can bottom out.
                    int[] bottomDrugs = new int[currentDrugs.length];
                    int drugCounter = 0;
                    
                    for(count = 0; count < currentDrugs.length; ++count)
                    {
                        if (drugs[currentDrugs[count]].getBottomsOut())
                        {
                            bottomDrugs[drugCounter] = currentDrugs[count];
                            ++drugCounter;
                        }
                    }
                    
                    if (drugCounter > 0)
                    {
                        //Randomly select a drug to bottom out if there are any bottomed out
                        //drugs in the current location.
                        highLowDrug = bottomDrugs[random.nextInt(drugCounter)];
                        
                        //Alert the user of the bottomed out drug.
                        Dialog.alert(drugs[highLowDrug].getBottomOutQuote());
                        
                        //Set the new bottomed out value for the drug.
                        drugs[highLowDrug].newBottomOutPrice();                        
                    }
                    
                }
                else
                {
                    //Drug topped out.
                    //Randomly select a drug to top out.
                    highLowDrug = random.nextInt(currentDrugs.length);
                    
                    //Select a random alert message
                    if (random.nextInt(2) == 0)
                    {
                        Dialog.alert("Cops made a big " + drugs[currentDrugs[highLowDrug]].getName() +
                            " bust! Prices are outrageous!");
                    }
                    else
                    {
                        Dialog.alert("Addicts are buying " + drugs[currentDrugs[highLowDrug]].getName() + 
                            " at ridiculous prices!");
                    }
                    
                    //Set the new topped out value for the drug.
                    drugs[currentDrugs[highLowDrug]].newTopOutPrice();
                }
            }            
            
            //Update the screen with the new player status.
            updateGameScreen();
            
            //Is the game over?
            if (day > 31)
            {
                endGame();
            }
            
        }// end if((choice...
    }// end jetAction
    
    //Officer Hardass found the player while they were jetting.
    private void copEncounter()
    {
        boolean keepGoing = true;
        boolean sendFeedback;
        String[] noWeapon = {"Stand", "Run"};
        String[] withWeapon = {"Fight", "Run"};
        String promptMessage = cop.getName() + " is chasing you!";
        StringBuffer feedbackMessage = new StringBuffer(cop.getName() + " is chasing you!");
        
        while (keepGoing)
        {
            //Player does not have weapon.  Display the stand or run option.
            if (player.getWeapon() == Weapons.NONE)
            {
                if (Dialog.ask(promptMessage, noWeapon, 0) == 0)
                {
                    Dialog.alert("You stand there like a dumbass...");
                    //Determine if the player was shot.
                    if (random.nextInt(3) == 0)
                    {
                        //Player was shot.
                        player.setHealth(player.getHealth() - Weapons.getDamage(cop.getWeapon()));
                        
                        updateGameScreen();
                        promptMessage = cop.getName() + " hits you, man!";
                        
                        if(player.getHealth() <= 0)
                        {
                            Dialog.alert("You're dead! Game over.");
                            keepGoing = false;
                            endGame();
                        }
                    }
                    else
                    {
                        promptMessage = cop.getName() + " shoots at you... and misses!";
                    }
                }
                else
                {
                    //Player is trying to run away.
                    if (random.nextInt(3) == 0)
                    {
                        Dialog.alert("You got away!");
                        keepGoing = false;
                    }
                    //Determine if the player was shot.                    
                    else if (random.nextInt(3) == 0)
                    {
                        //Player was shot.
                        player.setHealth(player.getHealth() - Weapons.getDamage(cop.getWeapon()));
                        updateGameScreen();
                        promptMessage = cop.getName() + " hits you, man!";
                        
                        if(player.getHealth() <= 0)
                        {
                            Dialog.alert("You're dead! Game over.");
                            keepGoing = false;
                            endGame();
                        }                        
                    }
                    else
                    {
                        //Player was not shot.
                        promptMessage = cop.getName() + " shoots at you... and misses!";
                    }

                }
            }
            else
            {
                //Player has a weapon.  Display the fight or run option.
                if (Dialog.ask(feedbackMessage.toString(), withWeapon, 0) == 0)
                {
                    //Player decided to fight.
                    
                    feedbackMessage = feedbackMessage.delete(0, feedbackMessage.length());
                    sendFeedback = false;
                    
                    //Determine if the player was shot.
                    if (random.nextInt(3) == 0)
                    {
                        //Player was shot.
                        player.setHealth(player.getHealth() - Weapons.getDamage(cop.getWeapon()));
                        updateGameScreen();
                        sendFeedback = true;
                        feedbackMessage.append(cop.getName());
                        feedbackMessage.append(" hits you, man!");
                        
                        //Player was shot.
                        player.setHealth(player.getHealth() - Weapons.getDamage(cop.getWeapon()));
                        
                        if(player.getHealth() <= 0)
                        {
                            Dialog.alert("You're dead! Game over.");
                            keepGoing = false;
                            endGame();
                        }
                    }
                    else
                    {
                        sendFeedback = true;
                        feedbackMessage.append(cop.getName());
                        feedbackMessage.append(" shoots at you... and misses!");
                    }
                    
                    //Determine if the cop was shot.
                    if (random.nextInt(4) == 0)
                    {
                        //Cop was shot.
                        cop.setHealth(cop.getHealth() - Weapons.getDamage(player.getWeapon()));
                        
                        if (sendFeedback)
                                feedbackMessage.append("\n");
                        
                        if (cop.getHealth() > 0)
                        {
                            feedbackMessage.append("You hit ");
                            feedbackMessage.append(cop.getName());
                            feedbackMessage.append(".");
                        }
                        else
                        {
                            Dialog.alert("You killed " + cop.getName() + "!");
                            keepGoing = false;
                        }
                    }
                    else
                    {
                        //Cop was not shot.
                        if (sendFeedback)
                            feedbackMessage.append("\n");
                            
                        feedbackMessage.append("You missed ");
                        feedbackMessage.append(cop.getName());
                    }
                    
                }
                else
                {
                    //Player decided to run.                    
                    feedbackMessage = feedbackMessage.delete(0, feedbackMessage.length());
                    
                    //Player is trying to run away.
                    if (random.nextInt(3) == 0)
                    {
                        Dialog.alert("You got away!");
                        keepGoing = false;
                    }
                    //Determine if the player was shot.                    
                    else if (random.nextInt(3) == 0)
                    {
                        //Player was shot.
                        player.setHealth(player.getHealth() - Weapons.getDamage(cop.getWeapon()));
                        
                        updateGameScreen();
                        
                        feedbackMessage.append(cop.getName());
                        feedbackMessage.append(" hits you, man!");

                        //Player was shot.
                        player.setHealth(player.getHealth() - Weapons.getDamage(cop.getWeapon()));
                        
                        if(player.getHealth() <= 0)
                        {
                            Dialog.alert("You're dead! Game over.");
                            keepGoing = false;
                            endGame();
                        }                        
                        
                    }
                    else
                    {
                        //Player was not shot.
                        feedbackMessage.append(cop.getName());
                        feedbackMessage.append(" shoots at you... and misses!");
                    }
                }
            }

        }       
    }// end copEncounter()
    
    //Called when the game is over.
    private void endGame()
    {
        int highScorePosition, count;
        boolean checkForHighScore, keepGoing;
        String playerName;
        
        //Find out how much cash the player has.
        int score = player.getCash() + player.getBank() - player.getLoan();
        
        //Add the value of all drugs they currently own to their score.
        for(count = 0; count < drugs.length; ++count)
        {
            score += drugInventory[count] * drugs[count].getCurrentPrice();
        }

        Dialog.alert("Game over!  Your score: $" + CurrencyConverter.convertCurrency(score));
        
        //Check for a new high score.
        highScorePosition = 5;
        checkForHighScore = true;
        
        //See if the current score is higher then an existing high score.
        while (checkForHighScore && highScorePosition != 0)
        {
            if (score > highScoreValues[highScorePosition - 1])
                --highScorePosition;
            else
                checkForHighScore = false;
        }

        if (highScorePosition < 5)
        //User has a new high score!
        {
            //Alert the user of the new high score and prompt for their name.
            PromptDialog getNameDialog = new PromptDialog("New High Score!",
                "Enter your name: ", 0, false);
            getNameDialog.show();
            
            //Set the name to "" if the user cancels the prompt.
            if(getNameDialog.isCancelled()) 
            {
                playerName = "";
            }
            else
            {
                playerName = getNameDialog.getData();
            }
            
            keepGoing = true;
            count = 4;
            
            //Drop the lowest score off the bottom of the list, shift other 
            //high scores down and then insert the new high score.
            while (keepGoing)
            {
                if (highScorePosition == count)
                {
                    highScoreValues[count] = score;
                    highScoreNames[highScorePosition] = playerName;
                    keepGoing = false;
                }
                else
                {
                    highScoreValues[count] = highScoreValues[count - 1];
                    highScoreNames[count] = highScoreNames[count - 1];
                    --count;
                }
            }
            
            //Save the new high scores.
            dopeWarsHighScores.setNames(highScoreNames);
            dopeWarsHighScores.setScores(highScoreValues);
            
            synchronized (store)
            {
                store.setContents(dopeWarsHighScores);
                store.commit();
            }
        }
            
        highScoreScreen = new HighScoreScreen(highScoreNames, highScoreValues);
        pushScreen(introScreen);
        pushScreen(highScoreScreen);
        popScreen(gameScreen);
    }//end endGame()     
}
