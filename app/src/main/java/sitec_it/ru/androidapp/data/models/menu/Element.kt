package sitec_it.ru.androidapp.data.models.menu

import com.google.gson.annotations.SerializedName

data class Element(
    @SerializedName("ElementType")
    val ElementType: String,
    @SerializedName("SubMenu")
    val SubMenu: List<SubMenu>,
    @SerializedName("Text")
    val Text: String
)