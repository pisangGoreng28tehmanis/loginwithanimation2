package com.dicoding.picodiploma.loginwithanimation.data

import android.util.Log
import com.dicoding.picodiploma.loginwithanimation.data.api.response.StoryResponse
import com.dicoding.picodiploma.loginwithanimation.data.api.retrofit.ApiService
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference
import java.io.IOException

class StoryRepository(private val apiService: ApiService, private val userPreference: UserPreference) {

    companion object {
        private var INSTANCE: StoryRepository? = null

        // Singleton pattern to get the instance
        fun getInstance(apiService: ApiService, userPreference: UserPreference): StoryRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = StoryRepository(apiService, userPreference)
                INSTANCE = instance
                instance
            }
        }
    }

    suspend fun getStories(token: String, page: Int = 0, size: Int = 10): StoryResponse {
        if (token.isEmpty()) {
            throw IllegalArgumentException("Token is missing")
        }

        return try {
            // Make API call
            val response = apiService.getStories(token, page, size)

            // Check if the response has an error
            if (response.error == true) {
                throw IOException("Error fetching stories: ${response.message}")

            }


            // Return the response
            response
        } catch (e: Exception) {
            Log.e("StoryRepository", "Error fetching stories: ${e.message}")
            throw e // Rethrow the exception
        }
    }



}
