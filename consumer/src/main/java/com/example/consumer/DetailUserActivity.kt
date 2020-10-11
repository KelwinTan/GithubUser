package com.example.consumer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detail_user.*

class DetailUserActivity : AppCompatActivity() {

    private var statusFavourite: Boolean = true

    companion object {
        const val DETAIL_USER = "detail_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)

        val user = intent.getParcelableExtra<User>(DETAIL_USER) as User

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
    }

    private fun setStatusFavourite(statusFavourite: Boolean) {
        if (statusFavourite) {
            fab_favourite.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else {
            fab_favourite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }

}


