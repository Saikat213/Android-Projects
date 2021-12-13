package com.example.fundooapp.viewmodel

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.fundooapp.CreateNotesFragment
import com.example.fundooapp.HomeFragment
import com.example.fundooapp.R
import com.example.fundooapp.model.NotesData
import com.example.fundooapp.model.UserAuthService

class NoteAdapter(val dataSet: ArrayList<NotesData>) : RecyclerView.Adapter<NoteAdapter.RecyclerViewHolder>() {

    class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
       lateinit var displayDataTitle : TextView
       lateinit var displayDataContent : TextView
       lateinit var imgButton : ImageView

        init {
            displayDataTitle = itemView.findViewById(R.id.tvtitle)
            displayDataContent = itemView.findViewById(R.id.tvcontent)
            imgButton = itemView.findViewById(R.id.notesMenu)
        }

        fun bindData(userData : NotesData) {
            displayDataTitle.text = userData.Title
            displayDataContent.text = userData.Content
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_layout, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.bindData(dataSet[position])
        var test : NotesData = dataSet[position]
        holder.imgButton.setOnClickListener {
            val popupMenu = PopupMenu(it.context, holder.imgButton)
            popupMenu.menuInflater.inflate(R.menu.notes_options_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener {
                when(it.itemId) {
                    R.id.delete -> {
                        dataSet.removeAt(position)
                        HomeFragment().deleteNotes(test?.ID!!)
                    }
                    R.id.edit -> {
                        holder.bindData(test)
                        CreateNotesFragment().updateNotes(test)
                    }
                    R.id.archive -> {
                        dataSet.removeAt(position)
                    }
                }
                notifyDataSetChanged()
                true
            })
            popupMenu.show()
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}