package cs161.mortgagecalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.*;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    TextView textViewLoanRate, textViewResult;
    EditText editTextAmountToBorrow;
    SeekBar seekBarLoanRate;
    RadioGroup radioGroupLoanTerm;
    CheckBox checkBoxTaxAndInsurance;
    Button buttonCalculate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();
        initializeControllers();
    }

    /**
     * Initializes all views % sets seekbar progress to accompanying textview
     */
    public void initializeViews(){
        editTextAmountToBorrow = (EditText) findViewById(R.id.edittext_amount_borrowed);
        textViewLoanRate = (TextView) findViewById(R.id.textview_loan_rate);
        textViewResult = (TextView) findViewById(R.id.textview_result);
        seekBarLoanRate = (SeekBar) findViewById(R.id.seekbar_loan_rate);
        radioGroupLoanTerm = (RadioGroup) findViewById(R.id.radiogroup_loan_term);
        checkBoxTaxAndInsurance = (CheckBox) findViewById(R.id.checkbox_tax_insurance);
        buttonCalculate = ( Button) findViewById(R.id.button_calculate);
        textViewLoanRate.setText(getResources().getString(R.string.interest_rate) + seekBarLoanRate.getProgress()+"");

    }

    /**
     * Initializes controllers, only two currently needed.
     */
    public void initializeControllers(){
        seekBarController(seekBarLoanRate);
        setButtonCalculateController(buttonCalculate);
    }

    /**
     * Calculate Button Controller:
     * Checks if all form inputs are valid or not, formats data as needed and passes data to calculateMortgage method
     * @param calculate: Calculate button is passed in
     */
    public void setButtonCalculateController(final Button calculate){
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean error = false;
                String errorMessage = "";
                int currentLoanTermValue = 0;
                double doubleAmountToBorrow = 0.0;
                int intSelectedInterestRate = seekBarLoanRate.getProgress();
                boolean booleanTaxAndInsuranceSelected = checkBoxTaxAndInsurance.isChecked();
                int  intSelectedRadioButtonValue = radioGroupLoanTerm.indexOfChild(findViewById(radioGroupLoanTerm.getCheckedRadioButtonId()));

                //IF/ELSE Block checks to see if amount to borrow edit text is filled in or not
                if(!editTextAmountToBorrow.getText().toString().isEmpty())
                {
                    doubleAmountToBorrow =  Double.parseDouble(editTextAmountToBorrow.getText().toString());
                }
                else{
                    error =true;
                    errorMessage += "Error: Invalid Loan Amount \n\r";
                }

                //IF/ELSE Block gets value of selected radio group button and makes it into the associated value
                if(intSelectedRadioButtonValue == 0)
                {
                    currentLoanTermValue = 15;
                }
                else if(intSelectedRadioButtonValue == 1)
                {
                    currentLoanTermValue = 20;
                }
                else if(intSelectedRadioButtonValue == 2)
                {
                    currentLoanTermValue = 30;
                }
                else{
                    error =true;
                    errorMessage += "Error: Invalid Loan Term \n\r" +"|"+intSelectedInterestRate+"|";
                }


                if(!error) {
                   textViewResult.setText("$"+(calculateMortgage(doubleAmountToBorrow, intSelectedInterestRate, currentLoanTermValue, booleanTaxAndInsuranceSelected)+"\n\r Monthly"));
                }
                else{
                    Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Get Seekbar value and set the accompanying textview with the value
     * @param currentSeekBarLoanRate: Seekbar is passed in
     */
    public void seekBarController(SeekBar currentSeekBarLoanRate){
       currentSeekBarLoanRate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewLoanRate.setText(getResources().getString(R.string.interest_rate) + progress + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }

    /**
     * Calculates Mortgage
     * @param amountToBorrow: How much money is going to be borrowed
     * @param interestRate: How much interest rate is be applied, from 0 -20
     * @param loanTerm: How many years will the loan take
     * @param applyTaxes: Apply taxes to loan or not
     * @return Mortgage cost
     */
    public double calculateMortgage(double amountToBorrow, int interestRate, int loanTerm, boolean applyTaxes){
        DecimalFormat df2 = new DecimalFormat("###.##");
       ;
        double taxedAmount = 0.00;
        double rate =  (interestRate+0.00)/1200;

        if(applyTaxes) {
            taxedAmount = amountToBorrow * .01;
        }

        if(rate==0.0)
        {
             return Double.valueOf(df2.format((amountToBorrow/(loanTerm*12)+taxedAmount)));
        }
        else
        {
            double x= ((amountToBorrow * rate) / (1 - Math.pow(1 + rate, -loanTerm * 12)) + taxedAmount);
            return Double.valueOf(df2.format(x));
        }
    }
}


