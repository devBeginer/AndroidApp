package sitec_it.ru.androidapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import sitec_it.ru.androidapp.data.models.form.ArgumentDB

@Dao
interface ArgumentDao {
    @Insert
    suspend fun insertArgument(argumentDB: ArgumentDB): Long

    @Update
    suspend fun updateArgument(argumentDB: ArgumentDB)

    @Delete
    suspend fun deleteArgument(argumentDB: ArgumentDB)
    @Query("select * from Arguments where databaseID=:currentDatabaseId AND actionName=:actionName")
    fun getRecordByAction(currentDatabaseId: String?, actionName: String ): List<ArgumentDB>
    @Query("select * from Arguments where databaseID=:currentDatabaseId AND actionName=:actionName AND name=:name AND value=:value AND elementID=:elementID")
    fun getRecordById(currentDatabaseId: String?, actionName: String, name: String, value: String, elementID: String): ArgumentDB?
}