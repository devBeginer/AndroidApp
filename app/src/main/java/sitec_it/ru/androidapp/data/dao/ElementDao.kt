package sitec_it.ru.androidapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import sitec_it.ru.androidapp.data.models.form.ElementDB

@Dao
interface ElementDao {
    @Insert
    suspend fun insertElement(elementDB: ElementDB): Long

    @Update
    suspend fun updateElement(elementDB: ElementDB)

    @Delete
    suspend fun deleteElement(elementDB: ElementDB)
    @Query("select * from Elements where databaseID=:currentDatabaseId AND formId=:formId")
    fun getRecordByForm(currentDatabaseId: String?, formId: String ): List<ElementDB>
    @Query("select * from Elements where databaseID=:currentDatabaseId AND elementId=:elementId")
    fun getRecordById(currentDatabaseId: String?, elementId: String ): ElementDB?
}