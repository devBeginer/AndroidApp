package sitec_it.ru.androidapp.data.models.menu

data class MenuFormElement(
    val Actions: String?,
    val ElementType: String,
    val SubMenu: List<SubMenu>,
    val Text: String
)