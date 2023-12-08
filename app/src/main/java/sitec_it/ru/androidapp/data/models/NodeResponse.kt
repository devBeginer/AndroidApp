package sitec_it.ru.androidapp.data.models

import com.google.gson.annotations.SerializedName

data class NodeResponse(
    @SerializedName("NodeID")
    val nodeId: String,
    @SerializedName("Prefix")
    val prefix: String
){
    override fun toString(): String {
        return "{ nodeId: $nodeId prefix: $prefix}"
    }
}
