/**
 * Weapons
 *
 * Author:  Mark Sohm
 *
 * Weapons available in Dope Wars.
 */

package com.msohm.dopeWarsLibrary;

import java.util.Random;

public final class Weapons
{
    
    //Weapon identifiers.
    public static final int NONE = 0;
    public static final int RUGER = 1;
    public static final int BARETTA = 2;
    public static final int SAT_NIGHT_SPECIAL = 3;
    public static final int t38_SPECIAL = 4;
    
    private static final String[] names = {"None", "Ruger", "Baretta", "Saturday Night Special", ".38 Special"};
    private static final int[] prices = {0, 2900, 3000, 3100, 3500};
    private static final int damage[] = {0, 4, 8, 12, 16};
    
    public Weapons()
    {}
    
    public static String getName(int weapon)
    {
        return names[weapon];
    }
    
    public static int getPrice(int weapon)
    {
        return prices[weapon];
    }
    
    public static int getDamage(int weapon)
    {
        return damage[weapon];
    }
    
    public static int getRandomWeapon()
    {
        Random rand = new Random();
        return rand.nextInt(4) + 1;
    }
}
