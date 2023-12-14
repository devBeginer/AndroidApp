package sitec_it.ru.androidapp.data.models

import com.google.gson.annotations.SerializedName

data class UserX(
    @SerializedName("Code")
    val code: String,
    @SerializedName("Login")
    val login: String,
    @SerializedName("Name")
    val name: String,
    @SerializedName("Password")
    val password: String
)