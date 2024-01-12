package sitec_it.ru.androidapp.data.models.operations

import com.google.gson.annotations.SerializedName

data class WorkThread(
    @SerializedName("Name")
    val Name: String,
    @SerializedName("ThreadID")
    val ThreadID: String
)