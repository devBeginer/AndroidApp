package sitec_it.ru.androidapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import sitec_it.ru.androidapp.data.models.form.ActionDB
import sitec_it.ru.androidapp.data.models.form.FormDB

@Dao
interface ActionDao {
    @Insert
    suspend fun insertAction(actionDB: ActionDB): Long

    @Update
    suspend fun updateAction(actionDB: ActionDB)

    @Delete
    suspend fun deleteAction(actionDB: ActionDB)
    @Query("select * from Actions where databaseID=:currentDatabaseId AND elementID=:elementID AND actionName=:action")
    fun getRecordById(currentDatabaseId: String?, action: String, elementID: String): ActionDB?
    @Query("select * from Actions where databaseID=:currentDatabaseId AND elementId=:elementId")
    fun getActionsByElement(currentDatabaseId: String?, elementId: String): List<ActionDB>
}