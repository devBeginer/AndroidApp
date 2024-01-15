package sitec_it.ru.androidapp.data.models.menu

data class GeneralForm(
    val Elements: List<GeneralFormElement>,
    val FormDescription: String,
    val FormID: String,
    val FormName: String,
    val FormType: String,
    val RemoteFormRequest:List<RemoteFormRequest>
)