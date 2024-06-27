package com.test.test.presentation.input

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.test.test.R
import com.test.test.data.db.DB
import com.test.test.databinding.FragmentInputBinding
import com.test.test.main.LoaderViewModel
import java.io.File

/**
 * Permite la captura de la foto del usuario al dar click en la imagen de
 * camara.
 */
open class PhotoFragment : Fragment()
{
    protected lateinit var viewModel: InputViewModel
    protected var loaderVm : LoaderViewModel? = null
    protected lateinit var binding: FragmentInputBinding
    private var photoStored  = false

    private val takePhoto =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success == true)
            {
                photoStored = true
                Glide.with(requireContext()).load(viewModel.delegate.tempPhotoUri)
                    .signature(ObjectKey(System.currentTimeMillis().hashCode()))
                    .into(binding.photo)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentInputBinding.inflate(inflater).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        loaderVm = ViewModelProvider(requireActivity())[LoaderViewModel::class.java]
        val db: DB
        val inputFactory = InputViewModelFactory(
            DB.getInstance(requireContext()).also { db = it }.userDAO(), db.stateDAO()
        )
        viewModel = ViewModelProvider(this, inputFactory)[InputViewModel::class.java]
        viewModel.img.observe(viewLifecycleOwner, Observer {
            it?:return@Observer
            Glide.with(requireContext()).load(it)
                .signature(ObjectKey(System.currentTimeMillis().hashCode()))
                .into(binding.photo)
            loaderVm?.hideLoader()
        })
        setupPhoto()
    }

    private fun setupPhoto()
    {
        val file = File(requireContext().filesDir.toString() + "/images")
        file.mkdirs()
        val fileTempPhoto = File("$file/temp_image.jpg")
        viewModel.delegate.tempPhotoUri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.FileProvider",
            fileTempPhoto
        )

        binding.photo.setOnClickListener {
            takePhoto.launch(viewModel.delegate.tempPhotoUri)
        }
    }

    protected fun savePhoto( success: (String) -> Unit)
    {
        if(!photoStored) { loaderVm?.hideLoader() ;
            viewModel.onError(getString(R.string.saveWithoutPhoto)); return
        }
        requireContext().contentResolver.openInputStream(viewModel.delegate.tempPhotoUri!!)?.let {
              viewModel.uploadPhoto(it, success)
        }
    }

    protected fun fetchImage(photoName: String) = viewModel.fetchImage(photoName)
}