package sitec_it.ru.androidapp.data.models.menu

import com.google.gson.annotations.SerializedName

data class Action(
    @SerializedName("Save")
    val Save: Save,
    @SerializedName("goToForm")
    val goToForm: String
)