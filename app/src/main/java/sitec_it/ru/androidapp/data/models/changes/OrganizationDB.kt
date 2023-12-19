package sitec_it.ru.androidapp.data.models.changes

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    foreignKeys = [ForeignKey(
        entity = ChangesDB::class,
        parentColumns = ["id"], childColumns = ["change"], onDelete = ForeignKey.CASCADE
    )], tableName = "Organization"
)
data class OrganizationDB(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val code: String,
    val name: String,
    val change: Long
)
