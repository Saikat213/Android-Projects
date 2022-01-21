package com.example.chatapplication.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.chatapplication.R
import com.example.chatapplication.model.CustomSharedPreference

class ChatDetailsFragment : Fragment() {
    private lateinit var displayUserNumber : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayUserNumber = view.findViewById(R.id.userPhn)
        displayUserNumber.text = CustomSharedPreference.get("ChatWith")
    }
}