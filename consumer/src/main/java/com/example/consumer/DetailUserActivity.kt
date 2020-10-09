package com.example.consumer

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.consumer.db.UserContract
import com.example.consumer.helper.MappingHelper
import com.example.consumer.helper.UserHelper
import kotlinx.android.synthetic.main.activity_detail_user.*

class DetailUserActivity : AppCompatActivity() {

    private var statusFavourite: Boolean = false
    private lateinit var userHelper: UserHelper
    private lateinit var favouriteList: ArrayList<User>
    private lateinit var uriWithId: Uri
    private lateinit var favouriteUser: User

    companion object {
        const val DETAIL_USER = "detail_user"

        const val AUTHORITY = "com.example.dicodinguserapp"
        const val SCHEME = "content"

        val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME).authority(AUTHORITY).appendPath(
            UserContract.UserColumns.TABLE_NAME
        ).build()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)

        userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()
        val user = intent.getParcelableExtra<User>(DETAIL_USER) as User

        checkFavouriteUser(user)

        statusFavourite = favouriteList.indexOf(user) >= 0
        Log.d("status",statusFavourite.toString())
        setStatusFavourite(statusFavourite)

        detail_txt_username.text = user.username
        detail_txt_name.text = user.name
        Glide.with(this).load(user.avatar).into(detail_img_avatar)
        detail_txt_company.text = "Company: " + user.company
        detail_txt_location.text = "Location: " + user.location
        detail_txt_repository.text = "Repository: " + user.repository

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        sectionsPagerAdapter.username = user.username
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)
        supportActionBar?.elevation = 0f

        fab_favourite.setOnClickListener {
            if (statusFavourite) {
                userHelper.deleteById(user.username.toString())
            } else {
                val values = ContentValues()
                values.put(UserContract.UserColumns.USERNAME, user.username)
                values.put(UserContract.UserColumns.NAME, user.name)
                values.put(UserContract.UserColumns.AVATAR_URL, user.avatar)
                values.put(UserContract.UserColumns.COMPANY, user.company)
                values.put(UserContract.UserColumns.LOCATION, user.location)
                values.put(UserContract.UserColumns.REPOSITORY, user.repository)
                values.put(UserContract.UserColumns.FOLLOWER, user.follower)
                values.put(UserContract.UserColumns.FOLLOWING, user.following)
                userHelper.insert(values)
            }
            statusFavourite = !statusFavourite
            setStatusFavourite(statusFavourite)
        }

    }

    private fun setStatusFavourite(statusFavourite: Boolean) {
        if (statusFavourite) {
            fab_favourite.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else {
            fab_favourite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        userHelper.close()
    }

    private fun checkFavouriteUser(user: User){
        favouriteList = MappingHelper.mapCursorToArrayList(userHelper.queryById(user.username.toString()))
        Log.d("checkfavourite", favouriteList.toString())

    }

}


