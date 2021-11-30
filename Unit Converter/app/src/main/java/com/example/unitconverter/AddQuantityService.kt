package com.example.unitconverter

class AddQuantityService {
    fun lengthMetricsAdd(value1: Double, value2: Double, metric1: String, metric2: String,
                         metric3: String): Double {
        var tempVal : Double
        if (metric1 == "KM" && metric2 == "KM") {
            tempVal = value1 + value2
            return ConvertUnits().lengthMetrics(metric1, metric3, tempVal)
        }
        if (metric1 == "KM" && metric2 == "M") {
            var temp = value1 * 1000
            tempVal = temp + value2
            return ConvertUnits().lengthMetrics(metric2, metric3, tempVal)
        }
        if (metric1 == "KM" && metric2 == "CM") {
            var temp = value1 * 100000
            tempVal = temp + value2
            return ConvertUnits().lengthMetrics(metric2, metric3, tempVal)
        }
        if (metric1 == "KM" && metric2 == "FOOT") {
            var temp = value1 * 3281
            tempVal = temp + value2
            return ConvertUnits().lengthMetrics(metric2, metric3, tempVal)
        }
        if (metric1 == "M" && metric2 == "KM") {
            var temp = value2 * 1000
            tempVal = temp + value1
            return ConvertUnits().lengthMetrics(metric1, metric3, tempVal)
        }
        if (metric1 == "M" && metric2 == "M") {
            tempVal = value1 + value2
            return ConvertUnits().lengthMetrics(metric1, metric3, tempVal)
        }
        if (metric1 == "M" && metric2 == "CM") {
            var temp = value1 * 100
            tempVal = temp + value2
            return ConvertUnits().lengthMetrics(metric2, metric3, tempVal)
        }
        if (metric1 == "M" && metric2 == "FOOT") {
            var temp = value1 * 3.281
            return ConvertUnits().lengthMetrics(metric2, metric3, temp + value2)
        }
        if (metric1 == "CM" && metric2 == "KM") {
            var temp = value2 * 100000
            tempVal = temp + value1
            return ConvertUnits().lengthMetrics(metric1, metric3, tempVal)
        }
        if (metric1 == "CM" && metric2 == "M") {
            var temp = value2 * 100
            tempVal = temp + value1
            return ConvertUnits().lengthMetrics(metric1, metric3, tempVal)
        }
        if (metric1 == "CM" && metric2 == "CM") {
            tempVal = value1 + value2
            return ConvertUnits().lengthMetrics(metric1, metric3, tempVal)
        }
        if (metric1 == "CM" && metric2 == "FOOT") {
            var temp = value2 * 30.48
            return ConvertUnits().lengthMetrics(metric1, metric3, temp + value1)
        }
        if (metric1 == "FOOT" && metric2 == "KM") {
            var temp = value2 * 3281
            tempVal = temp + value1
            return ConvertUnits().lengthMetrics(metric1, metric3, tempVal)
        }
        if (metric1 == "FOOT" && metric2 == "M") {
            var temp = value2 * 3.281
            return ConvertUnits().lengthMetrics(metric1, metric3, temp + value1)
        }
        if (metric1 == "FOOT" && metric2 == "CM") {
            var temp = value2 / 30.48
            return ConvertUnits().lengthMetrics(metric1, metric3, temp + value1)
        }
        if (metric1 == "FOOT" && metric2 == "FOOT") {
            tempVal = value1 + value2
            return ConvertUnits().lengthMetrics(metric1, metric3, tempVal)
        }
        return 0.0
    }

    fun temperatureMetrics(value1: Double, value2: Double, metric1: String, metric2: String,
                                metric3: String) : Double {
        var tempVal : Double
        if (metric1 == "CELSIUS" && metric2 == "CELSIUS") {
            tempVal = value1 + value2
            return temperatureConvert(metric1, metric3, tempVal)
        }
        if (metric1 == "CELSIUS" && metric2 == "FAHRENHEIT") {
            var temp = (value1 * 9 / 5) + 32
            tempVal = temp + value2
            return temperatureConvert(metric2, metric3, tempVal)
        }
        if (metric1 == "CELSIUS" && metric2 == "KELVIN") {
            var temp = value1 + 273.15
            tempVal = temp + value2
            return temperatureConvert(metric2, metric3, tempVal)
        }
        if (metric1 == "FAHRENHEIT" && metric2 == "CELSIUS") {
            var temp = (value1 - 32) * 5 / 9
            tempVal = temp + value2
            return temperatureConvert(metric2, metric3, tempVal)
        }
        if (metric1 == "FAHRENHEIT" && metric2 == "FAHRENHEIT") {
            tempVal = value1 + value2
            return temperatureConvert(metric1, metric3, tempVal)
        }
        if (metric1 == "FAHRENHEIT" && metric2 == "KELVIN") {
            var temp = (value1 - 32) * 5 / 9 + 273.15
            tempVal = temp + value2
            return temperatureConvert(metric2, metric3, tempVal)
        }
        if (metric1 == "KELVIN" && metric2 == "CELSIUS") {
            var temp = value1 - 273.15
            tempVal = temp + value2
            return temperatureConvert(metric2, metric3, tempVal)
        }
        if (metric1 == "KELVIN" && metric2 == "FAHRENHEIT") {
            var temp = (value1 - 273.15) * 9 / 5 + 32
            tempVal = temp + value2
            return temperatureConvert(metric2, metric3, tempVal)
        }
        if (metric1 == "KELVIN" && metric2 == "KELVIN") {
            tempVal = value1 + value2
            return temperatureConvert(metric1, metric3, tempVal)
        }
        return 0.0
    }

    fun weightMetrics(value1: Double, value2: Double, metric1: String, metric2: String,
                      metric3: String) : Double {
        var tempVal : Double
        if (metric1 == "KG" && metric2 == "KG") {
            tempVal = value1 + value2
            return weightConvert(metric1, metric3, tempVal)
        }
        if (metric1 == "KG" && metric2 == "GRAMS") {
            var temp = value1 * 1000
            tempVal = temp + value2
            return weightConvert(metric2, metric3, tempVal)
        }
        if (metric1 == "KG" && metric2 == "POUNDS") {
            var temp = value1 * 2.205
            tempVal = temp + value2
            return weightConvert(metric2, metric3, tempVal)
        }
        if (metric1 == "GRAMS" && metric2 == "KG") {
            var temp = value2 * 1000
            tempVal = temp + value1
            return weightConvert(metric1, metric3, tempVal)
        }
        if (metric1 == "GRAMS" && metric2 == "GRAMS") {
            tempVal = value1 + value2
            return weightConvert(metric1, metric3, tempVal)
        }
        if (metric1 == "GRAMS" && metric2 == "POUNDS") {
            var temp = value1 / 454
            tempVal = temp + value2
            return weightConvert(metric2, metric3, tempVal)
        }
        if (metric1 == "POUNDS" && metric2 == "KG") {
            var temp = value2 * 2.205
            tempVal = temp + value1
            return weightConvert(metric1, metric3, tempVal)
        }
        if (metric1 == "POUNDS" && metric2 == "GRAMS") {
            var temp = value1 * 454
            tempVal = temp + value2
            return weightConvert(metric2, metric3, tempVal)
        }
        if (metric1 == "POUNDS" && metric2 == "POUNDS") {
            tempVal = value1 + value2
            return weightConvert(metric1, metric3, tempVal)
        }
        return 0.0
    }

    fun weightConvert(inputMetric: String, outputMetric: String, value: Double) : Double {
        if (inputMetric == outputMetric)
            return value
        if (inputMetric == "KG" && outputMetric == "GRAMS")
            return value * 1000
        if (inputMetric == "KG" && outputMetric == "POUNDS")
            return value * 2.205
        if (inputMetric == "GRAMS" && outputMetric == "KG")
            return value / 1000
        if (inputMetric == "GRAMS" && outputMetric == "POUNDS")
            return value / 454
        if (inputMetric == "POUNDS" && outputMetric == "KG")
            return value / 2.205
        if (inputMetric == "POUNDS" && outputMetric == "GRAMS")
            return value * 454
        return 0.0
    }

    fun temperatureConvert(inputMetric : String, outputMetric : String, value : Double) : Double {
        if (inputMetric == outputMetric)
            return value
        if (inputMetric == "CELSIUS" && outputMetric == "FAHRENHEIT")
            return ((value * 9 / 5) + 32)
        if (inputMetric == "CELSIUS" && outputMetric == "KELVIN")
            return value + 273.15
        if (inputMetric == "FAHRENHEIT" && outputMetric == "CELSIUS")
            return ((value - 32) * 5 / 9)
        if (inputMetric == "FAHRENHEIT" && outputMetric == "KELVIN")
            return ((value - 32) * 5 / 9) + 273.15
        if (inputMetric == "KELVIN" && outputMetric == "CELSIUS")
            return value - 273.15
        if (inputMetric == "KELVIN" && outputMetric == "FAHRENHEIT")
            return ((value - 273.15) * 9 / 5) + 32
        return 0.0
    }
}