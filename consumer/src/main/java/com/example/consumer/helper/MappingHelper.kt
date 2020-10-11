package com.example.consumer.helper

import android.database.Cursor
import com.example.consumer.User
import com.example.consumer.db.UserContract

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
}