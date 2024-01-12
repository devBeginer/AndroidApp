package sitec_it.ru.androidapp.data.models.operations

import com.google.gson.annotations.SerializedName

data class Form(
    @SerializedName("Elements")
    val Elements: List<Element>,
    @SerializedName("FormActions")
    val FormActions: List<FormAction>,
    @SerializedName("FormID")
    val FormID: String,
    @SerializedName("FormName")
    val FormName: String,
    @SerializedName("FormType")
    val FormType: String
)