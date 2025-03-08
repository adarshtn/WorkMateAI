package com.example.workmateai.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.workmateai.Job // Import your Job class

class JobAdapter(private val jobs: List<Job>, private val onJobClick: (String) -> Unit) :
    RecyclerView.Adapter<JobAdapter.JobViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return JobViewHolder(view)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val job = jobs[position]
        holder.textView.text = "${job.title} - ${job.company} (${job.location})"
        holder.itemView.setOnClickListener { onJobClick(job.link) } // Fixed to 'link'
    }

    override fun getItemCount(): Int = jobs.size

    class JobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(android.R.id.text1)
    }
}