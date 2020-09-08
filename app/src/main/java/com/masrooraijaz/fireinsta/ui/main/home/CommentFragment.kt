package com.masrooraijaz.fireinsta.ui.main.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.masrooraijaz.fireinsta.R
import com.masrooraijaz.fireinsta.models.Comment
import com.masrooraijaz.fireinsta.models.Post
import com.masrooraijaz.fireinsta.ui.InteractionListener
import com.masrooraijaz.fireinsta.ui.main.MainActivity
import com.masrooraijaz.fireinsta.util.FireStoreUtil
import com.masrooraijaz.fireinsta.util.PAGE_SIZE
import com.masrooraijaz.fireinsta.util.PREFETCH_DISTANCE
import kotlinx.android.synthetic.main.fragment_comment.*
import kotlinx.android.synthetic.main.fragment_home.*


class CommentFragment : BaseHomeFragment(), InteractionListener<Comment> {


    lateinit var commentsAdapter: CommentsFirestorePagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()
    }

    fun subscribeObservers() {

        homeViewModel.selectedPost.observe(viewLifecycleOwner, Observer { post ->
            post?.postId?.let {
                initAdapter(it)
            }
        })
    }

    private fun initAdapter(postId: String) {

        val query = homeViewModel.getPostCommentsQuery(postId)

        val options = FirestorePagingOptions.Builder<Comment>()
            .setLifecycleOwner(viewLifecycleOwner)
            .setQuery(
                query,
                FireStoreUtil.getPagedListConfig(PAGE_SIZE, PREFETCH_DISTANCE),
                Comment::class.java
            )
            .build()


        commentsAdapter = CommentsFirestorePagingAdapter(
            options,
            statesListener,
            this
        )


        initRecyclerView()
    }

    private fun initRecyclerView() {
        rv_comments.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = commentsAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        }
    }

    override fun onClick(item: Comment) {
    }



}