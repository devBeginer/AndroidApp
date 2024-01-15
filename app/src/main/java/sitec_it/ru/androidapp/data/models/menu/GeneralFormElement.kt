package sitec_it.ru.androidapp.data.models.menu

import com.google.gson.annotations.SerializedName

data class GeneralFormElement(
    @SerializedName("Actions")
    val Actions: List<GeneralFormAction>,
    @SerializedName("ElementType")
    val ElementType: String,
    @SerializedName("Text")
    val Text: String
)