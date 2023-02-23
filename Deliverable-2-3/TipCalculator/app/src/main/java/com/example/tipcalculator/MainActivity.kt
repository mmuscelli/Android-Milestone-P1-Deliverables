package com.example.tipcalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val priceEditText: EditText = findViewById(R.id.priceEditText)
        val tip10Button: Button = findViewById(R.id.tip10Buttom)
        val tip18Button: Button = findViewById(R.id.tip18Buttom)
        val tip25Button: Button = findViewById(R.id.tip25Buttom)
        val tipPercentageEditText: EditText = findViewById(R.id.tipPercentageEditText)
        val tipResultTextView: TextView = findViewById(R.id.tipResultTextView)
        val calculateButton: Button = findViewById(R.id.calculateButton)

        tip10Button.setOnClickListener {
            tipPercentageEditText.setText("10")
        }

        tip18Button.setOnClickListener {
            tipPercentageEditText.setText("18")
        }

        tip25Button.setOnClickListener {
            tipPercentageEditText.setText("25")
        }

        calculateButton.setOnClickListener {
            try {
                val price = priceEditText.text.toString().toFloat()
                val tipPercentage = tipPercentageEditText.text.toString().toFloat() / 100f
                val tip = tipPercentage * price
                tipResultTextView.text = String.format("\$%.2f", tip)
                Toast.makeText(applicationContext, "Calculation success!", Toast.LENGTH_LONG).show()
            } catch (e: NumberFormatException) {
                Toast.makeText(applicationContext, "Calculation error!\nPlease enter a price and a tip.", Toast.LENGTH_LONG).show()
            }
        }

    }
}