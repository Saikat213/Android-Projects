package com.example.unitconverter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.*

class AddQuantityFragment : Fragment(R.layout.fragment_add_quantity) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainMetrics = arrayOf("LENGTH","TEMPERATURE", "WEIGHT")
        val lengthMetrics = arrayOf("KM", "M", "CM", "FOOT")
        val tempMetrics = arrayOf("CELSIUS", "FAHRENHEIT", "KELVIN")
        val weightMetrics = arrayOf("KG", "GRAMS", "POUNDS")
        var inputMetric1 = ""
        var inputMetric2 = ""
        var resultMetric = ""
        var targetMetricSpinner = ""

        val parentSpinner = view.findViewById<Spinner>(R.id.spinner)
        val childSpinnerLeft = view.findViewById<Spinner>(R.id.spinner3)
        val childSpinnerRight = view.findViewById<Spinner>(R.id.spinner4)
        val childSpinnerBottom = view.findViewById<Spinner>(R.id.spinner5)
        val input1 = view.findViewById<EditText>(R.id.input1)
        val input2 = view.findViewById<EditText>(R.id.input2)
        val outputVal = view.findViewById<TextView>(R.id.textView6)
        val resultButton = view.findViewById<Button>(R.id.Result)

        var metricAdapter = ArrayAdapter<String>(requireContext(),
            android.R.layout.simple_spinner_dropdown_item, mainMetrics)
        parentSpinner.adapter = metricAdapter

        parentSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (mainMetrics[p2] == "LENGTH") {
                    var lengthMetricAdapter = ArrayAdapter<String>(requireContext(),
                        android.R.layout.simple_spinner_dropdown_item, lengthMetrics)
                    childSpinnerLeft.adapter = lengthMetricAdapter
                    childSpinnerRight.adapter = lengthMetricAdapter
                    childSpinnerBottom.adapter = lengthMetricAdapter
                    targetMetricSpinner = mainMetrics[p2]
                }
                if (mainMetrics[p2] == "TEMPERATURE") {
                    var tempMetricAdapter = ArrayAdapter<String>(requireContext(),
                        android.R.layout.simple_spinner_dropdown_item, tempMetrics)
                    childSpinnerLeft.adapter = tempMetricAdapter
                    childSpinnerRight.adapter = tempMetricAdapter
                    childSpinnerBottom.adapter = tempMetricAdapter
                    targetMetricSpinner = mainMetrics[p2]
                }
                if (mainMetrics[p2] == "WEIGHT") {
                    var weightMetricAdapter = ArrayAdapter<String>(requireContext(),
                        android.R.layout.simple_spinner_dropdown_item, weightMetrics)
                    childSpinnerLeft.adapter = weightMetricAdapter
                    childSpinnerRight.adapter = weightMetricAdapter
                    childSpinnerBottom.adapter = weightMetricAdapter
                    targetMetricSpinner = mainMetrics[p2]
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        childSpinnerLeft.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (targetMetricSpinner == "LENGTH")
                    inputMetric1 = lengthMetrics[p2]
                if (targetMetricSpinner == "TEMPERATURE")
                    inputMetric1 = tempMetrics[p2]
                if (targetMetricSpinner == "WEIGHT")
                    inputMetric1 = weightMetrics[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        childSpinnerRight.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (targetMetricSpinner == "LENGTH")
                    inputMetric2 = lengthMetrics[p2]
                if (targetMetricSpinner == "TEMPERATURE")
                    inputMetric2 = tempMetrics[p2]
                if (targetMetricSpinner == "WEIGHT")
                    inputMetric2 = weightMetrics[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        childSpinnerBottom.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (targetMetricSpinner == "LENGTH")
                    resultMetric = lengthMetrics[p2]
                if (targetMetricSpinner == "TEMPERATURE")
                    resultMetric = tempMetrics[p2]
                if (targetMetricSpinner == "WEIGHT")
                    resultMetric = weightMetrics[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        resultButton.setOnClickListener {
            var inputValue1 = input1.text.toString().toDouble()
            var inputValue2 = input2.text.toString().toDouble()
            if (targetMetricSpinner == "LENGTH") {
                var addQuantities = AddQuantityService().lengthMetricsAdd(
                    inputValue1, inputValue2,
                    inputMetric1, inputMetric2, resultMetric
                )
                outputVal.text = addQuantities.toString()
            }
            if (targetMetricSpinner == "TEMPERATURE") {
                var tempQuantities = AddQuantityService().temperatureMetrics(inputValue1,
                            inputValue2, inputMetric1, inputMetric2, resultMetric)
                outputVal.text = tempQuantities.toString()
            }
            if (targetMetricSpinner == "WEIGHT") {
                var weightQuantities = AddQuantityService().weightMetrics(inputValue1,
                                inputValue2, inputMetric1, inputMetric2, resultMetric)
                outputVal.text = weightQuantities.toString()
            }
        }
    }
}