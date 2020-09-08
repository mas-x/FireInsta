package com.masrooraijaz.fireinsta.ui.main

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.*
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.masrooraijaz.fireinsta.BaseActivity
import com.masrooraijaz.fireinsta.R
import com.masrooraijaz.fireinsta.ui.DataListener
import com.masrooraijaz.fireinsta.ui.main.home.ViewPostFragment
import com.masrooraijaz.fireinsta.util.DataOrException
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "MainActivity"

class MainActivity : BaseActivity(), DataListener, StatesListener,
    NavController.OnDestinationChangedListener {

    private lateinit var bottomNavigationView: BottomNavigationView
    lateinit var appBarConfiguration: AppBarConfiguration

    override fun showProgressbar(bool: Boolean) {
        if (bool)
            progress_bar.visibility = View.VISIBLE
        else
            progress_bar.visibility = View.GONE

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(main_toolbar)


        bottomNavigationView = findViewById(R.id.bottom_navigation_view)
        NavigationUI.setupWithNavController(
            bottomNavigationView,
            findNavController(R.id.main_nav_host_fragment)
        )

        appBarConfiguration =
            AppBarConfiguration(setOf(R.id.homeFragment, R.id.uploadFragment, R.id.profileFragment))

        setupActionBarWithNavController(
            findNavController(R.id.main_nav_host_fragment),
            appBarConfiguration
        )

        findNavController(R.id.main_nav_host_fragment).addOnDestinationChangedListener(this)
    }

    override fun handleDataChange(dataOrException: DataOrException<*, *>) {
        showProgressbar(dataOrException.loading)
        dataOrException.exception?.let {
            handleException(it)
        }
    }


    override fun onLoad() {
        showProgressbar(true)
    }

    override fun onLoadingMore() {
        // do nothing
    }

    override fun onLoaded() {
        showProgressbar(false)
    }

    override fun onFinished() {
        //Dont know yet
    }

    override fun onError() {
        Toast.makeText(this, "Error while loading data...", Toast.LENGTH_SHORT).show()
    }


    fun showBottomNavigation(bool: Boolean) {
        if (bool)
            bottomNavigationView.visibility = View.VISIBLE
        else
            bottomNavigationView.visibility = View.GONE
    }

    fun showToolbar(bool: Boolean) {
        if (bool)
            main_toolbar.visibility = View.VISIBLE
        else
            main_toolbar.visibility = View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(
            Navigation.findNavController(this, R.id.main_nav_host_fragment),
            appBarConfiguration
        )


    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        when (destination.id) {
            R.id.viewImageFragment -> {
                showBottomNavigation(false)
                showToolbar(false)
            }
            R.id.commentFragment -> {
                showBottomNavigation(false)
                showToolbar(true)
            }

            else -> {
                showBottomNavigation(true)
                showToolbar(true)
            }
        }

    }


}