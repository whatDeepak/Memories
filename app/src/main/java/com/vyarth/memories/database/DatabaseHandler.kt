package com.vyarth.memories.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.vyarth.memories.models.MemoriesModel

//creating the database logic, extending the SQLiteOpenHelper base class
class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1 // Database version
        private const val DATABASE_NAME = "MemoriesDatabase" // Database name
        private const val TABLE_MEMORIES = "MemoriesTable" // Table Name

        //All the Columns names
        private const val KEY_ID = "_id"
        private const val KEY_TITLE = "title"
        private const val KEY_IMAGE = "image"
        private const val KEY_DESCRIPTION = "description"
        private const val KEY_DATE = "date"
        private const val KEY_LOCATION = "location"
        private const val KEY_LATITUDE = "latitude"
        private const val KEY_LONGITUDE = "longitude"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        //creating table with fields
        val CREATE_MEMORIES_TABLE = ("CREATE TABLE " + TABLE_MEMORIES + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_IMAGE + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_LOCATION + " TEXT,"
                + KEY_LATITUDE + " TEXT,"
                + KEY_LONGITUDE + " TEXT)")
        db?.execSQL(CREATE_MEMORIES_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_MEMORIES")
        onCreate(db)
    }

    /**
     * Function to insert a Happy Place details to SQLite Database.
     */
    fun addmemories(memories: MemoriesModel): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE, memories.title) // ModelClass TITLE
        contentValues.put(KEY_IMAGE, memories.image) // ModelClass IMAGE
        contentValues.put(
            KEY_DESCRIPTION,
            memories.description
        ) // ModelClass DESCRIPTION
        contentValues.put(KEY_DATE, memories.date) // ModelClass DATE
        contentValues.put(KEY_LOCATION, memories.location) // ModelClass LOCATION
        contentValues.put(KEY_LATITUDE, memories.latitude) // ModelClass LATITUDE
        contentValues.put(KEY_LONGITUDE, memories.longitude) // ModelClass LONGITUDE

        // Inserting Row
        val result = db.insert(TABLE_MEMORIES, null, contentValues)
        //2nd argument is String containing nullColumnHack

        db.close() // Closing database connection
        return result
    }

    /**
     * Function to read all the list of Happy Places data which are inserted.
     */
    fun getmemoriesList(): ArrayList<MemoriesModel> {

        // A list is initialize using the data model class in which we will add the values from cursor.
        val memoriesList: ArrayList<MemoriesModel> = ArrayList()

        val selectQuery = "SELECT  * FROM $TABLE_MEMORIES" // Database select query

        val db = this.readableDatabase

        try {
            val cursor: Cursor = db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst()) {
                do {
                    val place = MemoriesModel(
                        cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_TITLE)),
                        cursor.getString(cursor.getColumnIndex(KEY_IMAGE)),
                        cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndex(KEY_DATE)),
                        cursor.getString(cursor.getColumnIndex(KEY_LOCATION)),
                        cursor.getDouble(cursor.getColumnIndex(KEY_LATITUDE)),
                        cursor.getDouble(cursor.getColumnIndex(KEY_LONGITUDE))
                    )
                    memoriesList.add(place)

                } while (cursor.moveToNext())
            }
            cursor.close()
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        return memoriesList
    }

    /**
     * Function to update record
     */
    fun updateMemories(memories: MemoriesModel): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE, memories.title) // ModelClass TITLE
        contentValues.put(KEY_IMAGE, memories.image) // ModelClass IMAGE
        contentValues.put(
            KEY_DESCRIPTION,
            memories.description
        ) // ModelClass DESCRIPTION
        contentValues.put(KEY_DATE, memories.date) // ModelClass DATE
        contentValues.put(KEY_LOCATION, memories.location) // ModelClass LOCATION
        contentValues.put(KEY_LATITUDE, memories.latitude) // ModelClass LATITUDE
        contentValues.put(KEY_LONGITUDE, memories.longitude) // ModelClass LONGITUDE

        // Updating Row
        val success =
            db.update(TABLE_MEMORIES, contentValues, KEY_ID + "=" + memories.id, null)
        //2nd argument is String containing nullColumnHack

        db.close() // Closing database connection
        return success
    }

    /**
     * Function to delete happy place details.
     */
    fun deletememories(memories: MemoriesModel): Int {
        val db = this.writableDatabase
        // Deleting Row
        val success = db.delete(TABLE_MEMORIES, KEY_ID + "=" + memories.id, null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }
}
