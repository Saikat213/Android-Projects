package com.example.fundooapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fundooapp.model.Label

class LabelAdapter(var labelList: ArrayList<Label>,val context: Context) :
        RecyclerView.Adapter<LabelAdapter.LabelViewHolder>() {

    class LabelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabelViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_label, parent, false)
        return LabelViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LabelViewHolder, position: Int) {
        val label: Label = labelList[position]
        val labelName = label.name
        if(labelName != null){
            assignValues(holder,label)
        }
    }

    override fun getItemCount(): Int {
        return labelList.size
    }

    private fun assignValues(holder: LabelViewHolder, label: Label) {
    }
}