package sitec_it.ru.androidapp.data.models.operations

import com.google.gson.annotations.SerializedName

data class FormAction(
    @SerializedName("Action")
    val Action: String,
    @SerializedName("FormActionArguments")
    val FormActionArguments: List<FormActionArguments>,
    @SerializedName("FormID")
    val FormID: String
)