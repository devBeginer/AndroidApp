package sitec_it.ru.androidapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import sitec_it.ru.androidapp.data.models.ProfileLicense

@Dao
interface ProfileLicenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfileLicense(profileLicense: ProfileLicense): Long

    @Update
    suspend fun updateProfileLicense(profileLicense: ProfileLicense)

    @Delete
    suspend fun deleteProfileLicense(profileLicense: ProfileLicense)

    @Query("SELECT * FROM ProfileLicense WHERE profile == :profile")
    suspend fun getProfileLicenseByProfile(profile: Long): ProfileLicense?

    @Query("SELECT * FROM ProfileLicense WHERE id == :id")
    suspend fun getProfileLicenseById(id: Long): ProfileLicense?
}