package com.vyarth.memories.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.vyarth.memories.R
import com.vyarth.memories.models.MemoriesModel


class HappyPlaceDetailActivity : AppCompatActivity() {

    /**
     * This function is auto created by Android when the Activity Class is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        // This is used to align the xml view to this class
        setContentView(R.layout.activity_happy_place_detail)

        var happyPlaceDetailModel: MemoriesModel? = null

        val toolbarHappyPlaceDetail=findViewById<Toolbar>(R.id.toolbar_happy_place_detail)
        val ivPlaceImage = findViewById<ImageView>(R.id.iv_place_image)
        val tvDescription: TextView = findViewById(R.id.tv_description)
        val tvLocation: TextView = findViewById(R.id.tv_location)
        val btnViewOnMap = findViewById<Button>(R.id.btn_view_on_map)

        if (intent.hasExtra(MainActivity.EXTRA_PLACE_DETAILS)) {
            // get the Serializable data model class with the details in it
            happyPlaceDetailModel =
                intent.getParcelableExtra(MainActivity.EXTRA_PLACE_DETAILS) as? MemoriesModel
        }

        if (happyPlaceDetailModel != null) {
            setSupportActionBar(toolbarHappyPlaceDetail)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = happyPlaceDetailModel.title

            toolbarHappyPlaceDetail.setNavigationOnClickListener {
                onBackPressed()
            }

            ivPlaceImage.setImageURI(Uri.parse(happyPlaceDetailModel.image))
            tvDescription.text = happyPlaceDetailModel.description
            tvLocation.text = happyPlaceDetailModel.location
        }
    }
}