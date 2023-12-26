package sitec_it.ru.androidapp.data.models.form

import androidx.room.Entity
import androidx.room.ForeignKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Elements", primaryKeys = ["elementID","databaseId"],
    foreignKeys = [ForeignKey(
        entity = FormDB::class,
        parentColumns = arrayOf("formID", "databaseId"),
        childColumns = arrayOf("formID", "databaseId"),
        onDelete = ForeignKey.CASCADE
    )])
data class ElementDB(
    val formID: String,
    val elementID: String,
    val elementName: String,
    val elementType: String,
    val nextField: String,
    val databaseId: String
)
