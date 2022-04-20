package ax.ha.it.aa.niksipirkka.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ax.ha.it.aa.niksipirkka.entities.Category

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

    @Query("SELECT * FROM categories")
    fun getAllCategoriesNoLive(): List<Category>

    @Query("SELECT category_id FROM categories WHERE category = :category")
    fun getCategoryIdByString(category: String): Int
}
