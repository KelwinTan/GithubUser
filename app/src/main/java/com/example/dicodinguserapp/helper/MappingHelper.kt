package com.example.dicodinguserapp.helper

import android.database.Cursor
import android.util.Log
import com.example.dicodinguserapp.User
import com.example.dicodinguserapp.db.UserContract

object MappingHelper {

    fun mapCursorToArrayList(userCursor: Cursor?): ArrayList<User>{
        val userList = ArrayList<User>()
        userCursor?.apply {
            while (moveToNext()){
                val username = getString(getColumnIndexOrThrow(UserContract.UserColumns.USERNAME))
                val name = getString(getColumnIndexOrThrow(UserContract.UserColumns.NAME))
                val avatarUrl = getString(getColumnIndexOrThrow(UserContract.UserColumns.AVATAR_URL))
                val company = getString(getColumnIndexOrThrow(UserContract.UserColumns.COMPANY))
                val location = getString(getColumnIndexOrThrow(UserContract.UserColumns.LOCATION))
                val repository = getString(getColumnIndexOrThrow(UserContract.UserColumns.REPOSITORY))
                val follower = getString(getColumnIndexOrThrow(UserContract.UserColumns.FOLLOWER))
                val following = getString(getColumnIndexOrThrow(UserContract.UserColumns.FOLLOWING))
                userList.add(User(username, name, avatarUrl, company, location, repository, follower, following))
            }
        }
        return  userList
    }

    fun mapCursorToObject(userCursor: Cursor?): User {
        var user = User()
        userCursor?.apply {
            moveToFirst()
            val username = getString(getColumnIndexOrThrow(UserContract.UserColumns.USERNAME))
            val name = getString(getColumnIndexOrThrow(UserContract.UserColumns.NAME))
            val avatarUrl = getString(getColumnIndexOrThrow(UserContract.UserColumns.AVATAR_URL))
            val company = getString(getColumnIndexOrThrow(UserContract.UserColumns.COMPANY))
            val location = getString(getColumnIndexOrThrow(UserContract.UserColumns.LOCATION))
            val repository = getString(getColumnIndexOrThrow(UserContract.UserColumns.REPOSITORY))
            val follower = getString(getColumnIndexOrThrow(UserContract.UserColumns.FOLLOWER))
            val following = getString(getColumnIndexOrThrow(UserContract.UserColumns.FOLLOWING))
            user = User(username, name, avatarUrl, company, location, repository, follower, following)
        }
        Log.d("mapping", user.toString())
        return user
    }


}