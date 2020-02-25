package com.example.assignment1temperatureconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private boolean flag = true;   //detect which radio button is

    private TextView labelForInput;
    private EditText textForInput;
    private TextView textForWrongInput;
    private TextView labelForOutput;
    private TextView textForOutput;
    private TextView textForHistory;
    private StringBuilder sb = new StringBuilder();


    private RadioButton buttonToDegreeC;
    private RadioButton buttonToDegreeF;

    private String myData;

    private SharedPreferences myPrefs;
    private SharedPreferences.Editor prefsEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        labelForInput = findViewById(R.id.labelForInput);
        textForInput = findViewById(R.id.textForInput);
        textForWrongInput = findViewById(R.id.textForWrongInput);
        labelForOutput = findViewById(R.id.labelForOutput);
        textForOutput = findViewById(R.id.textForOutput);
        textForHistory = findViewById(R.id.textForHistory);
        buttonToDegreeC = findViewById(R.id.buttonToDegreeC);
        buttonToDegreeF = findViewById(R.id.buttonToDegreeF);
        textForHistory.setMovementMethod(new ScrollingMovementMethod());


        myPrefs = getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE);
        prefsEditor = myPrefs.edit();

        myData = myPrefs.getString("DATA", "");
        textForHistory.setText(myData);


        flag = myPrefs.getBoolean("Button", true);

        if (flag) {
            buttonToDegreeC.performClick();

        } else {
            buttonToDegreeF.performClick();
        }
    }

    public void buttonToDegreeCClicked(View v) {
        flag = true;
        labelForInput.setText("Fahrenheit Degree:");
        labelForOutput.setText("Celsius Degree:");
        textForInput.setText("");
        textForOutput.setText("");

        prefsEditor.putBoolean("Button", flag);
        prefsEditor.apply();
    }

    public void buttonToDegreeFClicked(View v) {
        flag = false;
        labelForInput.setText("Celsius Degree:");
        labelForOutput.setText("Fahrenheit Degree:");
        textForInput.setText("");
        textForOutput.setText("");

        prefsEditor.putBoolean("Button", flag);
        prefsEditor.apply();
    }

    public void buttonForConvertClicked(View v) {
        //check input is right format or show wrong input !price1.matches("^[0-9][A-Za-z0-9 -]*$")
        String input = textForInput.getText().toString();
        if (!input.matches("^-?(0|[1-9]\\d*)(\\.\\d+)?$")) {
            textForWrongInput.setText("Wrong Input! Please enter number!");
            Toast.makeText(
                    this,
                    "Wrong Input! Please enter number!",
                    Toast.LENGTH_LONG
            ).show();
        } else {
            textForWrongInput.setText("");
            double temp = Double.parseDouble(input);
            double result;
            if (sb.length() == 0) {
                myData = myPrefs.getString("DATA", "");
                sb.append(myData);
            }
            // convert to C if flag is true else convert to false
            // save string  F ==> C or C ==> F into textForHistory
            if (flag) {
                result = (temp - 32.0) / 1.8;
                String result1D = String.format("%.1f", result);
                textForOutput.setText(result1D);
                sb.insert(0, (textForInput.getText().toString() + " F ==> " + result1D + " C" + "\n"));
                textForHistory.setText(sb.toString());
            } else {
                result = (temp * 1.8) + 32;
                String result1D = String.format("%.1f", result);
                textForOutput.setText(result1D);
                sb.insert(0, (textForInput.getText().toString() + " C ==> " + result1D + " F" + "\n"));
                textForHistory.setText(sb.toString());
            }
        }
        prefsEditor.putString("DATA", sb.toString());
        prefsEditor.apply();
    }

    public void buttonForClearClicked(View v) {
        sb = new StringBuilder();
        prefsEditor.clear();
        prefsEditor.apply();
        textForHistory.setText(sb.toString());
        textForInput.setText("");
        textForOutput.setText("");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        outState.putString("output", textForHistory.getText().toString());
        outState.putBoolean("flag", flag);
        outState.putString("labelForInput", labelForInput.getText().toString());
        outState.putString("labelForOutput", labelForOutput.getText().toString());
        outState.putString("textForOutput", textForOutput.getText().toString());

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        sb.append(savedInstanceState.getString("output"));
        textForHistory.setText(sb.toString());
        flag = savedInstanceState.getBoolean("flag");
        labelForInput.setText(savedInstanceState.getString("labelForInput"));
        labelForOutput.setText(savedInstanceState.getString("labelForOutput"));
        textForOutput.setText(savedInstanceState.getString("textForOutput"));
    }
}
