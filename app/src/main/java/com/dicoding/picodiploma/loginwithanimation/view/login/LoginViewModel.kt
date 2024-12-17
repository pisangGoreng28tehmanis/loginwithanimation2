package com.dicoding.picodiploma.loginwithanimation.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import com.dicoding.picodiploma.loginwithanimation.data.api.Result

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun login(email: String, password: String) {
        viewModelScope.launch {
            repository.login(email, password).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        // Tampilkan indikator loading (bisa dengan LiveData)
                    }
                    is Result.Success -> {
                        saveSession(result.data) // Simpan sesi jika login sukses
                        // Lanjutkan ke MainActivity
                    }
                    is Result.Error -> {
                        _errorMessage.value = result.error // Set error message ke LiveData
                    }
                }
            }
        }
    }

    private fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}
