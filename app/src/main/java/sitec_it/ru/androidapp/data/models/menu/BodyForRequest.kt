package sitec_it.ru.androidapp.data.models.menu

import com.google.gson.annotations.SerializedName

data class BodyForRequest(
    @SerializedName("FormId")
    val formId:String,
    @SerializedName("Arguments")
    val Arguments:List<String>
)
