package ax.ha.it.aa.niksipirkka.repository

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import ax.ha.it.aa.niksipirkka.dao.AdviceDao
import ax.ha.it.aa.niksipirkka.dao.AdviceWithCategoryDao
import ax.ha.it.aa.niksipirkka.dao.CategoryDao
import ax.ha.it.aa.niksipirkka.entities.Advice
import ax.ha.it.aa.niksipirkka.entities.AdviceWithCategory
import ax.ha.it.aa.niksipirkka.entities.Category

class NiksiPirkkaRepo(
    private val adviceDao: AdviceDao,
    private val categoryDao: CategoryDao,
    private val adviceWithCategoryDao: AdviceWithCategoryDao
) {
    val advices: LiveData<List<AdviceWithCategory>> = adviceWithCategoryDao.getAdviceWithCategoryString()
    val categories: LiveData<List<Category>> = categoryDao.getAllCategories()
    val test: MutableMap<String, Int> = mutableMapOf()

    fun insertAdvice(advice: AdviceWithCategory) {
        Thread{
            try {
                adviceDao.insert(Advice(advice.author,advice.content, getCategoryId(advice.category)))
            } catch (ex: SQLiteConstraintException){}
        }.start()
    }
    fun insertCategory(vararg category: Category?) {
        Thread{
            try {
                categoryDao.insert(*category)
            } catch (ex: SQLiteConstraintException){}
        }.start()
    }
    private fun getCategoryId(cat: String): Int {
        return categoryDao.getCategoryIdByString(cat)
    }
}