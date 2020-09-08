package com.masrooraijaz.fireinsta.ui.main.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.masrooraijaz.fireinsta.R
import com.masrooraijaz.fireinsta.ui.main.MainActivity
import com.masrooraijaz.fireinsta.util.FirebaseStoragePaths
import kotlinx.android.synthetic.main.fragment_view_image.*
import kotlinx.android.synthetic.main.layout_post.view.*
import org.koin.android.ext.android.inject

private const val TAG = "ViewPostFragment"

class ViewPostFragment : BaseHomeFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()

        image_comments.setOnClickListener {
            findNavController().navigate(R.id.action_viewImageFragment_to_commentFragment)
        }

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

        })
    }
}