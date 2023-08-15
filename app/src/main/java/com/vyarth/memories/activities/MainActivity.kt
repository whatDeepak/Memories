package com.vyarth.memories.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.vyarth.memories.R
import com.vyarth.memories.adapters.MemoriesAdapter
import com.vyarth.memories.database.DatabaseHandler
import com.vyarth.memories.models.MemoriesModel
import com.vyarth.memories.utils.SwipeToDeleteCallback
import pl.kitek.rvswipetodelete.SwipeToEditCallback

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setting an click event for Fab Button and calling the AddHappyPlaceActivity.
        val fabAddHappyPlace = findViewById<FloatingActionButton>(R.id.fabAddHappyPlace)
        fabAddHappyPlace.setOnClickListener {
            val intent = Intent(this@MainActivity, AddHappyPlaceActivity::class.java)
            startActivityForResult(intent, ADD_PLACE_ACTIVITY_REQUEST_CODE)
        }

        getHappyPlacesListFromLocalDB()
    }

    /**
     * A function to get the list of happy place from local database.
     */
    private fun getHappyPlacesListFromLocalDB() {
        val rvHappyPlacesList=findViewById<RecyclerView>(R.id.rv_happy_places_list)
        val tvNoRecordsAvailable = findViewById<TextView>(R.id.tv_no_records_available)

        val dbHandler = DatabaseHandler(this)

        val getMemoriesList = dbHandler.getmemoriesList()

        if (getMemoriesList.size > 0) {
            if (getMemoriesList.size > 0) {
                rvHappyPlacesList.visibility = View.VISIBLE
                tvNoRecordsAvailable.visibility = View.GONE
                setupHappyPlacesRecyclerView(getMemoriesList)
            } else {
                rvHappyPlacesList.visibility = View.GONE
                tvNoRecordsAvailable.visibility = View.VISIBLE
            }
        }
    }

    // Call Back method  to get the Message form other Activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // check if the request code is same as what is passed  here it is 'ADD_PLACE_ACTIVITY_REQUEST_CODE'
        if (requestCode == ADD_PLACE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                getHappyPlacesListFromLocalDB()
            }else{
                Log.e("Activity", "Cancelled or Back Pressed")
            }
        }
    }

    /**
     * A function to populate the recyclerview to the UI.
     */
    private fun setupHappyPlacesRecyclerView(happyPlacesList: ArrayList<MemoriesModel>) {

        val rvHappyPlacesList=findViewById<RecyclerView>(R.id.rv_happy_places_list)
        rvHappyPlacesList.layoutManager = LinearLayoutManager(this)
        rvHappyPlacesList.setHasFixedSize(true)

        val placesAdapter = MemoriesAdapter(this, happyPlacesList)
        rvHappyPlacesList.adapter = placesAdapter

        placesAdapter.setOnClickListener(object :
            MemoriesAdapter.OnClickListener {
            override fun onClick(position: Int, model: MemoriesModel){
                val intent = Intent(this@MainActivity, HappyPlaceDetailActivity::class.java)
                intent.putExtra(EXTRA_PLACE_DETAILS, model) // Passing the complete serializable data class to the detail activity using intent.
                startActivity(intent)
            }
        })

        val editSwipeHandler = object : SwipeToEditCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = rvHappyPlacesList.adapter as MemoriesAdapter
                adapter.notifyEditItem(
                    this@MainActivity,
                    viewHolder.adapterPosition,
                    ADD_PLACE_ACTIVITY_REQUEST_CODE
                )
            }
        }
        val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
        editItemTouchHelper.attachToRecyclerView(rvHappyPlacesList)

        val deleteSwipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = rvHappyPlacesList.adapter as MemoriesAdapter
                adapter.removeAt(viewHolder.adapterPosition)

                getHappyPlacesListFromLocalDB() // Gets the latest list from the local database after item being delete from it.
            }
        }
        val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
        deleteItemTouchHelper.attachToRecyclerView(rvHappyPlacesList)
    }

    companion object{
        private const val ADD_PLACE_ACTIVITY_REQUEST_CODE = 1
        internal const val EXTRA_PLACE_DETAILS = "extra_place_details"
    }
}