package com.example.dicodinguserapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_detail_user.*

class DetailUserActivity : AppCompatActivity() {
    companion object {
        const val DETAIL_USER = "detail_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)

        val user = intent.getParcelableExtra<User>(DETAIL_USER) as User

        val detail_txt_username: TextView = findViewById(R.id.detail_txt_username)
        val detail_txt_name: TextView = findViewById(R.id.detail_txt_name)
        val detail_img_avatar: CircleImageView = findViewById(R.id.detail_img_avatar)
        val detail_txt_company: TextView = findViewById(R.id.detail_txt_company)
        val detail_txt_location: TextView = findViewById(R.id.detail_txt_location)
        val detail_txt_repository :TextView = findViewById(R.id.detail_txt_repository)

        detail_txt_username.text = user.username
        detail_txt_name.text = user.name
//        detail_img_avatar.setImageResource(user.avatar)
        Glide.with(this).load(user.avatar).into(detail_img_avatar)
        detail_txt_company.text = "Company: " + user.company
        detail_txt_location.text = "Location: " + user.location
        detail_txt_repository.text = "Repository: " + user.repository
//        detail_txt_follower.text = "Follower: " + user.follower
//        detail_txt_following.text = "Following: " + user.following

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        sectionsPagerAdapter.username = user.username
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)
        supportActionBar?.elevation = 0f
    }
}