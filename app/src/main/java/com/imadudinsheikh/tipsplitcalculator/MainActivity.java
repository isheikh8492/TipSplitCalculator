package com.imadudinsheikh.tipsplitcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private EditText totalBillInput;
    private RadioGroup tipPercentageInput;
    private EditText noOfPeopleInput;

    private EditText finalBillOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        totalBillInput = findViewById(R.id.billTotalTaxInput);
        tipPercentageInput = findViewById(R.id.tipRadioGroup);
        noOfPeopleInput = findViewById(R.id.numberPeopleInput);
        finalBillOutput = findViewById(R.id.finalBillOutput);
    }

    public void calculate(View v) {
        int radioButtonId = tipPercentageInput.getCheckedRadioButtonId();
        Log.d(TAG, "value of radioGroup " + radioButtonId);
        float result = 0;
        if ((radioButtonId != -1) && (!Float.isNaN(Float.parseFloat(totalBillInput.getText().toString())))){
            RadioButton selectedTipAmount = (RadioButton) tipPercentageInput.findViewById(radioButtonId);
            float bill = Float.parseFloat(totalBillInput.getText().toString());
            bill += (0.01 * Float.parseFloat(selectedTipAmount.getText().subSequence(0, 2).toString())) * bill;
            result = (float) bill / Float.parseFloat(noOfPeopleInput.getText().toString());
            finalBillOutput.setText(Float.toString(result));
        }

    }

}