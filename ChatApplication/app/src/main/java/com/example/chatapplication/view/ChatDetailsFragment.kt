package com.example.chatapplication.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplication.R
import com.example.chatapplication.model.CustomSharedPreference
import com.example.chatapplication.model.UserMessages
import com.example.chatapplication.utility.FirebaseService
import com.example.chatapplication.utility.MessageAdapter
import com.google.firebase.auth.FirebaseAuth
import java.util.*
import kotlin.collections.ArrayList

class ChatDetailsFragment : Fragment() {
    private lateinit var displayUserNumber : TextView
    private lateinit var enterMessage : EditText
    private lateinit var sendMessage : ImageView
    private lateinit var singleChatRecyclerView: RecyclerView
    private lateinit var messageAdapter : MessageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = requireContext()
        displayUserNumber = view.findViewById(R.id.userPhn)
        displayUserNumber.text = CustomSharedPreference.get("ChatWith")
        enterMessage = view.findViewById(R.id.typeMessage)
        sendMessage = view.findViewById(R.id.send)
        singleChatRecyclerView = view.findViewById(R.id.singleChatRecyclerView)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        layoutManager.stackFromEnd = true
        singleChatRecyclerView.layoutManager = layoutManager
        messageAdapter = MessageAdapter(ArrayList<UserMessages>(), context)
        singleChatRecyclerView.adapter = messageAdapter
        FirebaseService().retrieveUserChats(context, singleChatRecyclerView)
        sendMessageToUser()
    }

    private fun sendMessageToUser() {
        sendMessage.setOnClickListener {
            val message = enterMessage.text.toString()
            if (message.isEmpty())
                ToastMessages().displayMessage(requireContext(), "Please Enter message")
            else {
                enterMessage.setText("")
                val date : Date = Date()
                val userMessage = UserMessages(message, FirebaseAuth.getInstance().currentUser!!.uid, date.time)
                FirebaseService().uploadUserChats(userMessage)
            }
        }
    }
}