package sitec_it.ru.androidapp.data.models.operations

import com.google.gson.annotations.SerializedName

data class Operation(
    @SerializedName("Name")
    val Name: String,
    @SerializedName("OperationID")
    val OperationID: String,
    @SerializedName("WorkThreads")
    val workThreads: List<WorkThread>
)