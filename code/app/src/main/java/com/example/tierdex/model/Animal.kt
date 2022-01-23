package com.example.tierdex.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Animal(@StringRes val animalID: Int, @DrawableRes val imgRes: Int ) {}