package sitec_it.ru.androidapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import sitec_it.ru.androidapp.data.models.node.Node

@Dao
interface NodeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNode(node: Node)

    @Update
    suspend fun updateNode(node: Node)

    @Delete
    suspend fun deleteNode(node: Node)
}