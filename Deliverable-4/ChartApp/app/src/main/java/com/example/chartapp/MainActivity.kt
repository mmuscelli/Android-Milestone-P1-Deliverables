package com.example.chartapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lineDataSet: LineDataSet = randomDataSet()
        val mainLineChart: LineChart = findViewById(R.id.mainLineChart) // Chart object

        // Configure and populate the chart
        mainLineChart.data = LineData(lineDataSet)
        mainLineChart.animateX(100)
    }

    fun randomDataSet(): LineDataSet {
        // List of entries
        val lineData: MutableList<Entry> = mutableListOf()

        // Generate a random data set
        for (x in 0..59) {
            val randomEntry = Entry(x.toFloat(), Random.nextFloat())
            lineData.add(randomEntry)
        }

        return LineDataSet(lineData, "Random Numbers")
    }
}