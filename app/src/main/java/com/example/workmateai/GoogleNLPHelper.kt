package com.example.workmateai.utils

import android.content.Context
import android.util.Log
import com.example.workmateai.R
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

object GoogleNLPHelper {
    private val client = OkHttpClient()
    private val gson = Gson()

    suspend fun analyzeText(
        context: Context,
        text: String
    ): Result<List<String>> = withContext(Dispatchers.Default) {
        try {
            val apiKey = context.getString(R.string.google_cloud_api_key)
            val url = "https://language.googleapis.com/v1/documents:analyzeEntities?key=$apiKey"

            val jsonBody = JsonObject().apply {
                val document = JsonObject().apply {
                    addProperty("type", "PLAIN_TEXT")
                    addProperty("content", text)
                }
                add("document", document)
                addProperty("encodingType", "UTF8")
            }

            val requestBody = gson.toJson(jsonBody).toRequestBody("application/json".toMediaTypeOrNull())

            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                val nlpResponse = gson.fromJson(responseBody, NLPResponse::class.java)

                val skills = nlpResponse.entities
                    .filter { it.type == "OTHER" || it.type == "WORK_OF_ART" }
                    .map { it.name }

                Result.success(skills)
            } else {
                Result.failure(Exception("API Error: ${response.code} - ${response.message}"))
            }
        } catch (e: IOException) {
            Result.failure(Exception("Network Error: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(Exception("Processing Error: ${e.message}"))
        }
    }
}

data class NLPResponse(val entities: List<Entity>)
data class Entity(val name: String, val type: String)