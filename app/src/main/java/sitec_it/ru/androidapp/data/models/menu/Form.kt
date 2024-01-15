package sitec_it.ru.androidapp.data.models.menu

import com.google.gson.annotations.SerializedName

data class Form(
    @SerializedName("MenuForms")
    val MenuForms: List<MenuForm>,
    @SerializedName("GeneralForms")
    val GeneralForms: List<GeneralForm>
)