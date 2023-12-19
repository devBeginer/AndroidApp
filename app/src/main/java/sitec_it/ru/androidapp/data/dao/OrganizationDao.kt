package sitec_it.ru.androidapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import sitec_it.ru.androidapp.data.models.changes.ChangesDB
import sitec_it.ru.androidapp.data.models.changes.OrganizationDB

@Dao
interface OrganizationDao {
    @Insert
    suspend fun insertOrganization(organizationDB: OrganizationDB)

    @Update
    suspend fun updateOrganization(organizationDB: OrganizationDB)

    @Delete
    suspend fun deleteOrganization(organizationDB: OrganizationDB)

    @Query("SELECT * FROM Organization WHERE id == :id")
    suspend fun getOrganizationById(id: Long): OrganizationDB?

    @Query("SELECT * FROM Organization WHERE change == :change")
    suspend fun getOrganizationByChange(change: Long): List<OrganizationDB>
}