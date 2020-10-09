package com.example.consumer.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "dbgithubuserapp"

        private const val DATABASE_VERSION = 1

        private val SQL_CREATE_TABLE_USER =
            "CREATE TABLE ${UserContract.UserColumns.TABLE_NAME}" + "(${UserContract.UserColumns.USERNAME} VARCHAR(255) PRIMARY KEY," +
                    "${UserContract.UserColumns.NAME} VARCHAR(255) NOT NULL, " +
                    "${UserContract.UserColumns.COMPANY} VARCHAR(255) NOT NULL, " +
                    "${UserContract.UserColumns.LOCATION} VARCHAR(255) NOT NULL, " +
                    "${UserContract.UserColumns.REPOSITORY} VARCHAR(255) NOT NULL, " +
                    "${UserContract.UserColumns.AVATAR_URL} VARCHAR(255) NOT NULL," +
                    "${UserContract.UserColumns.FOLLOWER} VARCHAR(255) NOT NULL," +
                    "${UserContract.UserColumns.FOLLOWING} VARCHAR(255) NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_USER)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${UserContract.UserColumns.TABLE_NAME}")
        onCreate(db)
    }


}