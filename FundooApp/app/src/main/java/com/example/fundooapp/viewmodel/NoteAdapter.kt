package com.example.fundooapp.viewmodel

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.fundooapp.R
import com.example.fundooapp.UpdateFragment
import com.example.fundooapp.model.NotesData
import com.example.fundooapp.model.NotesServiceImpl
import java.util.*
import kotlin.collections.ArrayList

class NoteAdapter(var dataSet: ArrayList<NotesData>, val context: Context) : RecyclerView.Adapter<NoteAdapter.RecyclerViewHolder>(), Filterable {
    val originalList : ArrayList<NotesData> = dataSet
    var filteredList = ArrayList<NotesData>()

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
        var notes : NotesData = dataSet[position]
        holder.bindData(notes)
        holder.imgButton.setOnClickListener {v ->
            val popupMenu = PopupMenu(v.context, holder.imgButton)
            popupMenu.menuInflater.inflate(R.menu.notes_options_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener {
                when(it.itemId) {
                    R.id.delete -> {
                        dataSet.removeAt(position)
                        Log.d("ID-->${notes.ID}", "Title--->${notes.Title}")
                        NotesServiceImpl().deleteNotes(notes.ID, notes?.Title!!, context)
                    }

                    R.id.edit -> {
                        val bundle = Bundle()
                        bundle.putString("Title", notes.Title)
                        bundle.putString("Content", notes.Content)
                        bundle.putString("ID", notes.ID)
                        val updateFragment = UpdateFragment()
                        bundle.putSerializable("NOTE_KEY", notes)
                        val activity = v.context as AppCompatActivity
                        updateFragment.arguments = bundle
                        activity.supportFragmentManager.beginTransaction().apply {
                            replace(R.id.fragmentContainer, updateFragment)
                            addToBackStack(null)
                            commit()
                        }
                    }

                    R.id.archive -> {
                        dataSet[position].Archive = "true"
                        dataSet.removeAt(position)
                        UpdateFragment().updateArchiveField(notes.ID, notes.Title, context)
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

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(text: CharSequence?): FilterResults {
                val charSearch = text.toString()
                if (charSearch.isEmpty()) {
                    filteredList.clear()
                    filteredList = originalList
                }
                else {
                    val resultList = ArrayList<NotesData>()
                    for (data in dataSet) {
                        if (data.Title!!.lowercase(Locale.getDefault()).contains(charSearch.lowercase(Locale.getDefault())))
                            resultList.add(data)
                    }
                    filteredList = resultList
                }
                val filterResult = FilterResults()
                filterResult.values = filteredList
                return filterResult
            }

            override fun publishResults(text: CharSequence?, results: FilterResults?) {
                filteredList = results!!.values as ArrayList<NotesData>
                Log.d("FilteredList Size: ${filteredList.size}", "$filteredList")
                dataSet = filteredList
                Log.d("UpdatedList Size: ${dataSet.size}", "$dataSet")
            }
        }
    }
}