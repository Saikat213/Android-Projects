package com.example.unitconverter

class ConvertUnits {
    fun lengthMetrics(inputMetric : String, outputMetric : String, value : Double) : Double {
        if (inputMetric == outputMetric)
            return value * 1.0
        if (inputMetric == "KM" && outputMetric == "M")
            return value * 1000.0
        if (inputMetric == "KM" && outputMetric == "CM")
            return value * 100000.0
        if (inputMetric == "KM" && outputMetric == "FOOT")
            return value * 3281.0
        if (inputMetric == "KM" && outputMetric == "MILES")
            return value / 1.609
        if (inputMetric == "M" && outputMetric == "KM")
            return value / 1000.0
        if (inputMetric == "M" && outputMetric == "CM")
            return value * 100.0
        if (inputMetric == "M" && outputMetric == "FOOT")
            return value * 3.281
        if (inputMetric == "M" && outputMetric == "MILES")
            return value / 1609.0
        if (inputMetric == "CM" && outputMetric == "KM")
            return value / 100000.0
        if (inputMetric == "CM" && outputMetric == "M")
            return value / 100.0
        if (inputMetric == "CM" && outputMetric == "FOOT")
            return value / 30.48
        if (inputMetric == "CM" && outputMetric == "MILES")
            return value / 160934.0
        if (inputMetric == "FOOT" && outputMetric == "KM")
            return value / 3281.0
        if (inputMetric == "FOOT" && outputMetric == "M")
            return value / 3.281
        if (inputMetric == "FOOT" && outputMetric == "CM")
            return value * 30.48
        if (inputMetric == "FOOT" && outputMetric == "MILES")
            return value / 5280.0
        if (inputMetric == "MILES" && outputMetric == "KM")
            return value * 1.609
        if (inputMetric == "MILES" && outputMetric == "M")
            return value * 1609.0
        if (inputMetric == "MILES" && outputMetric == "CM")
            return value * 160934.0
        if (inputMetric == "MILES" && outputMetric == "FOOT")
            return value * 5280.0
        return 0.0
    }
}