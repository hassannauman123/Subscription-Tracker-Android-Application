package com.track_it.Presentation;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.text.InputFilter;
import android.text.Spanned;
import com.track_it.R;



// This class handles the presentation of the subscription page for the android app.


public class AddSubscription extends AppCompatActivity {

    private final  int  MAX_PAYMENT_AMOUNT = 5; // The maximum number of digits (before the decimal point) that can be entered by user for payment amount
    private final  int  MAX_PAYMENT_DECIMALS = 2; // The maximum number digits after the decimal for payment amount


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_subscription);

        Button button = (Button) findViewById(R.id.submit_sub_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                setContentView(R.layout.activity_main);
                finish();

            }
        });


        // This limits the constrains what the user can enter for payment amount
        EditText etText = findViewById(R.id.payment_amount);
        // Pass setFiltlers and array of objects that implement the InputFilter interface
        etText.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(MAX_PAYMENT_DECIMALS,MAX_PAYMENT_AMOUNT)});
    };

}


//Limits the user input for a decimal input to a format such that there will be a max of digitBeforeDecimal before decimal and a max of decimalDigits
class DecimalDigitsInputFilter implements InputFilter {

    private final int decimalDigits; // Maximum number of integer after decimal
    private final int digitBeforeDecimal; // Maximum number of integers before deciamls

    public DecimalDigitsInputFilter(int decimalDigits, int digitBeforeDecimal) {
        this.decimalDigits = decimalDigits;
        this.digitBeforeDecimal = digitBeforeDecimal;
    }



    // Makes it such that the string will be in the format such that will at most be digitBeforeDecimal before the decimal, and decimalDigits after the decimal
    @Override
    public CharSequence filter(CharSequence source, int start, int end,  Spanned dest, int dstart, int dend) {

        //Reminder
        // source is the char being added.
        // destination is the previous string. Source will be added to destination at position dstart if we return null, else it will be reject if we return ""


        CharSequence returnValue = null; // Null means we accept source, "" means we reject it

        int dotPos = -1; // Where does the decimal occur in destination?
        int len = dest.length(); // Get length of previous string


        // Iterate over the string, looking for decimal character
        for (int i = 0; i < len; i++)
        {
            char charIterator = dest.charAt(i); // The current char being read

                if (charIterator == '.' ) {
                    dotPos = i; // Decimal place found
                    break;
                }
                else if ( (i >= digitBeforeDecimal -1 ) &&  !source.equals(".") )  // The maximum amount of integers before the decimal

                {
                    if ( (i + 1) < len && dest.charAt(i+1) == '.') // The next char after this is a decimal, so we are good
                    {
                        continue; // Continue back to loop ( dotPos will end being = to i+1);
                    }
                    else
                    {
                        returnValue = ""; // Else we hit the maximum number of integers before decimal, and the next char is not a decimal so don't add anything
                        break; // Break out of this for loop, and dotPos will <
                    }
                }
        }

        if (dotPos >= 0)  // Decimal is in the string
        {

            // If source is being added to front of decimal and there is room accept source
            if (dend <= dotPos && (dotPos < digitBeforeDecimal))
            {
                 returnValue = null;
            }
            // Else - If there are already 2 digits past the decimal and source is going past decimal reject source
            else if (len - dotPos > decimalDigits)
            {
                returnValue = "";
            }
        }

        return returnValue; // Will be null if source accepted, "" if source accepted!
    }

}
