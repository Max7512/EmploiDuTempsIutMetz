package com.iutmetz.edt.data.remote.model

import com.google.gson.annotations.SerializedName

class GitHubTag (
    @SerializedName("name") val name: String,
    @SerializedName("zipball_url") val zipball_url: String,
    @SerializedName("tarball_url") val tarball_url: String,
    @SerializedName("commit") val commit: GitHubCommit,
    @SerializedName("node_id") val node_id: String,
)
{}