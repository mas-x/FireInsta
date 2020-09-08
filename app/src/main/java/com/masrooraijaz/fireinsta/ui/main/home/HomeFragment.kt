package com.masrooraijaz.fireinsta.ui.main.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreArray
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.masrooraijaz.fireinsta.R
import com.masrooraijaz.fireinsta.models.Post
import com.masrooraijaz.fireinsta.ui.InteractionListener
import com.masrooraijaz.fireinsta.ui.main.account.ProfileFirestorePagingAdapter
import com.masrooraijaz.fireinsta.util.FireStoreUtil
import com.masrooraijaz.fireinsta.util.PAGE_SIZE
import com.masrooraijaz.fireinsta.util.PREFETCH_DISTANCE
import kotlinx.android.synthetic.main.fragment_home.*

private const val TAG = "HomeFragment"
class HomeFragment : BaseHomeFragment(), InteractionListener<Post> {

    lateinit var postsAdapter: PostFirestorePagingAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        Log.d(TAG, "onViewCreated: ${homeViewModel.hashCode()}")
        initAdapter()
    }

    private fun initAdapter() {

        val query = homeViewModel.getPostsQuery()

        val options = FirestorePagingOptions.Builder<Post>()
            .setLifecycleOwner(viewLifecycleOwner)
            .setQuery(
                query,
                FireStoreUtil.getPagedListConfig(PAGE_SIZE, PREFETCH_DISTANCE),
                Post::class.java
            )
            .build()



        postsAdapter = PostFirestorePagingAdapter(
            options,
            statesListener,
            requestManager,
            homeViewModel.getStorageReference(),
            this
        )


        initRecyclerView()
    }

    private fun initRecyclerView() {
        rv_home.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = postsAdapter
        }
    }

    override fun onClick(item: Post) {
        Log.d(TAG, "onClick: ${findNavController().currentDestination}")
        homeViewModel.setSelectedPost(item)
        findNavController().navigate(R.id.action_homeFragment_to_viewImageFragment)
    }


}