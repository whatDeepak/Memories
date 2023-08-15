package com.vyarth.memories.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.recyclerview.widget.RecyclerView
import com.vyarth.memories.R
import com.vyarth.memories.activities.AddHappyPlaceActivity
import com.vyarth.memories.activities.MainActivity
import com.vyarth.memories.database.DatabaseHandler
import com.vyarth.memories.models.MemoriesModel
import de.hdodenhof.circleimageview.CircleImageView

open class MemoriesAdapter(
    private val context: Context,
    private var list: ArrayList<MemoriesModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null


    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_memories,
                parent,
                false
            )
        )
    }

    /**
     * Binds each item in the ArrayList to a view
     *
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val ivPlaceImage: CircleImageView= holder.itemView.findViewById(R.id.iv_place_image)
        val tvTitle: TextView= holder.itemView.findViewById(R.id.tvTitle)
        val tvDescription: TextView= holder.itemView.findViewById(R.id.tvDescription)

        val model = list[position]

        if (holder is MyViewHolder) {

            ivPlaceImage.setImageURI(Uri.parse(model.image))
            tvTitle.text = model.title
            tvDescription.text = model.description

            holder.itemView.setOnClickListener {

                if (onClickListener != null) {
                    onClickListener!!.onClick(position, model)
                }
            }
        }
    }

    /**
     * Gets the number of items in the list
     */
    override fun getItemCount(): Int {
        return list.size
    }

    /**
     * A function to edit the added happy place detail and pass the existing details through intent.
     */
    fun notifyEditItem(activity: Activity, position: Int, requestCode: Int) {
        val intent = Intent(context, AddHappyPlaceActivity::class.java)
        intent.putExtra(MainActivity.EXTRA_PLACE_DETAILS, list[position])
        activity.startActivityForResult(
            intent,
            requestCode
        ) // Activity is started with requestCode

        notifyItemChanged(position) // Notify any registered observers that the item at position has changed.
    }

    /**
     * A function to delete the added happy place detail from the local storage.
     */
    fun removeAt(position: Int) {

        val dbHandler = DatabaseHandler(context)
        val isDeleted = dbHandler.deletememories(list[position])

        if (isDeleted > 0) {
            list.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    /**
     * A function to bind the onclickListener.
     */
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, model: MemoriesModel)
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}