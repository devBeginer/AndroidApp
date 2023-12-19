package sitec_it.ru.androidapp.data.models.node

import com.google.gson.annotations.SerializedName

data class NodeRequest(
    @SerializedName("Name")
    val nodeName: String
) {
}