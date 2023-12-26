package sitec_it.ru.androidapp.data.models.form

import com.google.gson.annotations.SerializedName

data class Element(
    @SerializedName("FormID")
    val FormID: String,
    @SerializedName("Actions")
    val Actions: String,
    @SerializedName("Arguments")
    val Arguments: List<Argument>,
    @SerializedName("ElementID")
    val ElementID: String,
    @SerializedName("ElementName")
    val ElementName: String,
    @SerializedName("ElementType")
    val ElementType: String,
    @SerializedName("NextFieldID")
    val NextFieldID: String
)