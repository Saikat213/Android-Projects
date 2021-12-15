package com.example.fundooapp.viewmodel

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.fundooapp.CreateNotesFragment
import com.example.fundooapp.HomeFragment
import com.example.fundooapp.R
import com.example.fundooapp.UpdateFragment
import com.example.fundooapp.model.NotesData

class NoteAdapter(val dataSet: ArrayList<NotesData>) : RecyclerView.Adapter<NoteAdapter.RecyclerViewHolder>() {
    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var displayDataTitle : TextView
        var displayDataContent : TextView
        var imgButton : ImageView

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
        var Notes : NotesData = dataSet[position]
        holder.imgButton.setOnClickListener {v ->
            val popupMenu = PopupMenu(v.context, holder.imgButton)
            popupMenu.menuInflater.inflate(R.menu.notes_options_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener {
                when(it.itemId) {
                    R.id.delete -> {
                        dataSet.removeAt(position)
                        HomeFragment().deleteNotes(Notes?.ID!!)
                    }

                    R.id.edit -> {
                        val bundle = Bundle()
                        bundle.putString("Title", Notes.Title)
                        bundle.putString("Content", Notes.Content)
                        bundle.putString("ID", Notes.ID)
                        val updateFragment = UpdateFragment()
                        bundle.putSerializable("NOTE_KEY", Notes)
                        val activity = v.context as AppCompatActivity
                        updateFragment.arguments = bundle
                        activity.supportFragmentManager.beginTransaction().apply {
                            replace(R.id.fragmentContainer, updateFragment)
                            addToBackStack(null)
                            commit()
                        }
                    }

                    R.id.archive -> {
                        dataSet.removeAt(position)
                        dataSet[position].Archive = true

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