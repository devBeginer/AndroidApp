package sitec_it.ru.androidapp.data.models.menu

import com.google.gson.annotations.SerializedName

data class RemoteFormRequest(
    @SerializedName("FormID")
    val FormID:String,
    @SerializedName("Arguments")
    val Arguments:List<String>,
)
