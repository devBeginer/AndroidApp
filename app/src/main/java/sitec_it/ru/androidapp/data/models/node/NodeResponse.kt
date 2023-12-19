package sitec_it.ru.androidapp.data.models.node

import com.google.gson.annotations.SerializedName

data class NodeResponse(
    @SerializedName("DatabaseID")
    val nodeId: String,
    @SerializedName("Prefix")
    val prefix: String
){
    override fun toString(): String {
        return "{ nodeId: $nodeId prefix: $prefix}"
    }
}
