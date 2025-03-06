package com.example.workmateai.utils

import android.content.Context
import android.util.Log
import com.example.workmateai.Job
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

private val client = OkHttpClient()
private val gson = Gson()

suspend fun searchJobs(context: Context, skills: List<String>): List<Job> = withContext(Dispatchers.IO) {
    try {
        val apiKey = context.getString(com.example.workmateai.R.string.google_cloud_api_key)
        val projectId = "YOUR_PROJECT_ID" // Replace with your Google Cloud project ID
        val url = "https://jobs.googleapis.com/v4/projects/$projectId/jobs:search?key=$apiKey"

        // Limit to top 5 skills for a concise query
        val querySkills = skills.take(5)
        val query = querySkills.joinToString(" ")
        Log.d("JobSearchUtils", "Searching jobs with query: $query (from skills: $skills)")

        val requestBody = """
            {
                "requestMetadata": {
                    "domain": "your-app-domain.com",
                    "userId": "anonymous"
                },
                "searchMode": "JOB_SEARCH",
                "query": "$query",
                "pageSize": 10
            }
        """.trimIndent().toRequestBody("application/json".toMediaTypeOrNull())

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        val response = client.newCall(request).execute()

        if (response.isSuccessful) {
            val responseBody = response.body?.string()
            Log.d("JobSearchUtils", "API Response: $responseBody")
            val jobResponse = gson.fromJson(responseBody, JobSearchResponse::class.java)
            jobResponse.matchingJobs?.take(10)?.map { it.toJob() } ?: emptyList()
        } else {
            val errorBody = response.body?.string()
            Log.e("JobSearchUtils", "API Error: ${response.code} - ${response.message}, Body: $errorBody")
            throw Exception("API Error: ${response.code} - ${response.message}")
        }
    } catch (e: IOException) {
        Log.e("JobSearchUtils", "Network Error: ${e.message}")
        throw Exception("Network Error: ${e.message}")
    } catch (e: Exception) {
        Log.e("JobSearchUtils", "Job Search Error: ${e.message}")
        throw Exception("Job Search Error: ${e.message}")
    }
}

data class JobSearchResponse(
    @SerializedName("matchingJobs") val matchingJobs: List<MatchingJob>?
)

data class MatchingJob(
    @SerializedName("jobTitle") val jobTitle: String?,
    @SerializedName("companyName") val companyName: String?,
    @SerializedName("location") val location: String?,
    @SerializedName("requisitionId") val requisitionId: String?
) {
    fun toJob(): Job {
        return Job(
            title = jobTitle ?: "Unknown Title",
            company = companyName ?: "Unknown Company",
            location = location ?: "Unknown Location",
            link = "https://jobs.google.com/$requisitionId" // Placeholder
        )
    }
}