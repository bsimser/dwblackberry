/**
 * CurrencyConverter
 *
 * Author:  Mark Sohm
 *
 * Add's commas to create nice looking number strings.
 */

package com.msohm.dopeWarsLibrary;

public final class CurrencyConverter
{

    public CurrencyConverter()
    {}
    
    public static final String convertCurrency(int value)
    {
        StringBuffer convertedValue = new StringBuffer();
        String stringValue = value + "";
        int length = stringValue.length() - 1;
        int offset = 0;
        
        for (int count = length; count >= 0; --count)
        {
            convertedValue.insert(0, stringValue.charAt(count));
            
            if (offset == 2 && count != 0)
            {
                convertedValue.insert(0, ',');
                offset = -1;
            }
            
            ++offset;
        }
        
        return convertedValue.toString();
    }
}
