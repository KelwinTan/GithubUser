package com.example.consumer

import android.database.ContentObserver
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.consumer.helper.MappingHelper
import com.example.consumer.helper.UserHelper
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
        const val AUTHORITY = "com.example.dicodinguserapp"
        const val SCHEME = "content"

        val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME).authority(AUTHORITY).appendPath(
            "favourite_user"
        ).build()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite_user)

        rv_favourite_users.layoutManager = LinearLayoutManager(this)
        rv_favourite_users.setHasFixedSize(true)
        adapter = FavouriteUserAdapter(this)
        rv_favourite_users.adapter = adapter

//        userHelper = UserHelper.getInstance(applicationContext)
//        userHelper.open()

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object:ContentObserver(handler){
            override fun onChange(selfChange: Boolean) {
                loadFavouriteUsersAsync()
            }
        }

        Log.d("favourite-content", CONTENT_URI.toString())

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
            Log.d("load", adapter.listFavouriteUsers.toString())
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listFavouriteUsers)
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        userHelper.close()
//    }

}