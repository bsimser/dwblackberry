/**
 * Drug
 *
 * Author:  Mark Sohm
 *
 * Drugs that are bought and sold in Dope Wars.
 */

package com.msohm.dopeWarsLibrary;

import java.util.Random;
import java.lang.Math;

public final class Drug
{
    private String name;            //The drug name.
    private int minPrice, maxPrice; //The maximum and minimum prices a drug can reach.
    private boolean bottomsOut;     //Can the price of this drug bottom out/crash?
    private String bottomOutQuote;  //The quote used when a drug price bottoms out.
    private static Random priceVariance = new Random();   //Random price variance.  Static to provide more randomness.
    private int currentPrice;       //The current drug price.
    
    public Drug(String drugName, int minDrugPrice, int maxDrugPrice, 
        boolean drugBottomsOut, String drugBottomsOutQuote)
    {
        name = drugName;
        minPrice = minDrugPrice;
        maxPrice = maxDrugPrice;
        bottomsOut = drugBottomsOut;
        bottomOutQuote = drugBottomsOutQuote;
    }
    
    public String getName()
    {
        return name;
    }
    
    public String getBottomOutQuote()
    {
        return bottomOutQuote;
    }
    
    //Return a random price between the min and max values.
    public void randomizePrice()
    {
        currentPrice =  (int)(((maxPrice - minPrice) * priceVariance.nextFloat()) + minPrice);
    }
    
    //Return a random bottom out value.
    public void newBottomOutPrice()
    {
        int lowVal = 0;
        
        while (lowVal == 0)
        {
            lowVal =  (int)(minPrice - (priceVariance.nextFloat() * minPrice));
        }
        currentPrice = lowVal;
    }
    
    //Return a random extreme high price.
    //Ensure that the random extreme high price is greater than the maxValue.
    public void newTopOutPrice()
    {
        int highVal = 0;
        
        while (highVal < maxPrice)
        {
            highVal = (int)(maxPrice * priceVariance.nextFloat() * 5);
        }
        
        currentPrice = highVal;
    }
        
    //Return the current drug price.
    public int getCurrentPrice()
    {
        return currentPrice;
    }
    
    //Return bottomsOut.
    public boolean getBottomsOut()
    {
        return bottomsOut;
    }
}
