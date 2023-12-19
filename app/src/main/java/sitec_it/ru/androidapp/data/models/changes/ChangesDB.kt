package sitec_it.ru.androidapp.data.models.changes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Changes" )
data class ChangesDB(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val isWait: Boolean,
    val messageNumber: Int,
    val uniqueDbId: String
)
