package com.masrooraijaz.fireinsta.ui.main.home.search

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import com.google.android.material.tabs.TabLayoutMediator
import com.masrooraijaz.fireinsta.R
import com.masrooraijaz.fireinsta.ui.main.home.BaseHomeFragment
import kotlinx.android.synthetic.main.fragment_search.*


class SearchFragment : BaseHomeFragment() {
    lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        viewpager_search.adapter = SearchTabAdapter(this)

        TabLayoutMediator(tab_layout_search, viewpager_search) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.people)
                }
                1 -> {
                    tab.text = getString(R.string.photos_sentence_case)
                }
            }
        }.attach()
    }

    private fun initSearchView(menu: Menu) {
        activity?.apply {

            val searchManager: SearchManager =
                getSystemService(Context.SEARCH_SERVICE) as SearchManager
            searchView = menu.findItem(R.id.action_search).actionView as SearchView
            searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
            searchView.maxWidth = Integer.MAX_VALUE
            searchView.setIconifiedByDefault(false)
            searchView.isSubmitButtonEnabled = true
        }

        // ENTER ON COMPUTER KEYBOARD OR ARROW ON VIRTUAL KEYBOARD
        val searchPlate = searchView.findViewById(R.id.search_src_text) as EditText
        searchPlate.setOnEditorActionListener { v, actionId, event ->

            if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED
                || actionId == EditorInfo.IME_ACTION_SEARCH
            ) {
                val account = v.text.toString()
                homeViewModel.setSearchAccount(account)
            }
            true
        }

        // SEARCH BUTTON CLICKED (in toolbar)
        val searchButton = searchView.findViewById(R.id.search_go_btn) as View
        searchButton.setOnClickListener {
            val account = searchPlate.text.toString()
            homeViewModel.setSearchAccount(account)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu);
        initSearchView(menu)
    }


}