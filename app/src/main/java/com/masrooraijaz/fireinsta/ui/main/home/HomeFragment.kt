package com.masrooraijaz.fireinsta.ui.main.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.masrooraijaz.fireinsta.R
import com.masrooraijaz.fireinsta.models.Post
import com.masrooraijaz.fireinsta.ui.FragmentItemsListener
import com.masrooraijaz.fireinsta.util.PICK_PHOTO_REQUEST_CODE
import kotlinx.android.synthetic.main.fragment_home.*

private const val TAG = "HomeFragment"

class HomeFragment : BaseHomeFragment(), PostInteractionListener, FragmentItemsListener {

    lateinit var postsAdapter: PostFirestoreRecyclerAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        Log.d(TAG, "onViewCreated: ${homeViewModel.hashCode()}")
        initAdapter()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.home_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_upload_photo -> {
                pickImage()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_PHOTO_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_PHOTO_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            data?.data?.let {
                UploadFragment.newInstance(it).show(
                    parentFragmentManager,
                    UPLOAD_FRAGMENT_TAG
                )
            }


        } else {
            Log.d(
                TAG,
                "onActivityResult: Did not select any photo..."
            )
        }
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



        postsAdapter = PostFirestoreRecyclerAdapter(
            options,
            statesListener,
            requestManager,
            homeViewModel.getStorageReference(),
            this,
            this,
            homeViewModel.getUserDocumentReference()
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

    override fun onFavt(item: Post) {

        homeViewModel.setSelectedPost(item);
        homeViewModel.favtPost()
    }

    override fun onComment(item: Post) {
        homeViewModel.setSelectedPost(item)
        findNavController().navigate(R.id.action_homeFragment_to_commentFragment)
    }

    override fun onDataChanged(itemCount: Int) {
        if (itemCount == 0) {
            text_no_items.visibility = View.VISIBLE
        } else {
            text_no_items.visibility = View.GONE
        }
    }


}