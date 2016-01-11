/**
 * DopeWarsSyncItem.java
 *
 * Handles the backup and restore of the high scores to/from the users Desktop.
 *
 * Author:  Mark Sohm
 */

package com.msohm.pub.dopeWars;

import net.rim.device.api.system.*;
import net.rim.device.api.util.*;
import net.rim.device.api.synchronization.*;
import java.util.*;
import net.rim.device.api.i18n.Locale;


public final class DopeWarsSyncItem extends SyncItem
{
    private String[] permNames = new String[5];
    private int permScores[] = new int[5];
    private static DopeWarsHighScores permHighScores;
    private static final int FIELDTAG_NAME = 1;
    private static final int FIELDTAG_SCORE = 2;
    
    private static PersistentObject store;

    //This store is static so that all instances of this class use the same Persistent Store.
    static 
    {
        //Unique identifier for the persistent store object.
        //Long value = com.msohm.dopeWars
        store = PersistentStore.getPersistentObject(0x120d120fa2875f50L);
    }

    public DopeWarsSyncItem()
    {
    }

    //The name that will be used to reference this SyncItem.
    //It is shown in the Desktop Manager under backup/restore advanced.
    public String getSyncName()
    {
        return "Dope Wars High Scores";
    }
    
    //Localization is not supported in this example.
    public String getSyncName(Locale locale)
    {
        return null;
    }

    //The version of this SyncItem.
    public int getSyncVersion()
    {
        return 1;
    }
    
    //Formats the data to the specification required by the Desktop Manager.
    public boolean getSyncData(DataBuffer db, int version)
    {
        
        int count;
        boolean retVal = true;
        String tempString;
        
        synchronized (store) 
        {
            //Check to see if the persistent store object exists on the BlackBerry.
            if (store.getContents() != null)
            {
                //Store exists, retrieve data from store.
                permHighScores = (DopeWarsHighScores)store.getContents();
                permNames = permHighScores.getNames();
                permScores = permHighScores.getScores();
            }
        }
        
        //Format data that will be interpreted by the Desktop Manager.
        //Format is Length Type Data
        //Length is a short, type is a byte and data is the length specified by Length.
        //Data must be structured in this format to be understood by the Desktop Manager.
        try
        {
            for (count = 0; count < 5; ++count)
            {
                //Write the name.
                db.writeShort(permNames[count].length() + 1);
                db.writeByte(FIELDTAG_NAME);                
                db.write(permNames[count].getBytes());
                db.writeByte(0);
                //Write the age.
                tempString = permScores[count] + "";
                db.writeShort(tempString.length() + 1);
                db.writeByte(FIELDTAG_SCORE);
                db.write(tempString.getBytes());
                db.writeByte(0);
        }
        } catch (Exception e)
        {
            retVal = false;
        }
        
        //Return true if all data was processed.
        return retVal;
    }

    //Interprets and stores the data sent from the Desktop Manager.    
    public boolean setSyncData(DataBuffer db, int version)
    {

        int length, nameCount = 0, scoreCount = 0;
        boolean retVal = true;
        
        try
        {
            //Read until the end of the Databuffer.
            while (db.available() > 0)
            {
                //Read the length of the data.
                length = db.readShort();
                
                //Set the byte array to the length of the data.
                byte[] bytes = new byte[length];
                
                //Determine the type of data to be read (name or age).
                switch (db.readByte()) 
                {
                    case FIELDTAG_NAME:
                        //Read the name from the Databuffer.  
                        //Convert and store it in the String array.
                        db.readFully(bytes);
                        permNames[nameCount] = new String(bytes).trim();
                        ++nameCount;
                        break;
                        
                    case FIELDTAG_SCORE:
                        //Read the age from the Databuffer.
                        //Convert and store it in the int array.
                        db.readFully(bytes);
                        permScores[scoreCount] = (int)(Integer.parseInt(new String(bytes).trim()));
                        ++scoreCount;
                        break;
                }
            }
            
        } catch (Exception e)
        {
            retVal = false;
        }
       
        try
        {
            //Store the new data in the persistent store object.
            permHighScores = new DopeWarsHighScores(permNames, permScores);
            store.setContents(permHighScores);
            store.commit();            
        } catch (Exception e)
        {
            retVal = false;
        }
        
        //Return true if the data was successfully restored, or false if it was not.
        
        return retVal;
    }
}
