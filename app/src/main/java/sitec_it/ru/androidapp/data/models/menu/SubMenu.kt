package sitec_it.ru.androidapp.data.models.menu

import com.google.gson.annotations.SerializedName

data class SubMenu(
    @SerializedName("Actions")
    val Actions: List<SubMenuAction>,
    @SerializedName("ElementType")
    val ElementType: String,
    @SerializedName("Text")
    val Text: String
)