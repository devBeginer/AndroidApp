package sitec_it.ru.androidapp.data.dao

import androidx.room.Dao
import androidx.room.Query
import sitec_it.ru.androidapp.data.models.message.MessageList

@Dao
interface MessageListDao {

    @Query("select * from MessageList where DatabaseID=:currentDatabaseId")
    fun getRecordById(currentDatabaseId: String?): MessageList


}