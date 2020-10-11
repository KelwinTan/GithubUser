package com.example.dicodinguserapp

import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodinguserapp.db.UserContract.UserColumns.Companion.CONTENT_URI
import com.example.dicodinguserapp.helper.MappingHelper
import com.example.dicodinguserapp.helper.UserHelper
import kotlinx.android.synthetic.main.activity_favourite_user.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavouriteUserActivity : AppCompatActivity() {

    private lateinit var userHelper: UserHelper
    private lateinit var adapter: FavouriteUserAdapter

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite_user)

        rv_favourite_users.layoutManager = LinearLayoutManager(this)
        rv_favourite_users.setHasFixedSize(true)
        adapter = FavouriteUserAdapter(this)
        rv_favourite_users.adapter = adapter

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object:ContentObserver(handler){
            override fun onChange(selfChange: Boolean) {
                loadFavouriteUsersAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        if(savedInstanceState == null){
            loadFavouriteUsersAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<User>(EXTRA_STATE)
            if(list != null){
                adapter.listFavouriteUsers = list
            }
        }

    }

    private fun loadFavouriteUsersAsync(){
        GlobalScope.launch(Dispatchers.Main){
            val deferredFavouriteUsers = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val users = deferredFavouriteUsers.await()
            if(users.size > 0){
                adapter.listFavouriteUsers = users
            } else {
                adapter.listFavouriteUsers = ArrayList()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listFavouriteUsers)
    }

}