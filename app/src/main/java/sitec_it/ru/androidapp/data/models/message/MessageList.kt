package sitec_it.ru.androidapp.data.models.message

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "MessageList" )
data class MessageList(
    @PrimaryKey
    @SerializedName("DatabaseID")
    val DatabaseID:String,
    @SerializedName("LastReceived")
    val LastReceived:String,
)
