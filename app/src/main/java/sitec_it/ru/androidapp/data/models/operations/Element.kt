package sitec_it.ru.androidapp.data.models.operations

data class Element(
    val Actions: List<Action>,
    val ElementID: String,
    val ElementName: String,
    val ElementType: String,
    val FormID: String,
    val NextFieldID: String
)