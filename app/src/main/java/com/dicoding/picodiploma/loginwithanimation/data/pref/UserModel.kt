package com.dicoding.picodiploma.loginwithanimation.data.pref

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    val email: String,
    val token: String,
    val name : String,
    val isLogin: Boolean = false
): Parcelable