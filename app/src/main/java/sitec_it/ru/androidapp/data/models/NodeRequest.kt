package sitec_it.ru.androidapp.data.models

import com.google.gson.annotations.SerializedName

data class NodeRequest(
    @SerializedName("nodeName")
    val nodeName: String
) {
}