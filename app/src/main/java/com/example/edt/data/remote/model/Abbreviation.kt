package com.example.edt.data.remote.model

import com.google.gson.annotations.SerializedName

class Abbreviation(
    @SerializedName("mod_lib") val mod_lib: String,
    @SerializedName("mod_code") val mod_code: String,
) {
}