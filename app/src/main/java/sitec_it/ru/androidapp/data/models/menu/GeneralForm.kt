package sitec_it.ru.androidapp.data.models.menu

import com.google.gson.annotations.SerializedName

data class GeneralForm(
    @SerializedName("Elements")
    val Elements: List<GeneralFormElement>,
    @SerializedName("FormDescription")
    val FormDescription: String,
    @SerializedName("FormID")
    val FormID: String,
    @SerializedName("FormName")
    val FormName: String,
    @SerializedName("FormType")
    val FormType: String,
    @SerializedName("RemoteFormRequest")
    val RemoteFormRequest:List<RemoteFormRequest>
)