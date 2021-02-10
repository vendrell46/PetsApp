package com.carlos.myapps.petsapp

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Task(
    val id: String,
    val name: String,
    val description: String,
    val complete: Boolean
) : Parcelable