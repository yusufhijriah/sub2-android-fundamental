package com.dicoding.submission2

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.submission2.adapter.ListUserAdapter
import com.dicoding.submission2.api.ApiConfig
import com.dicoding.submission2.api.UserResponseItem
import com.dicoding.submission2.databinding.FragmentFollowingAndFollowerBinding
import com.google.android.material.tabs.TabLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingAndFollowerFragment : Fragment() {

    private lateinit var binding: FragmentFollowingAndFollowerBinding
    private val adapter = ListUserAdapter(arrayListOf())
    private var username: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowingAndFollowerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()
        setupListener()

        username = arguments?.getString(EXTRA_USERNAME)
        findUsersFollowers(username ?: "")
    }

    private fun setupAdapter() {
        with(binding.rvFollowing) {
            this@FollowingAndFollowerFragment.adapter.setOnItemClickCallback(object: ListUserAdapter.OnItemClickCallback{
                override fun onItemClicked(user: User) {
                    startActivity(Intent(requireContext(), DetailUserActivity::class.java).apply {
                        putExtra(DetailUserActivity.EXTRA_USER, user)
                    })
                }

            })
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = this@FollowingAndFollowerFragment.adapter
        }
    }

    private fun setupListener() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> {
                        findUsersFollowers(username ?: "")
                    }
                    1 -> {
                        findUsersFollowing(username ?: "")
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}

        })
    }

    private fun findUsersFollowers(username: String) {
        binding.progressBar.isVisible = true
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<UserResponseItem>> {
            override fun onResponse(
                call: Call<List<UserResponseItem>>,
                response: Response<List<UserResponseItem>>
            ) {
                val responseBody = response.body();
                if (responseBody != null) {
                    val dataUser = responseBody.map { user ->
                        User(
                            user.login,
                            user.avatarUrl,
                            "",
                            "",
                            "",
                            user.publicRepos.toString(),
                            user.followers.toString(),
                            user.following.toString()
                        )
                    }

                    adapter.setFilterUser(dataUser)
                    binding.progressBar.isVisible = false
                }
            }

            override fun onFailure(call: Call<List<UserResponseItem>>, t: Throwable) {
                binding.progressBar.isVisible = false
            }
        })
    }

    private fun findUsersFollowing(username: String) {
        binding.progressBar.isVisible = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<UserResponseItem>> {
            override fun onResponse(
                call: Call<List<UserResponseItem>>,
                response: Response<List<UserResponseItem>>
            ) {
                val responseBody = response.body();
                if (responseBody != null) {
                    val dataUser = responseBody.map { user ->
                        User(
                            user.login,
                            user.avatarUrl,
                            "",
                            "",
                            "",
                            user.publicRepos.toString(),
                            user.followers.toString(),
                            user.following.toString()
                        )
                    }

                    adapter.setFilterUser(dataUser)
                    binding.progressBar.isVisible = false
                }
            }

            override fun onFailure(call: Call<List<UserResponseItem>>, t: Throwable) {
                binding.progressBar.isVisible = false
            }
        })
    }

    companion object {
        const val EXTRA_USERNAME = "username"
    }
}