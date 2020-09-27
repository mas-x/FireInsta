package com.masrooraijaz.fireinsta.ui.main.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.masrooraijaz.fireinsta.R
import com.masrooraijaz.fireinsta.util.FirebaseStoragePaths
import com.masrooraijaz.fireinsta.util.SuccessHandling
import kotlinx.android.synthetic.main.fragment_view_image.*

private const val TAG = "ViewPostFragment"

class ViewPostFragment : BaseHomeFragment() {


    val args: ViewPostFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        args.post?.let { homeViewModel.setSelectedPost(it) }

        return inflater.inflate(R.layout.fragment_view_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()

        image_comments.setOnClickListener {
            findNavController().navigate(R.id.action_viewImageFragment_to_commentFragment)
        }

        image_favts.setOnClickListener {
            Log.d(TAG, "onViewCreated: Favt post...")
            favtPost()
        }

        image_delete.setOnClickListener {
            //todo: ask if user is sure then delete
            findNavController().popBackStack()
            homeViewModel.deletePost().observe(viewLifecycleOwner, Observer {
                dataListener.handleDataChange(it)
            })

        }

    }

    private fun favtPost() {
        homeViewModel.favtPost()?.observe(viewLifecycleOwner, Observer {
            it.data?.let {
                if (it.msg == SuccessHandling.FAVT_SUCCESS) {
                    image_favts.setImageResource(R.drawable.ic_filled_favorite_24_white)
                } else if (it.msg == SuccessHandling.UNFAVT_SUCCESS) {
                    image_favts.setImageResource(R.drawable.ic_baseline_favorite_border_white_24)
                }
            }
            it.exception?.let {
                Log.d(TAG, "favtPost: $it ")
            }
        })
    }


    private fun subscribeObservers() {
        homeViewModel.selectedPost.observe(viewLifecycleOwner, Observer { post ->

            requestManager.load(
                post.userId?.let { userId ->
                    post.storageFileName?.let {
                        homeViewModel.getStorageReference().child(userId)
                            .child(FirebaseStoragePaths.USER_IMAGES_PATH).child(it)
                    }


                }

            ).into(imageview_post)
            image_caption.text = post.caption

            post.userId?.let { userId ->
                if (userId == homeViewModel.getLoggedInUserUid()) {
                    image_delete.visibility = View.VISIBLE
                }
            }

            post.likedBy?.let {
                if (it.contains(homeViewModel.getUserDocumentReference())) {
                    image_favts.setImageResource(R.drawable.ic_filled_favorite_24_white)
                }
            }

        })
    }
}