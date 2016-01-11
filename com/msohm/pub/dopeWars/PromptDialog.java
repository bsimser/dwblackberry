/**
 * Dope Wars Player Name Dialog
 *
 * Author:  Mark Sohm
 *
 * Custom dialog with a text entry field that prompts the user for their 
 * name that will be used in the game.
 */

package com.msohm.pub.dopeWars;

import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.system.*;
import java.util.*;

public final class PromptDialog extends Dialog
{
    private EditField value;
    private boolean intFilter;
    
    public PromptDialog(String msg, String question, boolean integerFilter)
    {
        super(Dialog.D_OK, msg, 1, Bitmap.getPredefinedBitmap(Bitmap.EXCLAMATION), Manager.FOCUSABLE);
           
        intFilter = integerFilter;
           
        //Is there an integer filter on this field?
        if (intFilter)   
            value = new EditField(question, "", 10, EditField.EDITABLE | EditField.FILTER_INTEGER);
        else
            value = new EditField(question, "", 10, EditField.EDITABLE);
            
        //Add an EditField to the standard OK dialog.            
        add(value);
        value.setFocus();
    }

    public boolean keyChar(char key, int status, int time) 
    {
        //Override key commands
        
        switch (key) 
        {
            case Characters.ENTER:
                //Update main class with the name and alert it that the new name is ready.
                DopeWars.setNewPromptValue(value.getText());
                this.close();
                break;
                
            case Characters.BACKSPACE:
                if (value.getTextLength() > 0)
                {
                    value.setText(value.getText().substring(0, value.getTextLength() - 1));
                }
                break;
                
            case Characters.ESCAPE:
                if (value.getTextLength() > 0)
                {
                    //If its an intFilter is enabled close the dialog with a 0 value.
                    if (intFilter)
                    {
                        DopeWars.setNewPromptValue("0");
                        this.close();
                    }
                    else
                    {
                        value.setText(value.getText().substring(0, value.getTextLength() - 1));
                    }
                }
                break;                
                
            default:
                if (intFilter)
                {
                    if (Character.isDigit(key))
                        value.setText(value.getText() + key);
                }
                else
                {
                    value.setText(value.getText() + key);
                }
                break;
        }

        return true;
    }    
    
    //Update main class with the name and alert it that the new name is ready.
    public boolean trackwheelClick(int status, int time) 
    {
        DopeWars.setNewPromptValue(value.getText());
        this.close();
        return true;
    }
}
       
