package sitec_it.ru.androidapp.data.models.changes

import com.google.gson.annotations.SerializedName

data class Changes(
    @SerializedName("IsWait")
    val IsWait: Boolean,
    @SerializedName("MessageNumber")
    val MessageNumber: Int,
    @SerializedName("Organization")
    val Organization: List<Organization>,
    @SerializedName("UniqueDbId")
    val UniqueDbId: String
)