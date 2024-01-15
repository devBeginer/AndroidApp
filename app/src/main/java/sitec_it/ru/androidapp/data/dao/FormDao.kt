package sitec_it.ru.androidapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import sitec_it.ru.androidapp.data.models.menu.db.FormDB

@Dao
interface FormDao {
    @Insert
    suspend fun insertForm(formDB: FormDB): Long

    @Update
    suspend fun updateForm(formDB: FormDB)

    @Delete
    suspend fun deleteForm(formDB: FormDB)

    @Query("select * from Forms where databaseID=:currentDatabaseId")
    fun getRecordById(currentDatabaseId: String?): FormDB?
    @Query("select * from Forms")
    fun getAllForms(): List<FormDB>
}