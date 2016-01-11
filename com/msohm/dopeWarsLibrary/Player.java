/**
 * Player
 *
 * Author:  Mark Sohm
 *
 * Dope Wars players.
 */

package com.msohm.dopeWarsLibrary;

public final class Player
{
    
    private String name;    //The player's name.
    private int cash;       //The players's cash balance.
    private int loan;       //The players's loan balance.
    private int bank;       //The players's bank account balance.
    private int freeSlots;  //The number of free slots in the players trench coat.
    private int totalSlots; //The number of slots in the players trench coat.
    private int health;     //The players's current health.
    private int weapon;     //The player's current weapon.
    
    public Player(String playerName, int playerCash, int playerLoan, int playerBank, int playerFreeSlots,
        int playerTotalSlots, int playerHealth, int playerWeapon)
    {
        name = playerName;
        cash = playerCash;
        loan = playerLoan;
        bank = playerBank;
        freeSlots = playerFreeSlots;
        totalSlots = playerTotalSlots;
        health = playerHealth;
        weapon = playerWeapon;
    }
    
    public String getName()
    {
        return name;
    }
    
    public int getCash()
    {
        return cash;
    }
    
    public void setCash(int playerCash)
    {
        cash = playerCash;
    }
    
    public int getLoan()
    {
        return loan;
    }
    
    public void setLoan(int playerLoan)
    {
        loan = playerLoan;
    }
    
    public int getBank()
    {
        return bank;
    }
    
    public void setBank(int playerBank)
    {
        bank = playerBank;
    }
    
    public int getFreeSlots()
    {
        return freeSlots;
    }
    
    public void setFreeSlots(int playerFreeSlots)
    {
        freeSlots = playerFreeSlots;
    }
    
    public int getTotalSlots()
    {
        return totalSlots;
    }
    
    public void setTotalSlots(int playerTotalSlots)
    {
        totalSlots = playerTotalSlots;
    }    

    public int getHealth()
    {
        return health;
    }
    
    public void setHealth(int playerHealth)
    {
        health = playerHealth;
    }
    
    public int getWeapon()
    {
        return weapon;
    }
    
    public void setWeapon(int playerWeapon)
    {
        weapon = playerWeapon;
    }
            
}
