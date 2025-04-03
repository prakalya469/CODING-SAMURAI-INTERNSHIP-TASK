package com.example.calculatorapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.calculatorapp.R;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    // Declare variables to hold references to UI elements
    private EditText num1EditText, num2EditText;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements from the layout
        num1EditText = findViewById(R.id.num1EditText);
        num2EditText = findViewById(R.id.num2EditText);
        resultTextView = findViewById(R.id.resultTextView);

        // Set click listeners for arithmetic operation buttons

        Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performCalculation('+');
            }
        });

        Button subtractButton = findViewById(R.id.subtractButton);
        subtractButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performCalculation('-');
            }
        });

        Button multiplyButton = findViewById(R.id.multiplyButton);
        multiplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performCalculation('*');
            }
        });

        Button divideButton = findViewById(R.id.divideButton);
        divideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performCalculation('/');
            }
        });

        Button sqrtButton = findViewById(R.id.sqrtButton);
        sqrtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateSquareRoot();
            }
        });
    }

    private void performCalculation(char operator) {
        // Get the values entered in the input fields
        String num1Str = num1EditText.getText().toString();
        String num2Str = num2EditText.getText().toString();

        // Check if either input field is empty
        if (num1Str.isEmpty() || num2Str.isEmpty()) {
            Toast.makeText(this, "Please enter both numbers", Toast.LENGTH_SHORT).show();
            return; // Exit the method to prevent calculations with empty inputs
        }

        // Convert the input values to numeric format
        double num1 = Double.parseDouble(num1Str);
        double num2 = Double.parseDouble(num2Str);
        double result = 0;

        // Perform the selected calculation based on the operator
        switch (operator) {
            case '+':
                result = num1 + num2;
                break;
            case '-':
                result = num1 - num2;
                break;
            case '*':
                result = num1 * num2;
                break;
            case '/':
                if (num2 != 0) {
                    result = num1 / num2;
                } else {
                    Toast.makeText(this, "Cannot divide by zero", Toast.LENGTH_SHORT).show();
                    return; // Exit the method if division by zero is attempted
                }
                break;
        }

        // Format and display the calculation result
        DecimalFormat df = new DecimalFormat("#.##");
        resultTextView.setText("Result: " + df.format(result));
    }

    private void calculateSquareRoot() {
        String num1Str = num1EditText.getText().toString();
        // Check if the input field is empty
        if (num1Str.isEmpty()) {
            Toast.makeText(this, "Please enter a number", Toast.LENGTH_SHORT).show();
            return; // Exit the method to prevent calculations with empty inputs
        }

        double num = Double.parseDouble(num1Str);
        double sqrtResult = Math.sqrt(num);
        DecimalFormat df = new DecimalFormat("#.##");
        resultTextView.setText("Square Root: " + df.format(sqrtResult));
    }
}