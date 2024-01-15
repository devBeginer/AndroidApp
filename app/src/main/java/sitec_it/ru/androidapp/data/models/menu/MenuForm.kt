package sitec_it.ru.androidapp.data.models.menu

import com.google.gson.annotations.SerializedName

data class MenuForm(
    @SerializedName("Elements")
    val Elements: List<MenuFormElement>,
    @SerializedName("FormID")
    val FormID: String,
    @SerializedName("FormName")
    val FormName: String,
    @SerializedName("FormType")
    val FormType: String
)