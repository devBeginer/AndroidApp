package sitec_it.ru.androidapp.data.models.form

import com.google.gson.annotations.SerializedName

data class ElementDB(
    val formId: String,
    val Actions: String,
    val Arguments: List<Argument>,
    val ElementID: String,
    val ElementName: String,
    val ElementType: String,
    val NextField: String
)
