package com.example.chatapplication.view

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.chatapplication.R
import com.example.chatapplication.model.Constants
import com.example.chatapplication.model.User
import com.example.chatapplication.model.UserAuthService
import com.example.chatapplication.utility.FirebaseService
import com.example.chatapplication.viewmodel.RegisterUserViewModel
import com.example.chatapplication.viewmodel.RegisterUserViewModelFactory
import com.example.chatapplication.viewmodel.SharedViewModel
import com.example.chatapplication.viewmodel.SharedViewModelFactory
import de.hdodenhof.circleimageview.CircleImageView

class RegisterUserFragment : Fragment() {
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var profileImage : CircleImageView
    private lateinit var username : EditText
    private lateinit var userNumber : EditText
    private lateinit var registerUser : Button
    private lateinit var imageUri : Uri
    private lateinit var registerUserViewModel: RegisterUserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity(), SharedViewModelFactory(
            UserAuthService()))[SharedViewModel::class.java]
        registerUserViewModel = ViewModelProvider(this, RegisterUserViewModelFactory(
            FirebaseService()))[RegisterUserViewModel::class.java]
        profileImage = view.findViewById(R.id.registerPicture)
        username = view.findViewById(R.id.userName)
        userNumber = view.findViewById(R.id.userNumber)
        registerUser = view.findViewById(R.id.register)
        onImageClick()
        registerUserToFirebase()
    }

    private fun registerUserToFirebase() {
        registerUser.setOnClickListener {
            val name = username.text.toString()
            val number = userNumber.text.toString()
            val image = FirebaseService().uploadUserProfilePicture(imageUri, requireContext())
            val userDetails = User(name, number, "+91", image)
            registerUserViewModel.addUserDetails(userDetails)
            registerUserViewModel.registerUserDetailsToFirebase.observe(viewLifecycleOwner, Observer {
                if (it.status)
                    sharedViewModel.gotoHomePageStatus(true)
            })
        }
    }

    private fun onImageClick() {
        profileImage.setOnClickListener {
            chooseImageFile()
        }
    }

    fun chooseImageFile() {
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(intent, Constants.IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.IMAGE_REQUEST && resultCode == RESULT_OK && data != null
            && data.data != null) {
            imageUri = data.data!!
            profileImage.setImageURI(imageUri)
        }
    }
}