/**
 * Quotes
 *
 * Author:  Mark Sohm
 *
 * Random babble that the player hears during the game.
 */

package com.msohm.dopeWarsLibrary;

public class Quotes
{
    
    private static final String[] quote = {"Is that a fruit in your hand?",
        "Your pockets smell funny.",
        "I like snails!",
        "I think we're winning the war on drugs.",
        "Are you high or something?",
        "I have a two headed besenorker in my pocket.",
        "A bee hurt my thumb.",
        "A box for free, a house for me!",
        "I took a dump over there.",
        "I've been to the moon you know.",
        "Does your mother know you're a dope dealer?",
        "Winners don't do drugs... unless they do.",
        "You must be from Vancouver",
        "I know how much wood a wood chuck can chuck.",
        "I don't believe in things like Atlantis or Australia.",
        "Want some shake 'n bake bacon?",
        "This is my real ass.",
        "Shhh, listen...  Do you smell something?",
        "I once deked out a cat.",
        "I love watching the kangeroo Olympics.",
        "Einstein invented water.",
        "That looks berry fun.",
        "My igloo burned down last night.",
        "When I opened the door my dog flew away.",
        "A plumber punched me when I asked for crack.",
        "The door is a jar.",
        "Cops ate my dad.",
        "Podderly sop.",
        "Are you motivated to deliver?",
        "Hay is for horses but weed is for me!",
        "My girlfriend beats me!",
        "Touch my breath and I'll blow you.",
        "Woot woot!",
        "The answer is 42."};
        
    private static final String[] people = {"An old lady ", "A dirty man ", 
        "Some punk ", "A crazy woman "};
        
    private static final String[] actions = {"cries", "screams", "whispers", "yells", "says", 
        "says"};
        
    public static final int QUOTE_COUNT = quote.length;
    public static final int PEOPLE_COUNT = people.length;
    public static final int ACTION_COUNT = actions.length;
    
    public Quotes()
    {}
    
    public static String getQuote(int quoteIndex)
    {
        return quote[quoteIndex];
    }
    
    public static String getPerson(int peopleIndex)
    {
        return people[peopleIndex];
    }

    public static String getAction(int actionIndex)
    {
        return actions[actionIndex];
    }    
}
