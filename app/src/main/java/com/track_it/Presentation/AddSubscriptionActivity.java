package com.track_it.Presentation;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.RadioButton;

import com.track_it.R;
import com.track_it.domainObject.SubscriptionObj;
import com.track_it.logic.*;


// This class handles the presentation of the subscription page for the android app.


public class AddSubscriptionActivity extends AppCompatActivity {

    private final  int  MAX_PAYMENT_AMOUNT = SubscriptionObj.MAX_PAYMENT; // The maximum number of digits (before the decimal point) that can be entered by user for payment amount
    private final  int  MAX_PAYMENT_DECIMALS = 2; // The maximum number digits after the decimal for payment amount

    private AddSubscriptionHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_subscription);

        handler = new AddSubscriptionHandler();




        // Add subscription button target
        Button button = (Button) findViewById(R.id.submit_sub_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                clickedAddSubscriptionutton(v); // Run this function when the user clicks the add subscripton button.

                //setContentView(R.layout.activity_main);
                //finish();

            }
        });

        // This limits the constrains what the user can enter for payment amount
        EditText etText = findViewById(R.id.input_payment_amount);
        // Pass setFiltlers and array of objects that implement the InputFilter interface
        etText.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(MAX_PAYMENT_DECIMALS,MAX_PAYMENT_AMOUNT)});
    };


   private void clickedAddSubscriptionutton( View v)
    {

        boolean successTry = true;

        // Get the string the user entered for a name
        EditText textInput;
        textInput = (EditText)findViewById(R.id.input_subscription_name);
        String userNameInput = textInput.getText().toString().trim(); // get string, and remove white spaces


        // Get the payment amount the user entered, and convert it to Cents.
        textInput = (EditText)findViewById(R.id.input_payment_amount);

        String[] paymentAmountString =  textInput.getText().toString().split("\\.");

        int paymentInCents = 0;
        // I thought parseInt threw an exception on invalid data??
        // Any ways, get Paymeent amount in cents
        if (paymentAmountString.length > 0)
        {
            System.out.println("right here");
            if (isParsable(paymentAmountString[0])) {
                paymentInCents = Integer.parseInt(paymentAmountString[0]) * 100;

                if ( paymentAmountString.length > 1 && isParsable(paymentAmountString[1] ) )
                {
                    paymentInCents = paymentInCents +  Integer.parseInt(paymentAmountString[1]);
                }
                else {
                    paymentInCents = 0; // Reset back to zero, something was strange with the input
                }
            }
        }



        //
        // Get payment Frequency
        //
        //
        String PaymentFrequency = ""; // This will stay blank if radio button selected
        RadioButton radioWeek,RadioMonth,radioYear;
        radioWeek = (RadioButton)findViewById(R.id.input_frequency_weekly);
        RadioMonth= (RadioButton)findViewById(R.id.input_frequency_monthly);
        radioYear= (RadioButton)findViewById(R.id.input_frequency_yearly);

        if ( radioWeek.isChecked())
        {
            PaymentFrequency = SubscriptionObj.WEEKLY;
        }
        else if ( RadioMonth.isChecked())
        {
            PaymentFrequency = SubscriptionObj.MONTHLY;
        }

        else  if ( radioYear.isChecked())
        {
            PaymentFrequency = SubscriptionObj.YEARLY;
        }


        // Create a new Subscription object (it's fine if the data is not valid, because we are going to pass it
        // to the logic layer to check it)
        SubscriptionObj newSubscription = new  SubscriptionObj(userNameInput, paymentInCents, PaymentFrequency);


        handler.addSubscription(newSubscription); // The handler could somehow send back messages if anything went wrong, so our UI can tell the user what needs to be changed.




    }



    private static boolean isParsable(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (final NumberFormatException e) {
            return false;
        }
    }

}





//Limits the user input for a decimal input to a format such that there will be a max of digitBeforeDecimal before decimal and a max of decimalDigits
class DecimalDigitsInputFilter implements InputFilter {

    private final int decimalDigits; // Maximum number of integer after decimal
    private final int digitBeforeDecimal; // Maximum number of integers before deciamls

    public DecimalDigitsInputFilter(int decimalDigits, int digitBeforeDecimal) {
        this.decimalDigits = decimalDigits;
        this.digitBeforeDecimal = digitBeforeDecimal;
    }



    // Makes it such that the string will be in the format such that there will at most be digitBeforeDecimal before the decimal, and decimalDigits after the decimal
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

        return returnValue; // Will be null if source accepted, and "" if source accepted!
    }

}
