package com.example.core2_marcogiacoppo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup


class MainActivity : AppCompatActivity() {

    // Declare the UI elements
    private lateinit var imageView: ImageView
    private lateinit var title: TextView
    private lateinit var description: TextView
    private lateinit var ratingBar: RatingBar
    private lateinit var chipGroup: ChipGroup
    private lateinit var price: TextView

    data class Item(
        val imageResId: Int,
        val title: String,
        val description: String,
        val rating: Float,
        val chips: List<String>,
        val price: String,
        var returnDate: String)

    // Create a list of items
    private val itemList = listOf(
        Item(R.drawable.bmw, "BMW S1000rr", "Very fast bike, not for daily use", 5f, listOf("125cc+", "600cc+", "1000cc+"), "$249",""),
        Item(R.drawable.cb125r, "Honda CB125r", "Perfect for daily use", 3.5f, listOf("125cc+", "600cc+", "1000cc+"), "$149",""),
        Item(R.drawable.r6, "Yamaha R6", "Great bike for long touring", 4f, listOf("125cc+", "600cc+", "1000cc+"), "$219","")
    )

    private val disableBorrow = MutableList(itemList.size){false}

    private var currentItemIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI elements
        imageView = findViewById(R.id.imageView)
        title = findViewById(R.id.title)
        description = findViewById(R.id.desc)
        ratingBar = findViewById(R.id.ratingBar)
        chipGroup = findViewById(R.id.chipGroup)
        price = findViewById(R.id.price)

        // Set initial content
        updateUIWithItem(currentItemIndex)

        // Set "Next" button click listener
        val nextButton = findViewById<Button>(R.id.next)
        nextButton.setOnClickListener {
            // Increment the counter and wrap to the first item
            currentItemIndex = (currentItemIndex + 1) % itemList.size
            // Update the UI with the next item
            updateUIWithItem(currentItemIndex)
        }

        val rentButton = findViewById<Button>(R.id.rent)
        rentButton.setOnClickListener{
            if (!disableBorrow[currentItemIndex]){
                val data = itemList[currentItemIndex]

                val i = Intent(this, MainActivity2::class.java)

                i.putExtra("price", data.price)
                i.putExtra("image", data.imageResId)
                i.putExtra("tabs", currentItemIndex)
                i.putExtra("returnDate", data.returnDate)

                startActivityForResult(i, 1)
            }

        }
    }

    private fun updateUIWithItem(index: Int) {

        val data = itemList[currentItemIndex]

        imageView.setImageResource(data.imageResId)
        title.text = data.title
        description.text = data.description
        ratingBar.rating = data.rating

        // Define the mapping between prices and chips to highlight
        val priceToChipsMap = mapOf(
            "$249" to listOf("1000cc+"),
            "$219" to listOf("600cc+"),
            "$149" to listOf("125cc+")
        )

        // Determine which chips to highlight based on the price
        val chipsToHighlight = priceToChipsMap[data.price] ?: emptyList()

        // Clear existing chips and add the new chips
        chipGroup.removeAllViews()
        for (chipText in data.chips) {
            val chip = Chip(this)
            chip.text = chipText
            chip.setChipBackgroundColorResource(if (chipsToHighlight.contains(chipText)) R.color.green else R.color.white)
            chip.isCheckable = chipsToHighlight.contains(chipText)
            chipGroup.addView(chip)
        }
        price.text = data.price

        //Update borrow based on return date
        val rentButton = findViewById<Button>(R.id.rent)
        if (data.returnDate.isNotEmpty()){
            rentButton.text = "Due back ${data.returnDate}"
            rentButton.isEnabled = false
        } else {
            rentButton.text = "Rent"
            rentButton.isEnabled = true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1 && resultCode == RESULT_OK){
            val tabIndex = data?.getIntExtra("tabIndex", -1)
            val isButtonDisabled = data?.getBooleanExtra("isButtonDisabled", false)
            val returnDate = data?.getStringExtra("returnDate")

            if(tabIndex != -1 && isButtonDisabled != null){
                disableBorrow[tabIndex!!] = isButtonDisabled

                if(returnDate != null){
                    itemList[tabIndex!!].returnDate = returnDate
                }
                updateUIWithItem(tabIndex!!)
            }
        }
    }
}
