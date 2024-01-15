package sitec_it.ru.androidapp.data.models.menu

data class MenuForm(
    val Elements: List<MenuFormElement>,
    val FormID: String,
    val FormName: String,
    val FormType: String
)