package com.example.fundooapp

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.isVisible

class LabelExpandableListAdapter(var context: Context, var labelName : MutableList<String>,
            var labelList : HashMap<String, MutableList<String>>,
            var expandableListView : ExpandableListView) : BaseExpandableListAdapter() {
    override fun getGroupCount(): Int {
        return labelName.size
    }

    override fun getChildrenCount(position: Int): Int {
        return labelList.get(labelName.get(position))!!.size
    }

    override fun getGroup(position: Int): Any {
        return labelName.get(position)
    }

    override fun getChild(grpPosition: Int, childPosition: Int): Any {
        return labelList.get(labelName.get(grpPosition))!!.get(childPosition)
    }

    override fun getGroupId(position: Int): Long {
        return position.toLong()
    }

    override fun getChildId(grpPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(grpPosition: Int, isExpand: Boolean, view: View?, parentView: ViewGroup?): View {
        var changeView = view
        if (changeView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            changeView = inflater.inflate(R.layout.group_item, null)
        }
        val title = changeView?.findViewById<TextView>(R.id.labelText)!!
        val addLabelBtn = changeView?.findViewById<ImageButton>(R.id.addLabel)!!
        addLabelBtn.isVisible = true
        title.text = getGroup(grpPosition) as CharSequence?
        title.setOnClickListener {
            if (expandableListView.isGroupExpanded(grpPosition))
                expandableListView.collapseGroup(grpPosition)
            else
                expandableListView.expandGroup(grpPosition)
        }
        addLabelBtn.setOnClickListener {
            addLabel()
        }
        return changeView
    }

    override fun getChildView(grpPosition: Int, childPosition: Int, isLastChild: Boolean, view: View?, parentView: ViewGroup?): View {
        var changeView = view
        if (changeView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            changeView = inflater.inflate(R.layout.expand_label, null)
        }
        val title = changeView?.findViewById<TextView>(R.id.expandLabel)!!
        title.text = getChild(grpPosition, childPosition).toString()
        return changeView
    }

    override fun isChildSelectable(p0: Int, p1: Int): Boolean {
        return true
    }

    private fun addLabel() {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.add_label)
        val labelName = dialog.findViewById<TextView>(R.id.labelAddedByUser)
        val addBtn = dialog.findViewById<ImageButton>(R.id.createLabel)
        val closeBtn = dialog.findViewById<ImageButton>(R.id.closeLabel)
        addBtn.setOnClickListener {
            val labelFromUser = labelName.text.toString()
            val labelServices = LabelService()
            labelServices.addLabelList(labelFromUser)
            HomeFragment().showList(context)
            dialog.dismiss()
        }
        closeBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.FILL_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}