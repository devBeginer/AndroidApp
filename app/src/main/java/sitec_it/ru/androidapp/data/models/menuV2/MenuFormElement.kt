package sitec_it.ru.androidapp.data.models.menuV2

data class MenuFormElement(
    val Actions: Any,
    val ElementType: String,
    val SubMenu: List<SubMenu>,
    val Text: String
)