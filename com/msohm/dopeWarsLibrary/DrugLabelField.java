/**
 * DrugLabelField
 *
 * Author:  Mark Sohm
 *
 * A custom LabelField that also contains the index of the drug displayed.
 */

package com.msohm.dopeWarsLibrary;

import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Color;

public final class DrugLabelField extends LabelField
{
    private int dIndex;
    private boolean highlight = false;
    
    public DrugLabelField(String text)
    {
        super(text, LabelField.FOCUSABLE);
    }
    
    public void setDrugIndex(int drugIndex)
    {
        dIndex = drugIndex;
    }
    
    public int getDrugIndex()
    {
        return dIndex;
    }
    
    public void paint(Graphics graphics) 
    {    
        if (highlight)
        {
            graphics.setColor(Color.CHOCOLATE);
        }
        
        super.paint(graphics);
    }
    
    public void setHighlight(boolean showHighlight)
    {
        highlight = showHighlight;
    }
}
