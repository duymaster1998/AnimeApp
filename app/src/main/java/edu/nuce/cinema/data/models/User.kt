package edu.nuce.cinema.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("accessToken")
    @Expose
    val accessToken: String,
    @SerializedName("advertisingScore")
    @Expose
    val advertisingScore: String,
    @SerializedName("avatar")
    @Expose
    val avatar: String,
    @SerializedName("email")
    @Expose
    val email: String,
    @SerializedName("firstName")
    @Expose
    val firstName: String,
    @SerializedName("id")
    @Expose
    val id: String,
    @SerializedName("lastName")
    @Expose
    val lastName: String,
    @SerializedName("phone")
    @Expose
    val phone: String,
    @SerializedName("rewardPoints")
    @Expose
    val rewardPoints: String,
    @SerializedName("role")
    @Expose
    val role: String,
    @SerializedName("socialKey")
    @Expose
    val socialKey: String
)