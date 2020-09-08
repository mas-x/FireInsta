package com.masrooraijaz.fireinsta.ui.main.account

import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.masrooraijaz.fireinsta.R
import com.masrooraijaz.fireinsta.models.Post
import com.masrooraijaz.fireinsta.models.User
import com.masrooraijaz.fireinsta.util.FireStoreUtil
import com.masrooraijaz.fireinsta.util.LayoutUtil
import com.masrooraijaz.fireinsta.util.PAGE_SIZE
import com.masrooraijaz.fireinsta.util.PREFETCH_DISTANCE
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : BaseAccountFragment() {

    val userId = "DS7IeoPg3Xdy5tVY1peDbBuMKi93" //temp

    private lateinit var profileAdapter: ProfileFirestorePagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeObservers()
        initAdapter()
    }

    fun initAdapter() {
        val query = accountViewModel.getUserImagesQuery(userId)

        val options = FirestorePagingOptions.Builder<Post>()
            .setLifecycleOwner(viewLifecycleOwner)
            .setQuery(
                query,
                FireStoreUtil.getPagedListConfig(PAGE_SIZE, PREFETCH_DISTANCE),
                Post::class.java
            )
            .build()

        profileAdapter = ProfileFirestorePagingAdapter(
            options,
            accountViewModel.getStorageReference(userId),
            requestManager
        )

        initRecyclerView()
    }

    private fun initRecyclerView() {
        rv_profile_pics.apply {
            adapter = profileAdapter
            layoutManager =
                GridLayoutManager(
                    requireContext(),
                    LayoutUtil.calculateNoOfColumns(
                        requireContext(),
                        resources.getDimension(R.dimen.profile_column_width)
                    )
                )
        }

    }

    override fun onStart() {
        super.onStart()
        profileAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        profileAdapter.stopListening()
    }

    private fun subscribeObservers() {
        //atm only current user

        accountViewModel.getAccount(userId)
            .observe(viewLifecycleOwner, Observer { dataOrException ->

                dataListener.handleDataChange(dataOrException)

                dataOrException.data?.let {
                    setAccountProperties(it)
                }
            })
    }

    private fun setAccountProperties(user: User) {
        text_display_name.text = user.displayName
        text_followers.text = user.followers.toString()
        text_following.text = user.following.toString()
        text_posts.text = user.posts.toString()
    }

}