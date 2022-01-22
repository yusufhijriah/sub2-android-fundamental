package com.dicoding.submission2

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var username: String,
    var avatar: String,
    var company: String,
    var location: String,
    var name: String,
    var repository: String,
    var followers: String,
    var following: String,
) : Parcelable
