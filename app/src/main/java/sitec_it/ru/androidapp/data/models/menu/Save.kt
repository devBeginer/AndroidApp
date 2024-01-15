package sitec_it.ru.androidapp.data.models.menu

import com.google.gson.annotations.SerializedName

data class Save(
    @SerializedName("Type")
    val Type: String,
    @SerializedName("Value")
    val Value: String
)