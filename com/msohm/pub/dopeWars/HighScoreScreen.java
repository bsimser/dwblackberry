/**
 * High Score Screen
 *
 * Author:  Mark Sohm
 *
 * Screen that displays the Dope Wars high scores.
 */

package com.msohm.pub.dopeWars;

import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.system.*;
import java.util.*;
import com.msohm.dopeWarsLibrary.CurrencyConverter;

public class HighScoreScreen extends MainScreen
{
    public HighScoreScreen(String[] names, int[] scores)
    {
        super();
        
        //Screen is made up of two VerticalFieldManagers in a HorizontalFieldManager.
        //Gives the effect of having two columns for the fields.
        HorizontalFieldManager backGround = new HorizontalFieldManager();
        VerticalFieldManager leftColumn = new VerticalFieldManager();
        VerticalFieldManager rightColumn = new VerticalFieldManager();
        
        LabelField displayNames[] = new LabelField[5];
        LabelField displayScores[] = new LabelField[5];
        int count;
        
        LabelField title = new LabelField("Dope Wars High Scores", LabelField.ELLIPSIS | LabelField.USE_ALL_WIDTH);
        setTitle(title);
        
        //Column headings.
        LabelField leftTitle = new LabelField("Player      ");
        leftColumn.add(leftTitle);
        LabelField rightTitle = new LabelField("Score");
        rightColumn.add(rightTitle);
        
        //Blank label fields to add a blank line.
        leftColumn.add(new LabelField(""));
        rightColumn.add(new LabelField(""));
        
        //Add fields that list the high score names and values.
        for (count = 0; count < 5; count++)
        {
            
            displayNames[count] = new LabelField(names[count]);
            displayScores[count] = new LabelField("$" + 
                CurrencyConverter.convertCurrency(scores[count]));
            
            leftColumn.add(displayNames[count]);
            rightColumn.add(displayScores[count]);

        }
        
        backGround.add(leftColumn);
        backGround.add(rightColumn);
        add(backGround);
    }
    
    //Close the high score screen whenever a key is pressed.
    public boolean keyChar(char key, int status, int time) 
    {
        this.close();
        return true;
    }    

    //Close the high score screen if the trackwheel is clicked.    
    public boolean trackwheelClick(int status, int time)
    {
        this.close();
        return true;
    }
}

