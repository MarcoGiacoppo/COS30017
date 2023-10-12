package com.example.core1marco

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    private var totalNo = 0
    private var state = ""
    private var random = 0
    private var randomGenerator = Random(1) //use seed value of 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        // Initialize views
        val rollButton = findViewById<Button>(R.id.roll)
        val addButton = findViewById<Button>(R.id.add)
        val subtractButton = findViewById<Button>(R.id.subtract)
        val resetButton = findViewById<Button>(R.id.reset)
        val initial = findViewById<TextView>(R.id.initial)

        val total= findViewById<TextView>(R.id.total)
        total.text = totalNo.toString()

        val iconImageView = findViewById<ImageView>(R.id.dice)
        iconImageView.setImageResource(getDiceImageResource(random))

        executeCommonLogic(
            findViewById<Button>(R.id.roll),
            findViewById<Button>(R.id.add),
            findViewById<Button>(R.id.subtract),
        )

        // Set click listeners for buttons
        rollButton.setOnClickListener {
            state = "roll"
            random = randomGenerator.nextInt(1,7)
            executeCommonLogic(rollButton, addButton, subtractButton)
            iconImageView.setImageResource(getDiceImageResource(random))
            //Toggle visibility
            initial.visibility = View.INVISIBLE
            iconImageView.visibility = View.VISIBLE
        }

        addButton.setOnClickListener {
            state = "add"
            totalNo += random
            total.text = "$totalNo"
            executeCommonLogic(rollButton, addButton, subtractButton)

        }

        subtractButton.setOnClickListener {
            state = "subtract"
            totalNo = maxOf(totalNo - random, 0)
            total.text = "$totalNo"
            executeCommonLogic(rollButton, addButton, subtractButton)
            updateTextColor()
        }

        resetButton.setOnClickListener {
            totalNo = 0
            total.text = "$totalNo"
            updateTextColor()
            state = ""
            executeCommonLogic(rollButton, addButton, subtractButton)
        }

        if (savedInstanceState != null){
            totalNo = savedInstanceState.getInt("totalNo")
            state = savedInstanceState.getString("state", "")
            random = savedInstanceState.getInt("random")
        }
    }

    private fun executeCommonLogic(rollButton: Button, addButton: Button, subtractButton: Button) {
        updateViewForState(rollButton, addButton, subtractButton)
        updateTextColor()
    }

    private fun updateTextColor() {
        val totalTextView = findViewById<TextView>(R.id.total)
        if (totalNo < 20) {
            totalTextView.setTextColor(Color.BLACK)
        } else if (totalNo > 20) {
            totalTextView.setTextColor(Color.RED)
        } else if (totalNo == 20) {
            totalTextView.setTextColor(Color.GREEN)
            state = "win"
        }
    }

    private fun updateViewForState(rollButton: Button, addButton: Button, subtractButton: Button) {
        val disable = totalNo == 20
        rollButton.isEnabled = !disable && state != "roll"
        addButton.isEnabled = !disable && state == "roll"
        subtractButton.isEnabled = !disable && state == "roll"
    }

    private fun getDiceImageResource(diceValue: Int): Int {
        return when (diceValue) {
            1 -> R.drawable.one
            2 -> R.drawable.two
            3 -> R.drawable.three
            4 -> R.drawable.four
            5 -> R.drawable.five
            6 -> R.drawable.six
            else -> R.drawable.one
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //Save the necessary data to the bundle
        outState.putInt("totalNo", totalNo)
        outState.putString("state", state)
        outState.putInt("random", random)

        //Log to debug
        Log.i("MainActivity", "Saved Instance $totalNo")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        //Restore the necessary data from the bundle
        totalNo = savedInstanceState.getInt("totalNo")
        state = savedInstanceState.getString("state", "")
        random = savedInstanceState.getInt("random")

        // Update the UI to reflect the restored state
        val totalTextView = findViewById<TextView>(R.id.total)
        totalTextView.text = totalNo.toString()

        val iconImageView = findViewById<ImageView>(R.id.dice)
        iconImageView.setImageResource(getDiceImageResource(random))

        executeCommonLogic(
            findViewById<Button>(R.id.roll),
            findViewById<Button>(R.id.add),
            findViewById<Button>(R.id.subtract)
        )

        updateTextColor()

        //Log for debug
        Log.i("MainActivity", "Restored instance is $totalNo")
    }
}
