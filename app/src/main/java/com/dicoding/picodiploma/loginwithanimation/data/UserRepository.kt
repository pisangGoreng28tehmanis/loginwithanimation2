package com.dicoding.picodiploma.loginwithanimation.data

import com.dicoding.picodiploma.loginwithanimation.data.api.retrofit.ApiConfig
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import com.dicoding.picodiploma.loginwithanimation.data.api.Result

class UserRepository private constructor(
    private val userPreference: UserPreference
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun login(email: String, password: String): Flow<Result<UserModel>> = flow {
        emit(Result.Loading) // Emit loading state
        try {
            val response = ApiConfig.getApiService().login(email, password)
            if (response.error == false) {
                val token = response.loginResult?.token
                if (token != null) {
                    val user = UserModel(email, token) // Buat UserModel dengan token
                    emit(Result.Success(user)) // Emit hasil sukses
                } else {
                    emit(Result.Error("Token tidak ditemukan"))
                }
            } else {
                emit(Result.Error(response.message ?: "Terjadi kesalahan"))
            }
        } catch (e: HttpException) {
            emit(Result.Error("Kesalahan jaringan"))
        } catch (e: Exception) {
            emit(Result.Error("Terjadi kesalahan"))
        }
    }


    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(userPreference: UserPreference): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference)
            }.also { instance = it }

        fun login(email: String, password: String): Flow<Result<UserModel>> = flow {
            emit(Result.Loading) // Emit loading state
            try {
                val response = ApiConfig.getApiService().login(email, password)
                if (response.error == false) {
                    val token = response.loginResult?.token
                    if (token != null) {
                        val user = UserModel(email, token) // Buat UserModel dengan token
                        emit(Result.Success(user)) // Emit hasil sukses
                    } else {
                        emit(Result.Error("Token tidak ditemukan"))
                    }
                } else {
                    emit(Result.Error(response.message ?: "Terjadi kesalahan"))
                }
            } catch (e: HttpException) {
                emit(Result.Error("Kesalahan jaringan"))
            } catch (e: Exception) {
                emit(Result.Error("Terjadi kesalahan"))
            }
        }

        fun register(name: String, email: String, password: String): Flow<Result<String>> = flow {
            emit(Result.Loading) // Emit loading state
            try {
                val response = ApiConfig.getApiService().register(name, email, password)
                if (response.error == false) {
                    emit(Result.Success("Registrasi berhasil")) // Emit success result
                } else {
                    emit(Result.Error(response.message ?: "Registrasi gagal"))
                }
            } catch (e: HttpException) {
                emit(Result.Error("Kesalahan jaringan"))
            } catch (e: Exception) {
                emit(Result.Error("Terjadi kesalahan"))
            }
        }

    }
}
