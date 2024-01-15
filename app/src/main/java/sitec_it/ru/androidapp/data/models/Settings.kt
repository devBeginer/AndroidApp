package sitec_it.ru.androidapp.data.models

import com.google.gson.annotations.SerializedName

data class Settings(
    @SerializedName("profileName")
    val profileName: String,
    @SerializedName("server")
    val server: String,
    @SerializedName("port")
    val port: String,
    @SerializedName("base")
    val base: String,
    @SerializedName("login")
    val login: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("ssl")
    val ssl: Boolean,
    @SerializedName("disableCheckSSL")
    val disableCheckSSL: Boolean,
    @SerializedName("licenseServer")
    val licenseServer: String,
    @SerializedName("licensePort")
    val licensePort: String,
    @SerializedName("licensePassword")
    val licensePassword: String,
    @SerializedName("licenseLogin")
    val licenseLogin: String
)
