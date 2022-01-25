package com.example.chatapplication.view

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.chatapplication.R
import com.example.chatapplication.model.Constants
import com.example.chatapplication.model.CustomSharedPreference
import com.example.chatapplication.model.User
import com.example.chatapplication.model.UserAuthService
import com.example.chatapplication.utility.FirebaseService
import com.example.chatapplication.viewmodel.SharedViewModel
import com.example.chatapplication.viewmodel.SharedViewModelFactory
import com.example.chatapplication.viewmodel.UpdateProfileViewModel
import com.example.chatapplication.viewmodel.UpdateProfileViewModelFactory
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class UpdateProfile : Fragment() {
    private lateinit var updateDetails : Button
    private lateinit var updateName : EditText
    private lateinit var updateNumber : EditText
    private lateinit var updateImage : CircleImageView
    private lateinit var imageUri : Uri
    private lateinit var updateProfileViewModel: UpdateProfileViewModel
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.update_profile_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateProfileViewModel = ViewModelProvider(this, UpdateProfileViewModelFactory(
            FirebaseService()))[UpdateProfileViewModel::class.java]
        sharedViewModel = ViewModelProvider(requireActivity(), SharedViewModelFactory(
            UserAuthService()))[SharedViewModel::class.java]
        updateDetails = view.findViewById(R.id.updateUserDetails)
        updateName = view.findViewById(R.id.updateName)
        updateNumber = view.findViewById(R.id.updateNumber)
        updateImage = view.findViewById(R.id.updatePicture)
        FirebaseService().retrieveUserProfilePicture(requireContext(), updateImage)
        updateNumber.setText(CustomSharedPreference.get("UpdateNumber"))
        updateName.setText(CustomSharedPreference.get("UpdateName"))
        onImageViewClick()
        updateUserDetails()
    }

    private fun onImageViewClick() {
        updateImage.setOnClickListener {
            selectImage()
        }
    }

    private fun selectImage() {
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(intent, Constants.IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null
            && data.data != null) {
            imageUri = data.data!!
            updateImage.setImageURI(imageUri)
        }
    }

    private fun updateUserDetails() {
        updateDetails.setOnClickListener {
            val name = updateName.text.toString()
            val number = updateNumber.text.toString()
            FirebaseService().uploadUserProfilePicture(imageUri, requireContext())
            val updateProfile = User(name, number)
            updateProfileViewModel.updateUserProfile(updateProfile)
            updateProfileViewModel.updateUserProfileDetails.observe(viewLifecycleOwner, Observer {
                if (it.status) {
                    ToastMessages().displayMessage(requireContext(), "Profile Updated")
                    sharedViewModel.gotoHomePageStatus(true)
                }
            })
        }
    }
}