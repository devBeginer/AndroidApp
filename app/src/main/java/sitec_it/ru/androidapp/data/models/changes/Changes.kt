package sitec_it.ru.androidapp.data.models.changes

data class Changes(
    val IsWait: Boolean,
    val MessageNumber: Int,
    val Organization: List<Organization>,
    val UniqueDbId: String
)