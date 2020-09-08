package com.masrooraijaz.fireinsta.ui.main.upload

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.RequestManager
import com.masrooraijaz.fireinsta.R
import com.masrooraijaz.fireinsta.ui.DataListener
import com.masrooraijaz.fireinsta.util.SuccessHandling
import kotlinx.android.synthetic.main.fragment_upload_picture.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.ClassCastException

private const val TAG = "UploadFragment"

class UploadFragment : Fragment() {


    val PICK_PHOTO = 11

    private val requestManager by inject<RequestManager>()

    private val viewModel by viewModel<UploadViewModel>()

    private lateinit var dataListener: DataListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_upload_picture, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pickImage()
        btn_upload.setOnClickListener {
            uploadFile()
        }
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


    fun pickImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_PHOTO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_PHOTO && resultCode == Activity.RESULT_OK) {

            data?.let {
                viewModel.selectedImageUri.value = it.data

            }


        } else {
            Log.d(TAG, "onActivityResult: Did not select any photo...")
        }
    }


}