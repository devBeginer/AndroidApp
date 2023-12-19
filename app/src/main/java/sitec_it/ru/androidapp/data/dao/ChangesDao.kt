package sitec_it.ru.androidapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import sitec_it.ru.androidapp.data.models.changes.ChangesDB

@Dao
interface ChangesDao {
    @Insert
    suspend fun insertChange(changesDB: ChangesDB): Long

    @Update
    suspend fun updateChange(changesDB: ChangesDB)

    @Delete
    suspend fun deleteChange(changesDB: ChangesDB)

    @Query("SELECT * FROM Changes WHERE id == :id")
    suspend fun getChangesById(id: Long): ChangesDB?

    @Query("SELECT * FROM Changes WHERE UniqueDbId == :uniqueDbId")
    suspend fun getChangesByDbId(uniqueDbId: String): ChangesDB?
}