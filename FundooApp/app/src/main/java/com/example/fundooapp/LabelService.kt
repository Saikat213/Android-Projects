package com.example.fundooapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.ExpandableListView
import android.widget.Toast
import com.example.fundooapp.model.Label
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*

class LabelService {
    private var firebaseUser: FirebaseUser
    private var firebaseAuth: FirebaseAuth
    private var firestore: FirebaseFirestore
    private var documentLabelReference: CollectionReference

    init {
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        firebaseUser = firebaseAuth.currentUser!!
        documentLabelReference =
            firestore.collection("Users").document(firebaseUser.uid).collection("Label")
    }

    fun addLabelList(label: String) {
        val labelNote = mutableMapOf<String, String>()
        labelNote["label"] = label
        documentLabelReference.document().set(labelNote)
    }

    fun retrieveLabelCollection(
        context: Context,
        expandableListView: ExpandableListView
    ) {
        val labelList: MutableList<String> = mutableListOf()
        val labelListClass: MutableList<Label> = mutableListOf()
        documentLabelReference.orderBy("label", Query.Direction.ASCENDING)
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ) {
                    if (error != null) {
                        Toast.makeText(
                            context,
                            "Cannot Retrieve Data ${error.message.toString()}",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.e(ContentValues.TAG, error.localizedMessage!!.toString());
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            val data = dc.document.toObject(Label::class.java)
                            data.labelID = dc.document.id
                            labelList.add(dc.document.getString("label").toString())
                        }
                    }
                    val labelName: MutableList<String> = mutableListOf()
                    val labelHashMap: HashMap<String, MutableList<String>> = hashMapOf()
                    labelName.add("Label")
                    Log.d("check", "${labelListClass.size}")
                    labelHashMap[labelName[0]] = labelList
                    val expandListAdapter =
                        LabelExpandableListAdapter(context, labelName, labelHashMap, expandableListView)
                    expandableListView.setAdapter(expandListAdapter)
                }
            })
    }

    fun updateLabelCollection(id: String, value: String) {
        documentLabelReference.document(id).update("label", value)
    }

    fun deleteLabelCollection(id: String) {
        documentLabelReference.document(id).delete()
    }
}