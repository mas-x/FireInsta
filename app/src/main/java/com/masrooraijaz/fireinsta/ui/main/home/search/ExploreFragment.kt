package com.masrooraijaz.fireinsta.ui.main.home.search

import android.os.Bundle
import android.view.*
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.masrooraijaz.fireinsta.R
import com.masrooraijaz.fireinsta.models.Post
import com.masrooraijaz.fireinsta.ui.InteractionListener
import com.masrooraijaz.fireinsta.ui.main.home.BaseHomeFragment
import com.masrooraijaz.fireinsta.util.SEARCH_GRID_NUM_OF_COLUMNS
import kotlinx.android.synthetic.main.fragment_explore.*


class ExploreFragment : BaseHomeFragment(), InteractionListener<Post> {


    lateinit var searchPostsFirestoreRecyclerAdapter: SearchPostsFirestoreRecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        initAdapter()

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_icon_menu, menu);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_search_icon -> {
                findNavController().navigate(R.id.action_exploreFragment_to_searchFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initAdapter() {

        val query = homeViewModel.getPostsQuery()

        val options = FirestoreRecyclerOptions.Builder<Post>()
            .setLifecycleOwner(viewLifecycleOwner)
            .setQuery(
                query,
                Post::class.java
            )
            .build()


        searchPostsFirestoreRecyclerAdapter = SearchPostsFirestoreRecyclerAdapter(
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
        rv_search.apply {
            layoutManager =
                StaggeredGridLayoutManager(SEARCH_GRID_NUM_OF_COLUMNS, LinearLayoutManager.VERTICAL)
            adapter = searchPostsFirestoreRecyclerAdapter
        }
    }

    override fun onClick(item: Post) {
        homeViewModel.setSelectedPost(item)
        findNavController().navigate(R.id.action_searchFragment_to_viewImageFragment)
    }
}
