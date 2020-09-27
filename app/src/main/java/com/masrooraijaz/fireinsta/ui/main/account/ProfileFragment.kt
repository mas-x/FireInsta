package com.masrooraijaz.fireinsta.ui.main.account

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.masrooraijaz.fireinsta.R
import com.masrooraijaz.fireinsta.models.Post
import com.masrooraijaz.fireinsta.models.User
import com.masrooraijaz.fireinsta.ui.FragmentItemsListener
import com.masrooraijaz.fireinsta.ui.InteractionListener
import com.masrooraijaz.fireinsta.ui.auth.AuthActivity
import com.masrooraijaz.fireinsta.util.*
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.fragment_profile.*

private const val TAG = "ProfileFragment"

class ProfileFragment() : BaseAccountFragment(), InteractionListener<Post>, FragmentItemsListener {

    val args: ProfileFragmentArgs by navArgs()


    private lateinit var profileAdapter: ProfileFirestorePagingAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        args.user?.let { user ->
            user.id?.let { it -> accountViewModel.setCurrentAccountId(it) }
        } ?: accountViewModel.setCurrentUserAccountId()

        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeObservers()

        btn_follow.setOnClickListener {
            accountViewModel.followUser().observe(viewLifecycleOwner, Observer {
                dataListener.handleDataChange(it)
                it.data?.let { msg ->
                    if (msg.msg == SuccessHandling.FOLLOW_SUCCESS) {
                        Toast.makeText(requireContext(), "Followed!", Toast.LENGTH_SHORT).show()
                        btn_follow.visibility = View.GONE
                        btn_unfollow.visibility = View.VISIBLE
                    }
                }
            })
        }


        btn_unfollow.setOnClickListener {
            accountViewModel.unFollowUser().observe(viewLifecycleOwner, Observer {
                dataListener.handleDataChange(it)
                it.data?.let { msg ->
                    if (msg.msg == SuccessHandling.UNFOLLOW_SUCCESS) {
                        Toast.makeText(requireContext(), "Unfollowed!", Toast.LENGTH_SHORT).show()
                        btn_follow.visibility = View.VISIBLE
                        btn_unfollow.visibility = View.GONE
                    }
                }
            })
        }


    }

    fun initAdapter(userId: String) {
        val query = accountViewModel.getUserImagesQuery(userId)

        val options = FirestorePagingOptions.Builder<Post>()
            .setLifecycleOwner(viewLifecycleOwner)
            .setQuery(
                query,
                FireStoreUtil.getPagedListConfig(PAGE_SIZE, PREFETCH_DISTANCE),
                Post::class.java
            )
            .build()

        profileAdapter = ProfileFirestorePagingAdapter(
            options,
            accountViewModel.getStorageReference(userId),
            requestManager,
            this,
            this
        )

        initRecyclerView()
    }

    private fun initRecyclerView() {
        rv_profile_pics.apply {
            adapter = profileAdapter
            layoutManager =
                GridLayoutManager(
                    requireContext(),
                    LayoutUtil.calculateNoOfColumns(
                        requireContext(),
                        resources.getDimension(R.dimen.profile_column_width)
                    )
                )
        }

    }


    private fun subscribeObservers() {
        //atm only current user

        accountViewModel.currentAccountId.observe(viewLifecycleOwner, Observer {
            accountViewModel.getAccount(it)
            initAdapter(it)

            if (accountViewModel.getLoggedInUserId() == it) {
                //hide follow btn
                btn_follow.visibility = View.GONE

                //enable profile picture updation

                image_profile_picture.setOnClickListener {
                    updateProfilePicture()
                }

                if (accountViewModel.getLoggedInUserId() == it)
                    showLogOutMenu()
            }


        })

        accountViewModel.currentAccount
            .observe(viewLifecycleOwner, Observer { dataOrException ->

                dataListener.handleDataChange(dataOrException)



                dataOrException.data?.let { user ->

                    user.followersRef?.let { followersRef ->
                        accountViewModel.getLoggedInUserId()?.let {
                            if (followersRef.contains(accountViewModel.getUserRef(it))) {
                                //user is following
                                btn_follow.visibility = View.GONE
                                btn_unfollow.visibility = View.VISIBLE
                            }
                        }

                    }

                    requestManager.load(
                        accountViewModel.getUserProfilePicStorageRef()
                    )
                        .placeholder(R.drawable.ic_baseline_account_circle_24)
                        .error(R.drawable.ic_baseline_account_circle_24)
                        .into(image_profile_picture)
                    setAccountProperties(user)
                }
            })

        accountViewModel.selectedImageUri.observe(viewLifecycleOwner, Observer
        {
            it.let { uri ->
                requestManager.load(uri).placeholder(R.drawable.ic_baseline_account_circle_24)
                    .error(R.drawable.ic_baseline_account_circle_24).into(
                        image_profile_picture
                    )
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        if (accountViewModel.currentAccountId.value == accountViewModel.getLoggedInUserId()) {
            inflater.inflate(R.menu.account_menu, menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.nav_logout) {
            accountViewModel.logOut()
            navToAuthActivity()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun navToAuthActivity() {

        activity?.run {
            startActivity(Intent(activity, AuthActivity::class.java))
            finish()
        }

    }

    private fun showLogOutMenu() {


        activity?.run {
            invalidateOptionsMenu()
        }
    }

    private fun setAccountProperties(user: User) {
        text_display_name.text = user.displayName
        if (user.followingRef != null) {
            text_following.text = user.followingRef?.count().toString()
        } else {
            text_following.text = "0"
        }
        if (user.followersRef != null) {
            text_followers.text = user.followersRef?.count().toString()
        } else {
            text_followers.text = "0"
        }

        text_posts.text = user.posts.toString()
    }

    override fun onClick(item: Post) {
        val action = ProfileFragmentDirections.actionProfileFragmentToViewImageFragment(item)
        findNavController().navigate(action)
    }

    fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png", "image/jpg")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        startActivityForResult(intent, PICK_PHOTO_REQUEST_CODE)
    }

    fun updateProfilePicture() {
        if (dataListener.isStoragePermissionGranted())
            pickImage()
    }

    private fun launchImageCrop(uri: Uri?) {
        context?.let {
            CropImage.activity(uri).setGuidelines(CropImageView.Guidelines.ON)
                .start(it, this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_PHOTO_REQUEST_CODE -> {
                    data?.data?.let {
                        launchImageCrop(it)
                    } ?: Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
                }

                CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                    val result = CropImage.getActivityResult(data)
                    val resultUri = result.uri
                    accountViewModel.selectedImageUri.value = resultUri
                    uploadProfilePicture(resultUri)
                }

                CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE -> {
                    Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    private fun uploadProfilePicture(uri: Uri) {
        accountViewModel.updateProfilePicture(uri).observe(viewLifecycleOwner, Observer {
            dataListener.handleDataChange(it)
            if (it.data?.msg == SuccessHandling.DISPLAY_PIC_UPDATED) {
                Toast.makeText(requireContext(), "Profile picture updated.", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }


    override fun onDataChanged(itemCount: Int) {
        if (itemCount == 0)
            text_no_items.visibility = View.VISIBLE
        else
            text_no_items.visibility = View.GONE
    }


}