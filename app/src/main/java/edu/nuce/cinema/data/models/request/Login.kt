package edu.nuce.cinema.data.models.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Login(
    @SerializedName("email")
    @Expose
    val email: String,
    @SerializedName("firstName")
    @Expose
    val firstName: String,
    @SerializedName("lastName")
    @Expose
    val lastName: String,
    @SerializedName("socialKey")
    @Expose
    val socialKey: String,
    @SerializedName("token")
    @Expose
    val token: String,
    @SerializedName("avatar")
    @Expose
    val avatar: String,
)