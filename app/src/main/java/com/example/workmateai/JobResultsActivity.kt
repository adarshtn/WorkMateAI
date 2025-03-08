package com.example.workmateai.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workmateai.R
import com.example.workmateai.Job

import android.util.Log
import android.widget.TextView

class JobResultsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jobs)

        Log.d("JobResultsActivity", "Activity started")


        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val noJobsText: TextView = findViewById(R.id.noJobsText)

        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerView.visibility = View.GONE
        noJobsText.visibility = View.GONE

        val jobs = intent.getParcelableArrayListExtra<Job>("JOB_LIST") ?: emptyList()
        Log.d("JobResultsActivity", "Received jobs: $jobs")



        if (jobs.isEmpty()) {
            noJobsText.visibility = View.VISIBLE
            Log.d("JobResultsActivity", "No jobs to display")
        } else {
            recyclerView.visibility = View.VISIBLE
            recyclerView.adapter = JobAdapter(jobs) { url ->
                Log.d("JobResultsActivity", "Job clicked: $url")
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            }
            recyclerView.adapter?.notifyDataSetChanged()
            Log.d("JobResultsActivity", "Jobs displayed: ${jobs.size}")
        }
    }
}