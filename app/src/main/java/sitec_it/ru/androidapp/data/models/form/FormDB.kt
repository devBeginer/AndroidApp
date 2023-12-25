package sitec_it.ru.androidapp.data.models.form

import com.google.gson.annotations.SerializedName

data class FormDB(
    val Elements: List<Element>,
    val FormID: String,
    val FormName: String
)
