package com.example.edt.data.remote.model

import com.google.gson.annotations.SerializedName

class Data<T>(
    @SerializedName("name") val name: String,
    @SerializedName("data") val data: T,
)