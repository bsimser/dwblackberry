/**
 * Dope Wars High Scores
 *
 * Author:  Mark Sohm
 *
 * The persistant class used to store the high score list.
 */

package com.msohm.pub.dopeWars;

import net.rim.device.api.system.*;
import net.rim.device.api.util.*;
import net.rim.device.api.synchronization.*;
import java.util.*;


public final class DopeWarsHighScores implements Persistable
{
    //Array of string and ints to hold the high score names and score values.
    private String[] highNames = new String[5];
    private int highScores[] = new int[5];
    
    public DopeWarsHighScores(String[] names, int scores[])
    {
        highNames = names;
        highScores = scores;
    }
    public String[] getNames()
    {
        return highNames;
    }
    
    public void setNames(String[] names)
    {
        highNames = names;
    }
    
    public int[] getScores()
    {
        return highScores;
    }
    
    public void setScores(int[] scores)
    {
        highScores = scores;
    }
}
