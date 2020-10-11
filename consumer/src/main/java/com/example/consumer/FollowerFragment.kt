package com.example.consumer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment_follower.view.*
import kotlinx.android.synthetic.main.fragment_following.*
import org.json.JSONArray
import org.json.JSONObject

/**
 * A simple [Fragment] subclass.
 * Use the [FollowerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FollowerFragment : Fragment() {

    private var username: String? = null
    private var followersList = arrayListOf<User>()
    private lateinit var listFollowersAdapter: ListUserAdapter
    private lateinit var myFollowerRecyclerView: View

    companion object {
        private const val ARG_USERNAME = "username"

        fun newInstance(username: String?): Fragment {
            val fragment = FollowerFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)

            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listFollowersAdapter = ListUserAdapter(followersList)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        username = arguments?.getString("username")
        getUser(username.toString())
        myFollowerRecyclerView = inflater.inflate(R.layout.fragment_follower, container, false)
        myFollowerRecyclerView.rv_followers.setHasFixedSize(true)
        myFollowerRecyclerView.rv_followers.layoutManager = LinearLayoutManager(activity)
        myFollowerRecyclerView.rv_followers.adapter = listFollowersAdapter

        return myFollowerRecyclerView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar.visibility = View.VISIBLE

    }

    private fun getUser(query: String) {
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 13104280bc3102d95eb1e2b1416e49d8921fe080")
        client.addHeader("User-Agent", "request")
        client.addHeader("Accept", "application/json")
        client.addHeader("Content-Type", "application/json")

        val url = "https://api.github.com/users/$query/followers"
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
                        followersList.clear()
                        myFollowerRecyclerView.rv_followers.recycledViewPool.clear()
                        myFollowerRecyclerView.rv_followers.adapter = null
                        listFollowersAdapter.notifyDataSetChanged()
                    }

                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val loginUsername = jsonObject.getString("login")
                        getUserDetail(loginUsername)
                    }

                    activity?.runOnUiThread {
                        listFollowersAdapter = ListUserAdapter(followersList)
                        listFollowersAdapter.setOnItemClickCallback(object :
                            ListUserAdapter.OnItemClickCallback {
                            override fun onItemClicked(user: User) {
                                val moveUser = Intent(activity, DetailUserActivity::class.java)
                                moveUser.putExtra(DetailUserActivity.DETAIL_USER, user)
                                startActivity(moveUser)
                            }
                        })
                        myFollowerRecyclerView.rv_followers.adapter = listFollowersAdapter
                        listFollowersAdapter.notifyDataSetChanged()
                        progressBar.visibility = View.INVISIBLE

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
                        followersList.add(newUser)
                        listFollowersAdapter.notifyDataSetChanged()
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