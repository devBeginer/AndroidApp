package sitec_it.ru.androidapp.data.models.menu.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import sitec_it.ru.androidapp.data.models.menu.Form
import java.util.Date

@Entity(tableName = "Forms")
data class FormDB(
    val json: Form,
    @PrimaryKey
    val databaseId: String,
    val date: Date
)


