package sitec_it.ru.androidapp.data.models.menu

import com.google.gson.annotations.SerializedName

data class MenuForm(
    @SerializedName("forms")
    val forms: List<Form>
)