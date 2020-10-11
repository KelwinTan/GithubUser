package com.example.consumer

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_user.view.*

class FavouriteUserAdapter(private val activity: Activity) :
    RecyclerView.Adapter<FavouriteUserAdapter.FavouriteUserViewHolder>() {

    var listFavouriteUsers = ArrayList<User>()
        set(listFavouriteUsers) {
            if (listFavouriteUsers.size > 0) {
                this.listFavouriteUsers.clear()
            }
            this.listFavouriteUsers.addAll(listFavouriteUsers)
            notifyDataSetChanged()
        }

    inner class FavouriteUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(favouriteUser: User) {
            with(itemView) {
                Glide.with(itemView.context).load(favouriteUser.avatar).into(img_avatar)
                txt_username.text = favouriteUser.username
                txt_follower.text = "Followers: " + favouriteUser.follower
                txt_following.text = "Following: " + favouriteUser.following
                txt_repository.text = "Repository: " + favouriteUser.repository

                itemView.setOnClickListener(
                    CustomOnItemClickListener(adapterPosition,
                        object : CustomOnItemClickListener.OnItemClickCallback {
                            override fun onItemClicked(view: View, position: Int) {
                                val intent = Intent(activity, DetailUserActivity::class.java)
                                intent.putExtra(DetailUserActivity.DETAIL_USER, favouriteUser)
                                activity.startActivity(intent)
                            }

                        })
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteUserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return FavouriteUserViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavouriteUserViewHolder, position: Int) {
        holder.bind(listFavouriteUsers[position])
    }

    override fun getItemCount(): Int = this.listFavouriteUsers.size

}