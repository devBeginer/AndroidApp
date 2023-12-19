package sitec_it.ru.androidapp.data.models.changes

import com.google.gson.annotations.SerializedName

data class Organization(
    @SerializedName("Code")
    val Code: String,
    @SerializedName("Name")
    val Name: String
)