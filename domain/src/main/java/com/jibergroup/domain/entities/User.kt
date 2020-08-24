package com.jibergroup.domain.entities

import com.google.gson.annotations.SerializedName

data class User(
    val id: Int,
    val login: String,
    @SerializedName("avatar_url")
    val avatar: String,
    val url: String,
    @SerializedName("html_url")
    val htmlUrl: String,
    @SerializedName("gravatar_id")
    val avatarId: String,
    @SerializedName("node_id")
    val nodeId: String,
    val type: String,
    @SerializedName("site_admin")
    val isSiteAdmin: Boolean,
    val score: Float,
    val blog: String? = null,
    val company: String? = null,
    val name: String? = null,
    val location: String? = null,
    val email: String? = null,
    val bio: String? = null,
    val public_repos: Int? = null,
    val followers: Int? = null,
    val following: Int? = null,
    @SerializedName("created_at")
    val created: String? = null,
    @SerializedName("updated_at")
    val updated: String? = null
)
