package com.example.chatapplication.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplication.R
import com.example.chatapplication.model.*
import com.example.chatapplication.model.Constants.TOPIC
import com.example.chatapplication.utility.FirebaseService
import com.example.chatapplication.utility.MessageAdapter
import com.example.chatapplication.viewmodel.ChatDetailsViewModel
import com.example.chatapplication.viewmodel.ChatDetailsViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class ChatDetailsFragment : Fragment() {
    private lateinit var displayUserNumber : TextView
    private lateinit var enterMessage : EditText
    private lateinit var sendMessage : ImageView
    private lateinit var singleChatRecyclerView: RecyclerView
    private lateinit var messageAdapter : MessageAdapter
    private lateinit var chatDetailsViewModel: ChatDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chatDetailsViewModel = ViewModelProvider(this, ChatDetailsViewModelFactory(
            FirebaseService()))[ChatDetailsViewModel::class.java]
        //FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)
        val context = requireContext()
        //UserAuthService().fetchFcmToken()
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
        chatBetweenUsers(context)
    }

    private fun chatBetweenUsers(context: Context) {
        sendMessage.setOnClickListener {
            val message = enterMessage.text.toString()
            if (message.isEmpty())
                ToastMessages().displayMessage(requireContext(), "Please Enter message")
            else {
                enterMessage.setText("")
                val date : Date = Date()
                val userMessage = UserMessages(message, FirebaseAuth.getInstance().currentUser!!.uid,
                    date.time)
                val notification = JSONObject()
                notification.put("title", FirebaseAuth.getInstance().currentUser!!.phoneNumber)
                notification.put("body", message)
                val msg = JSONObject()
                msg.put("to", Constants.tokenOfPixel4User)
                msg.put("notification", notification)
                UserAuthService().pushNotifications(context, msg.toString())
                chatDetailsViewModel.sendMessageToUser(userMessage)
                chatDetailsViewModel.uploadChatDetails.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                })
            }
        }
    }
}