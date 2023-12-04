package sitec_it.ru.androidapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import sitec_it.ru.androidapp.data.models.Profile

@Dao
interface ProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: Profile)

    @Update
    suspend fun updateProfile(profile: Profile)

    @Delete
    suspend fun deleteProfile(profile: Profile)

    @Query("SELECT * FROM Profile WHERE name == :name")
    suspend fun getProfileByName(name: String): Profile?

    @Query("SELECT * FROM Profile WHERE id == :id")
    suspend fun getProfileById(id: Int): Profile?
}