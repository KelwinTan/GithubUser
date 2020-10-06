package com.example.dicodinguserapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment_follower.view.*
import kotlinx.android.synthetic.main.fragment_following.view.*
import org.json.JSONArray
import org.json.JSONObject

/**
 * A simple [Fragment] subclass.
 * Use the [FollowingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FollowingFragment : Fragment() {

    private var username: String? = null
    private var followingList = arrayListOf<User>()
    private lateinit var listFollowingAdapter: ListUserAdapter
    private lateinit var myFollowingRecyclerView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listFollowingAdapter = ListUserAdapter(followingList)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        username = arguments?.getString("username")
        getUser(username.toString())
        myFollowingRecyclerView = inflater.inflate(R.layout.fragment_following, container, false)
        myFollowingRecyclerView.rv_following.setHasFixedSize(true)
        myFollowingRecyclerView.rv_following.layoutManager = LinearLayoutManager(activity)
        myFollowingRecyclerView.rv_following.adapter = listFollowingAdapter

        return myFollowingRecyclerView
    }

    companion object {
        private val ARG_USERNAME = "username"

        fun newInstance(username: String?): Fragment {
            val fragment = FollowingFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)

            fragment.arguments = bundle
            return fragment
        }
    }

    private fun getUser(query: String) {
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 13104280bc3102d95eb1e2b1416e49d8921fe080")
        client.addHeader("User-Agent", "request")
        client.addHeader("Accept", "application/json")
        client.addHeader("Content-Type", "application/json")

        val url = "https://api.github.com/users/$query/following"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                try {
                    val jsonArray = JSONArray(result)

                    activity?.runOnUiThread {
                        followingList.clear()
                        myFollowingRecyclerView.rv_following.recycledViewPool.clear()
                        myFollowingRecyclerView.rv_following.adapter = null
                        listFollowingAdapter.notifyDataSetChanged()
                    }

                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val loginUsername = jsonObject.getString("login")
                        getUserDetail(loginUsername)
                    }

                    activity?.runOnUiThread {
                        listFollowingAdapter = ListUserAdapter(followingList)
                        listFollowingAdapter.setOnItemClickCallback(object :
                            ListUserAdapter.OnItemClickCallback {
                            override fun onItemClicked(user: User) {
                                val moveUser = Intent(activity, DetailUserActivity::class.java)
                                moveUser.putExtra(DetailUserActivity.DETAIL_USER, user)
                                startActivity(moveUser)
                            }
                        })
                        myFollowingRecyclerView.rv_following.adapter = listFollowingAdapter
                        listFollowingAdapter.notifyDataSetChanged()
                    }
                } catch (e: Exception) {
                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getUserDetail(query: String) {
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 13104280bc3102d95eb1e2b1416e49d8921fe080")
        client.addHeader("User-Agent", "request")
        client.addHeader("Accept", "application/json")
        client.addHeader("Content-Type", "application/json")

        val url = " https://api.github.com/users/$query"

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                try {
                    val responseObject = JSONObject(result)
                    val username = responseObject.getString("login")
                    val name = responseObject.getString("name")
                    val company = responseObject.getString("company")
                    val location = responseObject.getString("location")
                    val repository = responseObject.getString("public_repos")
                    val follower = responseObject.getString("followers")
                    val following = responseObject.getString("following")
                    val avatar = responseObject.getString("avatar_url")

                    val newUser = User(
                        username,
                        name,
                        avatar,
                        company,
                        location,
                        repository,
                        follower,
                        following
                    )
                    activity?.runOnUiThread {
                        followingList.add(newUser)
                        listFollowingAdapter.notifyDataSetChanged()
                    }

                } catch (e: Exception) {
                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })

    }


}