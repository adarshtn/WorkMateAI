package com.example.workmateai.utils

import android.content.Context
import android.util.Log
import org.json.JSONObject
import java.net.URL
import java.net.HttpURLConnection

data class AdzunaJob(
    val title: String,
    val company: String,
    val location: String,
    val redirectUrl: String
)

object JobSearchUtils {
    fun searchJobs(context: Context, skills: List<String>): List<AdzunaJob> {
        val appId = "0b8a0caf"
        val appKey = "498f6e67f6d1df9071f0a0595902dc52"
        val skillsQuery = skills.joinToString(" ")
        val url = "https://api.adzuna.com/v1/api/jobs/us/search/1?app_id=$appId&app_key=$appKey&what=$skillsQuery&content-type=application/json"
        Log.d("JobSearchUtils", "API URL: $url")

        try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("Accept", "application/json")
            connection.connectTimeout = 10000
            connection.readTimeout = 10000

            val responseCode = connection.responseCode
            val response = connection.inputStream.bufferedReader().use { it.readText() }
            Log.d("JobSearchUtils", "Raw API Response: $response")

            if (responseCode != 200) {
                Log.e("JobSearchUtils", "API failed with code $responseCode: $response")
                return emptyList()
            }

            val json = JSONObject(response)
            val results = json.getJSONArray("results")
            val jobs = mutableListOf<AdzunaJob>()
            for (i in 0 until results.length()) {
                val job = results.getJSONObject(i)
                jobs.add(
                    AdzunaJob(
                        title = job.getString("title"),
                        company = job.getJSONObject("company").getString("display_name"),
                        location = job.getJSONObject("location").getString("display_name"),
                        redirectUrl = job.getString("redirect_url")
                    )
                )
            }
            Log.d("JobSearchUtils", "Parsed Jobs: $jobs")
            return jobs
        } catch (e: Exception) {
            Log.e("JobSearchUtils", "Error fetching jobs: ${e.message}")
            return emptyList()
        }
    }
}