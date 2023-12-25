package sitec_it.ru.androidapp.data.models.form

import com.google.gson.annotations.SerializedName

data class Argument(
    @SerializedName("Name")
    val Name: String,
    @SerializedName("Value")
    val Value: String
)