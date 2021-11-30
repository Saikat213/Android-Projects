package com.example.unitconverter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.*

class ConvertFragment : Fragment(R.layout.fragment_convert) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val metrics = arrayOf("KM", "M", "CM", "FOOT", "MILES")
        var selectedInputMetric = ""
        var selectedOutputMetric = ""

        val spinner1 = view.findViewById<Spinner>(R.id.spinner1)
        val spinner2 = view.findViewById<Spinner>(R.id.spinner2)
        val convert = view.findViewById<Button>(R.id.button3)
        val input = view.findViewById<EditText>(R.id.editText)
        val outputValue = view.findViewById<TextView>(R.id.textView3)

        var metricAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, metrics)
        spinner1.adapter = metricAdapter
        spinner2.adapter = metricAdapter

        spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedOutputMetric = metrics[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedInputMetric = metrics[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        convert.setOnClickListener {
            var inputValue = input.text.toString().toInt()
            var conversion = ConvertUnits().lengthMetrics(selectedInputMetric, selectedOutputMetric, inputValue.toDouble())
            outputValue.text = conversion.toString()
        }
    }
}