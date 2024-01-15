package sitec_it.ru.androidapp.data.models.menu

import com.google.gson.annotations.SerializedName

data class SubMenuAction(
    @SerializedName("Action")
    val Action: String,
    @SerializedName("Argument")
    val Argument: String,
    @SerializedName("Value")
    val Value: String,

)