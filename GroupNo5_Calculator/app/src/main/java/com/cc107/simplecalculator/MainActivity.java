package com.cc107.simplecalculator;
import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText e1, e2, eT;
    Button add, sub, mul, div;
    TextView eL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        e1 = (EditText) findViewById(R.id.eNum1);
        e2 = (EditText) findViewById(R.id.eNum2);
        eT = (EditText) findViewById(R.id.eTotal);
        eL = (TextView) findViewById(R.id.label3);
        add = (Button) findViewById(R.id.btnAdd);
        sub = (Button) findViewById(R.id.btnSub);
        mul = (Button) findViewById(R.id.btnMul);
        div = (Button) findViewById(R.id.btnDiv);

        add.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    double num1 = Double.parseDouble(e1.getText().toString());
                    double num2 = Double.parseDouble(e2.getText().toString());
                    double sum = num1 + num2;
                    eL.setText("Sum");
                    eT.setText(Double.toString(sum));
                }
                catch (IllegalArgumentException e) {
                    Toast.makeText(MainActivity.this,"Please enter a valid number.",Toast.LENGTH_LONG).show();
                    eT.setText("");

                }
            }
        });

        sub.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    double num1 = Double.parseDouble(e1.getText().toString());
                    double num2 = Double.parseDouble(e2.getText().toString());
                    double dif = num1 - num2;
                    eL.setText("Difference");
                    eT.setText(Double.toString(dif));
                } catch (IllegalArgumentException e) {
                    Toast.makeText(MainActivity.this, "Please enter a valid number.", Toast.LENGTH_LONG).show();
                    eT.setText("");

                }
            }
        });

        mul.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    double num1 = Double.parseDouble(e1.getText().toString());
                    double num2 = Double.parseDouble(e2.getText().toString());
                    double prod = num1 * num2;
                    eL.setText("Product");
                    eT.setText(Double.toString(prod));
                } catch (IllegalArgumentException e) {
                    Toast.makeText(MainActivity.this, "Please enter a valid number.", Toast.LENGTH_LONG).show();
                    eT.setText("");

                }
            }
        });

        div.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                double num1 = Double.parseDouble(e1.getText().toString());
                double num2 = Double.parseDouble(e2.getText().toString());
                eL.setText("Quotient");
                if (num2 == 0) {
                    e2.setError("Divisor cannot be 0");
                    e2.setText("");
                    eT.setText("");
                } else {
                    double quo = num1 / num2;
                    eT.setText(Double.toString(quo));
                }
                } catch (IllegalArgumentException e) {
                    Toast.makeText(MainActivity.this,"Please enter a valid number.",Toast.LENGTH_LONG).show();
                    eT.setText("");

                }
            }
        });
    }

    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setMessage("Close this Application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();

    }
}


