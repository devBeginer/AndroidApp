package sitec_it.ru.androidapp.data.models.changes

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(tableName = "Organization", primaryKeys = ["code","databaseId"])
data class OrganizationDB(
    val code: String,
    val name: String,
    val databaseId: String
)
