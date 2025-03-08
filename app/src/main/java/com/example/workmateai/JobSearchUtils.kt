package com.example.workmateai.utils

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

// Data class for Adzuna job results
data class AdzunaJob(
    val title: String,
    val company: String,
    val description: String,
    val location: String,
    val redirectUrl: String
)

object JobSearchUtils {
    private val client = OkHttpClient()
    private val gson = Gson()

    suspend fun searchJobs(context: Context, skills: List<String>): List<AdzunaJob> = withContext(Dispatchers.IO) {
        try {
            val appId = "0b8a0caf" // Your Adzuna App ID
            val appKey = "498f6e67f6d1df9071f0a0595902dc52" // Your Adzuna App Key
            val query = skills.take(5).joinToString(" ") // e.g., "Java Kotlin C HTML CSS"
            val url = "https://api.adzuna.com/v1/api/jobs/us/search/1?app_id=$appId&app_key=$appKey&what=" + query.replace(" ", "%20") + "&content-type=application/json"
            Log.d("JobSearchUtils", "API URL: $url")

            val request = Request.Builder()
                .url(url)
                .get()
                .build()

            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                Log.d("JobSearchUtils", "Raw API Response: $responseBody")
                val json = JSONObject(responseBody)
                val results = json.getJSONArray("results")
                val jobs = mutableListOf<AdzunaJob>()
                for (i in 0 until results.length()) {
                    val job = results.getJSONObject(i)
                    jobs.add(
                        AdzunaJob(
                            title = job.getString("title"),
                            company = job.getJSONObject("company").getString("display_name"),
                            description = job.getString("description"),
                            location = job.getJSONObject("location").getJSONArray("area").getString(0),
                            redirectUrl = job.getString("redirect_url")
                        )
                    )
                }
                Log.d("JobSearchUtils", "Parsed Jobs: $jobs")
                jobs.take(10) // Limit to 10 results
            } else {
                val errorBody = response.body?.string()
                Log.e("JobSearchUtils", "API Error: ${response.code} - ${response.message}, Body: $errorBody")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("JobSearchUtils", "Job Search Error: ${e.message}")
            emptyList()
        }
    }
}