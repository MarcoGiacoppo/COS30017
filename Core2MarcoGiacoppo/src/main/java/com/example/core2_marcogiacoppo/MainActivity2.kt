package com.example.core2_marcogiacoppo


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Button
import android.widget.SeekBar
import android.widget.Toast
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ImageView
import java.time.LocalDate


class MainActivity2 : AppCompatActivity() {
    private var initialPrice: Double = 0.0
    private var tabs: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val image = findViewById<ImageView>(R.id.image)
        val priceText = findViewById<TextView>(R.id.total)
        val saveButton = findViewById<Button>(R.id.save)
        val seekBar = findViewById<SeekBar>(R.id.bar)
        val tooltipTextView = findViewById<TextView>(R.id.valueLabel)

        tabs = intent.getIntExtra("tabs", -1)

        val price = intent.getStringExtra("price")
        val imageView = intent.getIntExtra("image", 0)

        initialPrice = price?.substring(1)?.toDoubleOrNull() ?: 0.0 // Remove "$" and convert to Double
        priceText.text = price

        image.setImageResource(imageView)

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                val tooltipText = progress.toString()
                tooltipTextView.text = tooltipText // Update the tooltip text
                updateTooltipPosition(seekBar, tooltipTextView, progress)
                tooltipTextView.visibility = View.VISIBLE

                // Calculate the new price
                val newPrice = initialPrice * progress
                priceText.text = "$$newPrice"

                val currentDate = LocalDate.now()

                saveButton.setOnClickListener {

                    if (seekBar != null) {
                        Log.d("SeekBarProgress", "SeekBar Progress: ${seekBar.progress}")
                    }
                    Log.d("SelectedDays", "Selected Days: $progress")


                    if (progress == 0){
                        Toast.makeText(applicationContext, "Minimum 1 day bruv!", Toast.LENGTH_SHORT).show()
                    } else {
                        val returnDate = currentDate.plusDays(progress.toLong())

                        val resultIntent = Intent()
                        resultIntent.putExtra("tabIndex", tabs)
                        resultIntent.putExtra("isButtonDisabled", true)
                        resultIntent.putExtra("returnDate", returnDate.toString())
                        setResult(RESULT_OK, resultIntent)
                        finish()
                        Toast.makeText(applicationContext,"Woohooo!! Happy Riding :)", Toast.LENGTH_SHORT).show()

                    }
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Not needed
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                tooltipTextView.visibility = View.INVISIBLE
            }

            private fun updateTooltipPosition(seekBar: SeekBar?, tooltipTextView: TextView, progress: Int) {
                val thumb = seekBar?.thumb
                val thumbRect = thumb?.bounds
                val thumbX = thumbRect?.centerX() ?: 0

                // Calculate the x-coordinate for the tooltip to be centered on the thumb
                val tooltipX = thumbX - tooltipTextView.width + 110

                // Set the tooltip's position
                tooltipTextView.x = tooltipX.toFloat()
            }
        })
    }

    override fun onBackPressed() {
        Toast.makeText(this, "Hope you find your perfect bike!", Toast.LENGTH_SHORT).show()

        super.onBackPressed()
    }
}
