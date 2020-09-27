package com.masrooraijaz.fireinsta.ui.main.home

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.bumptech.glide.RequestManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.masrooraijaz.fireinsta.R
import com.masrooraijaz.fireinsta.ui.DataListener
import com.masrooraijaz.fireinsta.util.SuccessHandling
import kotlinx.android.synthetic.main.fragment_upload_picture.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

private const val TAG = "UploadFragment"
const val UPLOAD_FRAGMENT_IMAGE_KEY = "ImageUri"

const val UPLOAD_FRAGMENT_TAG = "UploadFragment"


class UploadFragment
    : BottomSheetDialogFragment() {


    private val requestManager by inject<RequestManager>()
    private val viewModel by sharedViewModel<HomeViewModel>()

    private lateinit var dataListener: DataListener


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_upload_picture, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.upload_menu, menu);
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.nav_post_photo) {
            uploadFile()
        }
        return super.onOptionsItemSelected(item)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        text_caption.addTextChangedListener {
            text_caption.error = null
        }

        arguments?.let {
            viewModel.selectedImageUri.value = it.getParcelable(UPLOAD_FRAGMENT_IMAGE_KEY)
        }

        btn_upload.setOnClickListener { uploadFile() }



        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.selectedImageUri.observe(viewLifecycleOwner, Observer {
            requestManager.load(
                it
            ).into(image_preview)
        })
    }

    private fun uploadFile() {

        if (text_caption.text.isNullOrBlank()) {
            text_caption.error = "Caption is required"
            return;
        }
        viewModel.selectedImageUri.value?.let { uri ->
            viewModel.uploadFile(
                text_caption.text.toString(), uri
            ).observe(viewLifecycleOwner, Observer {
                dataListener.handleDataChange(it)

                if (it.data?.msg == SuccessHandling.FILE_UPLOAD_SUCCESS) {
                    Log.d(TAG, "uploadFile: File Uploaded Successfully")
                }

            })
        }
        dismiss()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            dataListener = context as DataListener
        } catch (exception: ClassCastException) {
            exception.printStackTrace()
            Log.d(
                TAG,
                "onAttach: Invalid activity... Does not implement Data Listener"
            )
        }
    }

    companion object {
        fun newInstance(image: Uri): UploadFragment {
            val args = Bundle()
            args.putParcelable(UPLOAD_FRAGMENT_IMAGE_KEY, image)
            val fragment =
                UploadFragment()
            fragment.arguments = args
            return fragment
        }
    }


}