package sitec_it.ru.androidapp.data.models.form

import com.google.gson.annotations.SerializedName

data class Form(
    @SerializedName("Elements")
    val Elements: List<Element>,
    @SerializedName("FormID")
    val FormID: String,
    @SerializedName("FormName")
    val FormName: String
)