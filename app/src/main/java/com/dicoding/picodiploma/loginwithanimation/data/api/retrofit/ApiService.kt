package com.dicoding.picodiploma.loginwithanimation.data.api.retrofit

import com.dicoding.picodiploma.loginwithanimation.data.api.LoginResponse
import com.dicoding.picodiploma.loginwithanimation.data.api.RegisterResponse
import com.dicoding.picodiploma.loginwithanimation.data.api.response.StoryResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @FormUrlEncoded
    @POST("register")  // Perbaikan endpoint register
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")  // Perbaikan endpoint login
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("name") name: String? = null,
        @Field("token") token: String? = null
    ): LoginResponse


    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int = 0
    ): StoryResponse /*{
        return try {
            // Make the actual API call using the ApiConfig to get the ApiService
            val apiService = ApiConfig.getApiService(token)  // Get the API service with the token
            apiService.getStories(token)  // Call the API to get the stories
        } catch (e: Exception) {
            // Handle the exception properly, maybe return a default StoryResponse or throw the error
            throw e
        }
    }*/


}
