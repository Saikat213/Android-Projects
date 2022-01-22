package com.example.chatapplication.utility

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplication.R
import com.example.chatapplication.model.Constants
import com.example.chatapplication.model.UserMessages
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(var messages : ArrayList<UserMessages>, var context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class SenderViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val senderMessage : TextView
        init {
            senderMessage = itemView.findViewById(R.id.senderMessage)
        }
    }

    inner class ReceiverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receiverMessage : TextView
        init {
            receiverMessage = itemView.findViewById(R.id.receiverMessage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == Constants.ITEM_SENT) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.sender_chat_layout, parent, false)
            return SenderViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.receiver_chat_layout, parent, false)
            return ReceiverViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val userMsg = messages.get(position)
        if (holder.javaClass == SenderViewHolder::class.java) {
            val viewHolder = holder as SenderViewHolder
            viewHolder.senderMessage.text = userMsg.message
        } else {
            val viewHolder = holder as ReceiverViewHolder
            viewHolder.receiverMessage.text = userMsg.message
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun getItemViewType(position: Int): Int {
        val messagePosition = messages.get(position)
        if (FirebaseAuth.getInstance().currentUser!!.uid.equals(messagePosition.senderID))
            return Constants.ITEM_SENT
        else
            return Constants.ITEM_RECEIVE
    }
}