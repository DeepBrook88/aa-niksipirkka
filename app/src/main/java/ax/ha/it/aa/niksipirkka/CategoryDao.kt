package ax.ha.it.aa.niksipirkka

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CategoryDao {
    @Insert
    fun insert(vararg categories: Category?)

    @Update
    fun update(vararg categories: Category?)

    @Query("DELETE FROM categories")
    fun deleteAllCategories()

    @Query("SELECT * FROM categories")
    fun getAllCategories(): LiveData<List<Category>>
}
