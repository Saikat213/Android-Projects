package com.example.fundooapp.viewmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fundooapp.R
import com.example.fundooapp.model.NotesData

class NoteAdapter(val dataSet: ArrayList<NotesData>) : RecyclerView.Adapter<NoteAdapter.RecyclerViewHolder>() {

    class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
       lateinit var displayDataTitle : TextView
       lateinit var displayDataContent : TextView
        init {
            displayDataTitle = itemView.findViewById(R.id.tvtitle)
            displayDataContent = itemView.findViewById(R.id.tvcontent)
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
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}