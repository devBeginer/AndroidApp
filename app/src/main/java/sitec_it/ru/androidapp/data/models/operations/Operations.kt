package sitec_it.ru.androidapp.data.models.operations

import com.google.gson.annotations.SerializedName

data class Operations(
    @SerializedName("currentOperation")
    val currentOperation: String?,
    @SerializedName("forms")
    val forms: List<Form>,
    @SerializedName("operations")
    val operations: List<Operation>
)