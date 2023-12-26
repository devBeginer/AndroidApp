package sitec_it.ru.androidapp.data.models.form

import androidx.room.Entity
import androidx.room.ForeignKey
import com.google.gson.annotations.SerializedName
import java.util.UUID

@Entity(
    tableName = "Arguments", primaryKeys = ["actionName", "elementID", "name", "value", "databaseId"],
    foreignKeys = [ForeignKey(
        entity = ActionDB::class,
        parentColumns = arrayOf("actionName", "elementID", "databaseId"),
        childColumns = arrayOf("actionName", "elementID", "databaseId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class ArgumentDB(
    val actionName: String,
    val elementID: String,
    val name: String,
    val value: String,
    val databaseId: String
)
