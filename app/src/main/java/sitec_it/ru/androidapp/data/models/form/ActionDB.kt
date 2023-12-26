package sitec_it.ru.androidapp.data.models.form

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "Actions", primaryKeys = ["actionName", "elementID", "databaseId"],
    foreignKeys = [ForeignKey(
        entity = ElementDB::class,
        parentColumns = arrayOf("elementID", "databaseId"),
        childColumns = arrayOf("elementID", "databaseId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class ActionDB(
    val actionName: String,
    val elementID: String,
    val databaseId: String
)
