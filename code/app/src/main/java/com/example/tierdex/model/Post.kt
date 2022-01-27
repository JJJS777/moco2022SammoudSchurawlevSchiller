package com.example.tierdex.model


import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Post(
    @StringRes val userID: Int,
    @DrawableRes val imageResourceId: Int
)