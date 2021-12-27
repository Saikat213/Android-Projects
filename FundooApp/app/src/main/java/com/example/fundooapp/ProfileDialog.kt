package com.example.fundooapp

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class ProfileDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Profile").setPositiveButton("SignOut", DialogInterface.OnClickListener { dialog, id ->

        }).setNegativeButton("Change Profile Picture", DialogInterface.OnClickListener { dialog, id ->

        })
        builder.create().show()
        return super.onCreateDialog(savedInstanceState)
    }
}