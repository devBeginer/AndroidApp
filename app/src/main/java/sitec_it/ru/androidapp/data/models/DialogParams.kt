package sitec_it.ru.androidapp.data.models

data class DialogParams(
    val textMessage: String,
    val title: String,
    val positiveBtn: String,
    val onPositive:(()->Unit)? = null,
    val negativeButton: String? = null,
    val onNegative: (() -> Unit)? = null,
    val neutralButton: String? = null,
    val onNeutral: (() -> Unit)? = null
)
