package com.example.chatapplication.utility

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplication.R
import com.example.chatapplication.model.CustomSharedPreference
import com.example.chatapplication.model.User
import com.example.chatapplication.view.ChatDetailsFragment
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter(var users : ArrayList<User>, val context : Context) :
    RecyclerView.Adapter<UserAdapter.UsersViewHolder>() {

    inner class UsersViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        private var userNumber : TextView
        private var lastMessage : TextView
        private var profilePicture : CircleImageView

        init {
            userNumber = itemView.findViewById(R.id.userPhnNumber)
            lastMessage = itemView.findViewById(R.id.lastmessage)
            profilePicture = itemView.findViewById(R.id.profilePicture)
        }

        fun bindData(userData : User) {
            userNumber.text = userData.PhoneNumber
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_layout, parent, false)
        return UsersViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.bindData(users[position])
        holder.itemView.setOnClickListener {
            CustomSharedPreference.initSharedPreference(context)
            CustomSharedPreference.addString("ChatWith", users[position].PhoneNumber)
            val activity = it.context as AppCompatActivity
            activity.supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainer, ChatDetailsFragment())
                commit()
            }
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }
}