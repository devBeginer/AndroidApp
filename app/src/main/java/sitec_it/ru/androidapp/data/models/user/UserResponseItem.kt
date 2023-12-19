package sitec_it.ru.androidapp.data.models.user

import com.google.gson.annotations.SerializedName

data class UserResponseItem(
    @SerializedName("Code")
    val code: String,
    @SerializedName("Login")
    val login: String,
    @SerializedName("Name")
    val name: String/*,
    @SerializedName("Password")
    val password: String*/
)