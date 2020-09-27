package com.masrooraijaz.fireinsta.ui.main.home.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.masrooraijaz.fireinsta.R
import com.masrooraijaz.fireinsta.models.Post
import com.masrooraijaz.fireinsta.ui.InteractionListener
import com.masrooraijaz.fireinsta.ui.main.home.BaseHomeFragment
import com.masrooraijaz.fireinsta.util.SEARCH_GRID_NUM_OF_COLUMNS
import kotlinx.android.synthetic.main.fragment_search_posts.*

private const val TAG = "SearchPostsFragment"

class SearchPostsFragment : BaseHomeFragment(), InteractionListener<Post> {
    lateinit var searchPostsAdapter: SearchPostsFirestoreRecyclerAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_posts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeObservers()
    }

    fun subscribeObservers() {
        homeViewModel.searchAccountQuery.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "subscribeObservers: Initializing adapter....")
            initAdapter(it)
        })
    }

    fun initAdapter(searchText: String) {

        val query = homeViewModel.getSearchPostsQuery(searchText)

        val options = FirestoreRecyclerOptions.Builder<Post>()
            .setLifecycleOwner(viewLifecycleOwner)
            .setQuery(
                query,
                Post::class.java
            )
            .build()


        searchPostsAdapter = SearchPostsFirestoreRecyclerAdapter(
            options,
            statesListener,
            requestManager,
            homeViewModel.getStorageReference(),
            this,
            homeViewModel.getUserDocumentReference()
        )


        initRecyclerView()
    }

    private fun initRecyclerView() {

        rv_search_posts.apply {
            layoutManager =
                StaggeredGridLayoutManager(SEARCH_GRID_NUM_OF_COLUMNS, LinearLayoutManager.VERTICAL)
            adapter = searchPostsAdapter
        }
    }

    override fun onClick(item: Post) {
        homeViewModel.setSelectedPost(item)
        findNavController().navigate(R.id.action_searchFragment_to_viewImageFragment2)
    }


}