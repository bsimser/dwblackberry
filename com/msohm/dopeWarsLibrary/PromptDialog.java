/*
 * PromptDialog.java
 *
 * 
 */

package com.msohm.dopeWarsLibrary;

import net.rim.device.api.system.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;

public class PromptDialog extends PopupScreen
{
    private UiApplication _app;                 // Current UI application instance.
    private BasicEditField _editField;          // EditField for entering file name
    private ButtonField _okButton;              // Ok button for dismissing dialog.
    private ButtonField _maxButton;             // Max button to fill the prompt with the maximum value.
    private ButtonField _cancelButton;          // Cancel button for dismissing dialog.
    private boolean _cancelled;                 // boolean indicating whether dialog was cancelled.
    private int _maxValue;
    private boolean _integerFilter;


    public PromptDialog(String msg, String question, int maxValue, boolean integerFilter) 
    {
        super(new VerticalFieldManager());
        
        // Set the UI instance as the one that created the dialog.
        _app = UiApplication.getUiApplication();
        
        // Retrieve the default VerticalFieldManager from the PopupScreen.
        VerticalFieldManager manager = (VerticalFieldManager)getDelegate();
        manager.add(new LabelField(msg));
        
        // Create the EditField and add it to the screen.
        if (integerFilter)
        {
            _editField = new BasicEditField(question, "", 10, EditField.EDITABLE | EditField.FILTER_INTEGER);
        }
        else
        {
            _editField = new EditField(question, "", 10, EditField.EDITABLE);
        }
        
        manager.add(_editField);
        
        // Create a HorizontalFieldManager to house the Ok and Cancel buttons.
        HorizontalFieldManager buttonManager = new HorizontalFieldManager();
        _okButton = new ButtonField("OK");
        _cancelButton = new ButtonField("Cancel");
        buttonManager.add(_okButton);
        
        _maxValue = maxValue;
        _integerFilter = integerFilter;
        
        if (maxValue > 0)
        {
            _maxButton = new ButtonField("Max");
            buttonManager.add(_maxButton);
        }
        
        buttonManager.add(_cancelButton);
        
        manager.add(buttonManager);
    }

    /**
     * Show the dialog to the user.
     */
    public void show()
    {
        // Create a modal screen so that the code has to block before continuing.
        _app.pushModalScreen(this);
    }

    protected boolean keyChar(char c, int status, int time)
    {
        boolean retVal = false;
        // Check to see what field has focus.
        Field field = this.getLeafFieldWithFocus();
        
        switch(c) 
        {
            case Characters.ESCAPE:
                cancel();
                retVal = true;
                break;
            case Characters.ENTER:
                // Check to see what field has focus.
                if( field.equals(_okButton)) 
                {
                    accept();
                    retVal = true;
                } 
                else if (field.equals(_cancelButton)) 
                {
                    cancel();
                    retVal = true;
                }
                else if (field.equals(_maxButton))
                {
                    _editField.setText(Integer.toString(_maxValue));
                    accept();
                    retVal = true;
                }
                else if (field.equals(_editField))
                {
                    accept();
                    retVal = true;
                }
            break;
            default:
            break;
          }
        
        if (retVal)
        {
            return retVal;
        }
        else
        {
            return super.keyChar( c, status, time );
        }
    }
                

    protected boolean trackwheelClick(int status, int time)
    {
        boolean retVal = false;
        // Check to see what field has focus.
        Field field = this.getLeafFieldWithFocus();
        if( field.equals(_okButton)) 
        {
            accept();
            retVal = true;
        } 
        else if (field.equals(_cancelButton)) 
        {
            cancel();
            retVal = true;
        }
        else if (field.equals(_maxButton))
        {
            _editField.setText(Integer.toString(_maxValue));
            accept();
            retVal = true;
        }
        else if (field.equals(_editField))
        {
            accept();
            retVal = true;
        }
        
        return retVal;
    }
    
    /**
     * The user has accepted the dialog by pressing "enter" on the EditField
     * or by selecting the Ok button.
     */
    private void accept()
    {
        _app.popScreen(this);
        _cancelled = false;
    }
    
    /**
     * The user has decided to cancel the dialog by pressing the "escape" button
     * or by selecting the Cancel button.
     */
    private void cancel()
    {
        _app.popScreen(this);
        _cancelled = true;
    }
    
       
    /**
     * Indicates whether the dialog was cancelled by the user.
     * @return a boolean indicating if the dialog was cancelled
     * by the user.  True if cancelled, false otherwise.
     */
        
    public boolean isCancelled() 
    {
        return _cancelled;
    }
    
    /**
     * Returns the value that was entered by the user.  
     * Note that this value might not be meaningful if the
     * dialog was cancelled.
     * @return a String containing the file name entered
     * by the user.
     */
    public String getData()
    {
        if (_integerFilter && _editField.getText().length() < 1)
        {
            return "0";
        }
        else
        {
            return _editField.getText();
        }
    }
}
