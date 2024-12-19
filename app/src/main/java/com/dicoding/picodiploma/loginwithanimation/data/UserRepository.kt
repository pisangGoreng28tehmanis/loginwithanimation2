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
            // Mengirim request login ke server
            val response = ApiConfig.getApiService().login(email, password)

            // Mengecek apakah response error atau tidak
            if (response.error == false) {
                val loginResult = response.loginResult
                val token = loginResult?.token
                val name = loginResult?.name // Ambil nama dari respons

                // Jika token ditemukan, buat objek UserModel dengan name dan token
                if (token != null && name != null) {
                    val user = UserModel(
                        name = name,  // Dapatkan nilai name dari server
                        email = email,
                        token = token
                    )
                    emit(Result.Success(user)) // Emit hasil sukses
                } else {
                    emit(Result.Error("Token atau nama tidak ditemukan"))
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
                // Send login request to server
                val response = ApiConfig.getApiService().login(email, password)

                // Check if response is successful
                if (response.error == false) {
                    val loginResult = response.loginResult
                    val token = loginResult?.token
                    val name = loginResult?.name // Get name from response

                    // If token and name are found, create a UserModel object
                    if (token != null && name != null) {
                        val user = UserModel(
                            name = name,  // Get name from the response
                            email = email,
                            token = token
                        )
                        emit(Result.Success(user)) // Emit success result
                    } else {
                        emit(Result.Error("Token or name not found"))
                    }
                } else {
                    emit(Result.Error(response.message ?: "An error occurred"))
                }
            } catch (e: HttpException) {
                emit(Result.Error("Network error"))
            } catch (e: Exception) {
                emit(Result.Error("An error occurred"))
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
