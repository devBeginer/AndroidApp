package sitec_it.ru.androidapp.data.models.form

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Forms", primaryKeys = ["formID","databaseId"])
data class FormDB(
    val formID: String,
    val formName: String,
    val databaseId: String
)
