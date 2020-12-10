package edu.nuce.base.utils

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ApiException(
    @SerializedName("statusCode")
    @Expose
    val statusCode: Int?,
    @SerializedName("error")
    @Expose
    val error: String?,
    @SerializedName("message")
    @Expose
    val message: String?,
    @SerializedName("errors")
    @Expose
    val errors: String?,
    @SerializedName("timestamp")
    @Expose
    val timestamp: String?,
    @SerializedName("_path")
    @Expose
    val path: String?
)