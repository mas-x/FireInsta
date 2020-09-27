package com.masrooraijaz.fireinsta.ui.main.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.masrooraijaz.fireinsta.R
import com.masrooraijaz.fireinsta.models.Comment
import com.masrooraijaz.fireinsta.ui.InteractionListener
import com.masrooraijaz.fireinsta.util.FireStoreUtil
import com.masrooraijaz.fireinsta.util.PAGE_SIZE
import com.masrooraijaz.fireinsta.util.PREFETCH_DISTANCE
import kotlinx.android.synthetic.main.fragment_comment.*


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

        btn_post_comment.setOnClickListener {
            postComment()
        }

        edit_text_comment.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                btn_post_comment.isEnabled = editable?.isNotEmpty() ?: false
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, count: Int) {
            }

        })
    }

    private fun subscribeObservers() {

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
            this,
            homeViewModel.getStorageReference(),
            requestManager
        )


        initRecyclerView()
    }


    private fun postComment() {
        homeViewModel.postComment(
            edit_text_comment.text.toString().trim()
        ).observe(viewLifecycleOwner, Observer { dataOrException ->

            dataListener.handleDataChange(dataOrException)

            dataOrException.data?.let { message ->
                Toast.makeText(requireContext(), message.msg, Toast.LENGTH_SHORT).show()
                commentsAdapter.refresh()
            }

            edit_text_comment.setText("")


        })
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