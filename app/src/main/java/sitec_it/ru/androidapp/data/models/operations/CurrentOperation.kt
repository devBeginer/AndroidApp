package sitec_it.ru.androidapp.data.models.operations

data class CurrentOperation(
    val Name: String,
    val OperationID: String,
    val workThreads: List<WorkThread>
)