/**
 * Location
 *
 * Author:  Mark Sohm
 *
 * Location where drugs are bought and sold in Dope Wars.
 */

package com.msohm.dopeWarsLibrary;

import java.util.Random;
import java.lang.Math;

public final class Location
{
    
    private String name;    //The name of the location.
    private int minDrugs;   //The minimum number of drugs available in this location.
    private int maxDrugs;   //The maximum number of drugs available in this location.
    private static Random randValue = new Random(); //Random value for probability calculations.  Static to provide more randomness.
    private int numDrugs;
    
    public Location(String locationName, int minNumDrugs, int maxNumDrugs)
    {
        name = locationName;
        minDrugs = minNumDrugs;
        maxDrugs = maxNumDrugs;
    }
    
    public String getName()
    {
        return name;
    }
    
    //Return a random number of drugs currently available in this location.
    //The size is between the min and max value.
    public int getRandomNumDrugs()
    {
        numDrugs = Math.abs(randValue.nextInt(maxDrugs - minDrugs)) + minDrugs;
        return numDrugs;
    }
    
    //Return a random number of drugs currently available in this location.
    //The size is between the min and max value.
    public int getCurrentNumDrugs()
    {
        return numDrugs;
    }    
    
    //Is there a cop in this location?
    public boolean copPresent()
    {
        if (Math.abs(randValue.nextInt(25)) <= 1)
            return true;
        else
            return false;
    }
    
    //Did we score some free drugs in this location?
    //A value greater than 0 indicates we found some.
    public int freeDrugs()
    {
        if (Math.abs(randValue.nextInt(20)) <= 1)
            return Math.abs(randValue.nextInt(10)) + 1;
        else
            return 0;
    }
    
    //Did the player get mugged?
    public boolean getMugged()
    {
        if (Math.abs(randValue.nextInt(20)) <= 1)
            return true;
        else
            return false;
    }    
    
    //Can we buy a gun in this location?
    public boolean gunForSale()
    {
        if (Math.abs(randValue.nextInt(30)) <= 1)
            return true;
        else
            return false;
    }
    
    //Can we buy a bigger trench coat in this location?
    public boolean trenchCoatForSale()
    {
        if (Math.abs(randValue.nextInt(25)) <= 1)
            return true;
        else
            return false;
    }

    //Is there a random encounter (display quote).
    public boolean randomEncounter()
    {
        if (Math.abs(randValue.nextInt(4)) == 1)
            return true;
        else
            return false;
    }
}
