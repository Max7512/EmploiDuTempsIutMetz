package com.iutmetz.edt.data.remote.model

import com.google.gson.annotations.SerializedName

class GitHubCommit(
    @SerializedName("sha") val sha: String,
    @SerializedName("url") val url: String
) {
}