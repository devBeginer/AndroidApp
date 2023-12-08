package sitec_it.ru.androidapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Node(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nodeId: String,
    val prefix: String
)
