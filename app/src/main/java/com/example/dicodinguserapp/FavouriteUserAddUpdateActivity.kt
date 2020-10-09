package com.example.dicodinguserapp

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.dicodinguserapp.db.UserContract
import com.example.dicodinguserapp.helper.UserHelper
import kotlinx.android.synthetic.main.activity_favourite_user_add_update.*

class FavouriteUserAddUpdateActivity : AppCompatActivity(), View.OnClickListener {

    private var isEdit = false
    private var favouriteUser: User? = null
    private var position: Int = 0
    private lateinit var userHelper: UserHelper

    companion object {
        const val EXTRA_FAVOURITE_USER = "extra_favourite_user"
        const val EXTRA_POSITION = "extra_position"
        const val REQUEST_ADD = 100
        const val RESULT_ADD = 101
        const val REQUEST_UPDATE = 200
        const val RESULT_UPDATE = 201
        const val RESULT_DELETE = 301
        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELETE = 20


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite_user_add_update)

        userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()

        favouriteUser = intent.getParcelableExtra(EXTRA_FAVOURITE_USER)
        if(favouriteUser != null){
            position = intent.getIntExtra(EXTRA_POSITION, 0)
            isEdit = true
        } else {
            favouriteUser = User()
        }

        val actionBarTitle: String
        val btnTitle: String

        if(isEdit){
            actionBarTitle = "Ubah"
            btnTitle = "Update"
            favouriteUser?.let {
                edt_username.setText(it.username)
                edt_name.setText(it.name)
                edt_company.setText(it.company)
                edt_location.setText(it.location)
                edt_repository.setText(it.repository)
            }
        }else {
            actionBarTitle = "Tambah"
            btnTitle = "Simpan"
        }
        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btn_submit.text = btnTitle
        btn_submit.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if(view.id == R.id.btn_submit){
            val username = edt_username.text.toString().trim()
            val name = edt_name.text.toString().trim()
            val company = edt_company.text.toString().trim()
            val location = edt_location.text.toString().trim()
            val repository = edt_repository.text.toString().trim()

            if(username.isEmpty()){
                edt_username.error = "Field cannot be blank"
                return
            }
            favouriteUser?.username = username
            favouriteUser?.name = name
            favouriteUser?.company = company
            favouriteUser?.location = location
            favouriteUser?.repository = repository

            val intent = Intent()
            intent.putExtra(EXTRA_FAVOURITE_USER, favouriteUser)
            intent.putExtra(EXTRA_POSITION, position)

            val values = ContentValues()
            values.put(UserContract.UserColumns.USERNAME, username)
            values.put(UserContract.UserColumns.NAME, name)
            values.put(UserContract.UserColumns.COMPANY, company)
            values.put(UserContract.UserColumns.LOCATION, location)
            values.put(UserContract.UserColumns.REPOSITORY, repository)





        }


    }


}