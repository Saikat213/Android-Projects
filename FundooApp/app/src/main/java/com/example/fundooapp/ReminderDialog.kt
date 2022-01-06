package com.example.fundooapp

import android.app.*
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.work.*
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*
import java.util.concurrent.TimeUnit

class ReminderDialog : DialogFragment() {
    lateinit var timeBtn: Button
    lateinit var dateBtn: Button

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bundle : Bundle = this.requireArguments()
        val builder = AlertDialog.Builder(requireContext())
        val view = layoutInflater.inflate(R.layout.reminder_layout, null)
        timeBtn = view.findViewById(R.id.pickTimeBtn)
        dateBtn = view.findViewById(R.id.pickDateBtn)

        builder.setView(view)
        timeBtn.setOnClickListener {
            selectTime()
        }
        dateBtn.setOnClickListener {
            selectDate()
        }
        builder.setPositiveButton("Save", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, p1: Int) {
                val request = OneTimeWorkRequestBuilder<MyWorker>().setInitialDelay(
                    2,
                    TimeUnit.MINUTES
                ).setInputData(workDataOf("Title" to bundle.getString("Title"),
                    "Content" to bundle.getString("Content"))).build()
                WorkManager.getInstance(requireContext()).enqueue(request)
                WorkManager.getInstance(requireContext()).getWorkInfoByIdLiveData(request.id)
                    .observe(this@ReminderDialog, Observer<WorkInfo>() {
                        var status = it.state.name
                        Log.d("Notification status: ", status)
                    })
            }
        })
        builder.setNegativeButton("Cancel", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, p1: Int) {
                dismiss()
            }
        })
        return builder.create()
    }

    private fun selectTime() {
        var selectedTime: String = ""
        val timePicker =
            MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).setHour(12)
                .setMinute(0).setTitleText("Select Time").setTheme(R.style.AppThemeMaterial).build()
        timePicker.show(
            (activity as AppCompatActivity?)!!.supportFragmentManager,
            timePicker.toString()
        )
        timePicker.addOnPositiveButtonClickListener {
            if (timePicker.hour >= 12) {
                selectedTime = String.format("%02d", timePicker.hour - 12) + ":" + String.format(
                    "%02d",
                    timePicker.minute
                ) + "PM"
            } else {
                selectedTime = String.format("%02d", timePicker.hour) + ":" + String.format(
                    "%02d",
                    timePicker.minute
                ) + "AM"
            }
            timeBtn.text = selectedTime
            Log.d("Selected Time: ", selectedTime)
        }
    }

    private fun selectDate() {
        val datePicker =
            MaterialDatePicker.Builder.datePicker().setTheme(R.style.DateThemeMaterial).build()
        datePicker.show(
            (activity as AppCompatActivity?)!!.supportFragmentManager,
            datePicker.toString()
        )
        datePicker.addOnPositiveButtonClickListener {
            Log.d("Selected Date: ", datePicker.headerText)
            dateBtn.text = datePicker.headerText
        }
    }
}