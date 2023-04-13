package com.example.connectedapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private val retrofit : Retrofit by lazy {
    Retrofit.Builder().baseUrl("https://api.github.com/")
        .addConverterFactory(GsonConverterFactory.create()).build()
}

class MainActivity : AppCompatActivity() {

    // Variables for UI components
    private lateinit var ownerEditText: EditText
    private lateinit var repoEditText: EditText
    private lateinit var requestBtn: Button
    private lateinit var resultText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Bind UI components
        ownerEditText = findViewById(R.id.ownerEditText)
        repoEditText = findViewById(R.id.repoEditText)
        requestBtn = findViewById(R.id.requestBtn)
        resultText = findViewById(R.id.resultTextView)

        // Event listener
        requestBtn.setOnClickListener {
            val owner = ownerEditText.text.toString()
            val repo = repoEditText.text.toString()

            if (owner == "" || repo == "") {
                Toast.makeText(this,
                    "Both owner and repo must be provided.", Toast.LENGTH_LONG).show()
            } else {
                sendRequest(owner, repo)
            }
        }
    }

    private fun sendRequest(owner: String, repo: String) {
        // Get RetrofitService
        val retrofitService = retrofit.create(ApiService::class.java)

        // Send request
        val retrofitCall = retrofitService.getContributionCounts(owner, repo)

        retrofitCall.enqueue(object: retrofit2.Callback<ContributionCounts> {
            override fun onResponse(call: Call<ContributionCounts>, response: Response<ContributionCounts>) {
                // Check whether response is successful or not
                if (response.isSuccessful) {
                    // Access data using response.body()
                    if (response.body()!!.all.isNotEmpty()) {
                        // Data sent from GitHub API
                        val contributionCounts = response.body()!!.all

                        var totalContributionCounts = 0
                        for (counts in contributionCounts) {
                            totalContributionCounts += counts
                        }

                        // Populate this value to UI components
                        resultText.text = "Total Contribution: $totalContributionCounts"
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Request was not successful", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ContributionCounts>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Request failed", Toast.LENGTH_LONG).show()
            }
        })

    }
}