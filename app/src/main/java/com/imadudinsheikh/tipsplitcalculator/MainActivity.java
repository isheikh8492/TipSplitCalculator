package com.imadudinsheikh.tipsplitcalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private EditText totalBillInput;
    private RadioGroup tipPercentageInput;
    private EditText noOfPeopleInput;

    private TextView tipAmountOutput;
    private TextView totalWithTipOutput;
    private TextView finalBillOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        totalBillInput = findViewById(R.id.billTotalTaxInput);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Do something before text changes
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String input = charSequence.toString();
                if (input.contains(".")) {
                    int decimalIndex = input.indexOf(".");
                    int length = input.length();
                    if (length - decimalIndex > 3) {
                        input = input.substring(0, decimalIndex + 3);
                        totalBillInput.setText(input);
                        totalBillInput.setSelection(input.length());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Do something after text changes
            }
        };
        totalBillInput.addTextChangedListener(textWatcher);

        tipPercentageInput = findViewById(R.id.tipRadioGroup);

        tipAmountOutput = findViewById(R.id.tipAmountTextView);
        totalWithTipOutput = findViewById(R.id.totalTipTextView);
        noOfPeopleInput = findViewById(R.id.numberPeopleInput);

        noOfPeopleInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                // Remove any non-integer characters from the string
                String updatedString = s.toString().replaceAll("[^\\d]", "");
                // If the updated string is different from the original string
                if (!s.toString().equals(updatedString)) {
                    // Set the updated string as the new text in the EditText
                    noOfPeopleInput.setText(updatedString);
                    // Move the cursor to the end of the updated string
                    noOfPeopleInput.setSelection(updatedString.length());
                }
            }
        });

        finalBillOutput = findViewById(R.id.TotalPerPersonTextView);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("TIP_AMOUNT", tipAmountOutput.getText().toString());
        outState.putString("TOTAL_WITH_TIP", totalWithTipOutput.getText().toString());
        outState.putString("FINAL_BILL", finalBillOutput.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        tipAmountOutput.setText(savedInstanceState.getString("TIP_AMOUNT"));
        totalWithTipOutput.setText(savedInstanceState.getString("TOTAL_WITH_TIP"));
        finalBillOutput.setText(savedInstanceState.getString("TOTAL_WITH_TIP"));

    }

    public void calculateTipAndTotalWithTip(View v) {
        int radioButtonId = tipPercentageInput.getCheckedRadioButtonId();
        String totalBillTextInputString = totalBillInput.getText().toString();
        if (totalBillTextInputString.isEmpty()) {
            Toast.makeText(this, "Please enter Total Bill with Tax before tipping", Toast.LENGTH_SHORT).show();
            tipPercentageInput.check(-1);
        } else {
            if (totalBillTextInputString.contains("$")) {
                totalBillTextInputString = totalBillInput.getText().toString().substring(1);
            }
            BigDecimal totalBillWithTax;
            BigDecimal tipPercentage;
            BigDecimal tipAmount;

            if ((radioButtonId != -1) && (!Float.isNaN(Float.parseFloat(totalBillTextInputString)))){
                RadioButton selectedTipAmount = tipPercentageInput.findViewById(radioButtonId);
                totalBillWithTax = new BigDecimal(totalBillTextInputString);
                tipPercentage = new BigDecimal(selectedTipAmount.getText().subSequence(0, 2).toString()).multiply(BigDecimal.valueOf(0.01));
                tipAmount = totalBillWithTax.multiply(tipPercentage).setScale(2, RoundingMode.HALF_UP);
                BigDecimal totalWithTipAmount = totalBillWithTax.add(tipAmount).setScale(2, RoundingMode.HALF_UP);
                tipAmountOutput.setText(String.format("$%.2f", tipAmount));
                totalWithTipOutput.setText(String.format("$%.2f", totalWithTipAmount));
                totalBillInput.setText(String.format("$%.2f", totalBillWithTax));
            }
        }

    }

    public void calculateTotalPerPerson(View v) {
        int radioButtonId = tipPercentageInput.getCheckedRadioButtonId();
        String totalBillTextInputString = totalBillInput.getText().toString();
        if (totalBillTextInputString.isEmpty()) {
            Toast.makeText(this, "Please enter Total Bill with Tax before tipping", Toast.LENGTH_SHORT).show();
            tipPercentageInput.check(-1);
        } else {
            if (totalBillTextInputString.contains("$")) {
                totalBillTextInputString = totalBillInput.getText().toString().substring(1);
            }
            BigDecimal totalBillWithTax = null;
            BigDecimal tipPercentage;
            BigDecimal tipAmount;
            BigDecimal totalWithTipAmount = null;
            if ((radioButtonId != -1) && (!Float.isNaN(Float.parseFloat(totalBillInput.getText().toString().substring(1))))){
                RadioButton selectedTipAmount = tipPercentageInput.findViewById(radioButtonId);
                totalBillWithTax = new BigDecimal(totalBillTextInputString);
                tipPercentage = new BigDecimal(selectedTipAmount.getText().subSequence(0, 2).toString()).multiply(BigDecimal.valueOf(0.01));
                tipAmount = totalBillWithTax.multiply(tipPercentage).setScale(2, RoundingMode.HALF_UP);
                tipAmountOutput.setText(String.format("$%.2f", tipAmount));
                totalWithTipAmount = totalBillWithTax.add(tipAmount).setScale(2, RoundingMode.HALF_UP);
                totalWithTipOutput.setText(String.format("$%.2f", totalWithTipAmount));
            }
            if ((totalWithTipAmount == null) || (totalBillWithTax == null)) {
                Toast.makeText(this, "Please enter Total Bill with Tax before pressing this button", Toast.LENGTH_SHORT).show();
            } else if (noOfPeopleInput.getText().toString().isEmpty()) {
                Toast.makeText(this, "Number of People input field is empty", Toast.LENGTH_SHORT).show();
            } else {
                BigDecimal noOfPeople = new BigDecimal(noOfPeopleInput.getText().toString());
                BigDecimal totalDividedByFour = totalWithTipAmount.divide(noOfPeople, 2, RoundingMode.HALF_UP);
                finalBillOutput.setText(String.format("$%.2f", totalDividedByFour));
            }
        }
    }
}