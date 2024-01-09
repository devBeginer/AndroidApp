package sitec_it.ru.androidapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import sitec_it.ru.androidapp.data.models.user.User
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * FROM User WHERE login == :login")
    suspend fun getUserByLogin(login: String): User?

    @Query("SELECT * FROM User WHERE code =:code")
    suspend fun getUserByCode(code: String): User?

    @Query("SELECT * FROM User WHERE databaseID =:databaseId")
    suspend fun getUsersByDbId(databaseId: String): List<User>

    @Query("SELECT * FROM User")
    suspend fun getAllUsers(): List<User>
}