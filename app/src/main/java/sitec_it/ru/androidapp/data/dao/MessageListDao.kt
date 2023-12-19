package sitec_it.ru.androidapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import sitec_it.ru.androidapp.data.models.changes.ChangesDB
import sitec_it.ru.androidapp.data.models.message.MessageList

@Dao
interface MessageListDao {
    @Insert
    suspend fun insertMessage(messageList: MessageList): Long

    @Update
    suspend fun updateMessage(messageList: MessageList)

    @Delete
    suspend fun deleteMessage(messageList: MessageList)
    @Query("select * from MessageList where DatabaseID=:currentDatabaseId")
    fun getRecordById(currentDatabaseId: String?): MessageList?


}