package com.example.dicodinguserapp.db

import android.net.Uri
import android.provider.BaseColumns

internal class UserContract {



    internal class UserColumns : BaseColumns {


        companion object {
            const val TABLE_NAME = "favourite_user"
            const val USERNAME = "username"
            const val NAME = "name"
            const val AVATAR_URL = "avatar_url"
            const val COMPANY = "company"
            const val LOCATION = "location"
            const val REPOSITORY = "repository"
            const val FOLLOWER = "follower"
            const val FOLLOWING = "following"

            const val AUTHORITY = "com.example.dicodinguserapp"
            const val SCHEME = "content"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME).authority(AUTHORITY).appendPath(
                TABLE_NAME).build()


        }
    }


}