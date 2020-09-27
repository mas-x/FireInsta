package com.masrooraijaz.fireinsta.ui.main.home.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.masrooraijaz.fireinsta.R
import com.masrooraijaz.fireinsta.models.User
import com.masrooraijaz.fireinsta.ui.InteractionListener
import com.masrooraijaz.fireinsta.ui.main.home.BaseHomeFragment
import com.masrooraijaz.fireinsta.util.FireStoreUtil
import com.masrooraijaz.fireinsta.util.PAGE_SIZE
import com.masrooraijaz.fireinsta.util.PREFETCH_DISTANCE
import kotlinx.android.synthetic.main.fragment_search_text.*

private const val TAG = "SearchAccountFragment"

class SearchAccountFragment : BaseHomeFragment(), InteractionListener<User> {


    //todo -> separate search from home view model

    lateinit var searchAccountAdapter: SearchAccountsFirestorePagingAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_text, container, false)
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

    fun initAdapter(account: String) {

        val query = homeViewModel.getSearchUserAccountsQuery(account)

        val options = FirestorePagingOptions.Builder<User>()
            .setLifecycleOwner(viewLifecycleOwner)
            .setQuery(
                query,
                FireStoreUtil.getPagedListConfig(PAGE_SIZE, PREFETCH_DISTANCE),
                User::class.java
            )
            .build()


        searchAccountAdapter = SearchAccountsFirestorePagingAdapter(
            options,
            statesListener,
            requestManager,
            homeViewModel.getStorageReference(),
            this
        )


        initRecyclerView()
    }

    private fun initRecyclerView() {

        rv_search_accounts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = searchAccountAdapter
        }
    }

    override fun onClick(item: User) {
        val action = SearchFragmentDirections.actionSearchFragmentToProfileFragment(item)
        findNavController().navigate(action)
    }


}