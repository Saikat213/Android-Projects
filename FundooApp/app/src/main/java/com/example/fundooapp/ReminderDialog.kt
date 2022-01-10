package com.example.fundooapp

import android.app.*
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.work.*
import java.util.*
import java.util.concurrent.TimeUnit

class ReminderDialog : DialogFragment(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    lateinit var timeBtn: Button
    lateinit var dateBtn: Button
    var year = 0
    var month = 0
    var day = 0
    var hour = 0
    var minute = 0

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
                val delay = calculateTimeDifference(hour, minute)
                val request = OneTimeWorkRequestBuilder<MyWorker>().setInitialDelay(
                    delay.toLong(),
                    TimeUnit.MINUTES
                ).setInputData(workDataOf("Title" to bundle.getString("Title"),
                    "Content" to bundle.getString("Content"))).build()
                WorkManager.getInstance(requireContext()).enqueue(request)
                WorkManager.getInstance(requireContext()).getWorkInfoByIdLiveData(request.id)
                    .observe(this@ReminderDialog, Observer<WorkInfo>() {
                        Log.d("Notification status: ", it.state.name)
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

    private fun calculateTimeDifference(hour: Int, minute: Int) : Int {
        val today = Calendar.getInstance()
        val today_hr = today.get(Calendar.HOUR_OF_DAY)
        val today_min = today.get(Calendar.MINUTE)
        if (hour == today_hr) {
            if (minute > today_min)
                return minute - today_min
            else
                return 0
        } else if (hour == today_hr + 1) {
            if (minute > today_min) {
                return minute - today_min + 60
            } else if (minute == today_min) {
                return 60
            } else {
                return (60 - today_min) + minute
            }
        } else {
            var timeDiff : Int = 0
            var tempHr = hour
            while (tempHr > today_hr + 1) {
                timeDiff++
                tempHr--
            }
            if (minute > today_min) {
                return ((minute - today_min) + (timeDiff * 60))
            } else {
                return ((60 - today_min + minute) + (timeDiff * 60))
            }
        }
    }

    private fun selectTime() {
        val calender = Calendar.getInstance()
        val hr = calender.get(Calendar.HOUR_OF_DAY)
        val min = calender.get(Calendar.MINUTE)
        TimePickerDialog(requireContext(), this, hr, min,
            android.text.format.DateFormat.is24HourFormat(requireContext())).show()
    }

    private fun selectDate() {
        val calender = Calendar.getInstance()
        val yr = calender.get(Calendar.YEAR)
        val monthOfYr = calender.get(Calendar.MONTH)
        val dayOfYr = calender.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(requireContext(), this, yr, monthOfYr, dayOfYr).show()
    }

    override fun onDateSet(p0: DatePicker?, years: Int, months: Int, days: Int) {
        Calendar.getInstance().let {
            it.set(years, months, days)
            dateBtn.text = days.toString().padStart(2, '0') + "-" +
                    (months+1).toString().padStart(2, '0') + "-" + years
            this.year = years
            this.month = months + 1
            this.day = days
        }
    }

    override fun onTimeSet(p0: TimePicker?, hourOfDay: Int, minuteOfDay: Int) {
        timeBtn.text = hourOfDay.toString() +":"+minuteOfDay.toString()
        this.hour = hourOfDay
        this.minute = minuteOfDay
    }
}