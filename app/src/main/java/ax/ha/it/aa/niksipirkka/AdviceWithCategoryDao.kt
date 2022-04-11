package ax.ha.it.aa.niksipirkka

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

@Dao
interface AdviceWithCategoryDao {
    @Query(
        "SELECT advices.author as author, advices.content as content, categories.category as category " +
        "FROM advices, categories " +
        "WHERE advices.advice_id = categories.category_id"
    )
    fun getAdviceWithCategoryString(): LiveData<List<AdviceWithCategory>>
}