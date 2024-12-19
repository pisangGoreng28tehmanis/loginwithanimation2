package com.dicoding.picodiploma.loginwithanimation.data.api.retrofit

import com.dicoding.picodiploma.loginwithanimation.data.api.LoginResponse
import com.dicoding.picodiploma.loginwithanimation.data.api.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

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
}
