package com.example.dicodinguserapp.helper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.dicodinguserapp.db.DatabaseHelper
import com.example.dicodinguserapp.db.UserContract
import com.example.dicodinguserapp.db.UserContract.UserColumns.Companion.TABLE_NAME
import java.sql.SQLException

class UserHelper(context: Context) {

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private lateinit var databaseHelper: DatabaseHelper
        private var INSTANCE: UserHelper? = null
        fun getInstance(context: Context): UserHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserHelper(context)
            }


        private lateinit var database: SQLiteDatabase
    }

    init {
        databaseHelper = DatabaseHelper(context)
    }

    @Throws(SQLException::class)
    fun open() {
        database = databaseHelper.writableDatabase
    }

    fun close() {
        databaseHelper.close()
        if (database.isOpen) database.close()
    }

    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "${UserContract.UserColumns.USERNAME} ASC"
        )
    }

    fun queryById(id: String): Cursor {
        Log.d("querybyid", id.toString())
        return database.query(
            DATABASE_TABLE,
            null,
            "${UserContract.UserColumns.USERNAME} = ?", arrayOf(id),
            null,
            null,
            null,
            null
        )
    }

    fun insert(values: ContentValues?): Long {
        Log.d("insert-values", values.toString())
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun update(username: String, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, "${UserContract.UserColumns.USERNAME} = ?", arrayOf(username))
    }

    fun deleteById(id: String): Int {
        return database.delete(DATABASE_TABLE, "${UserContract.UserColumns.USERNAME} = '$id'", null)
    }

}