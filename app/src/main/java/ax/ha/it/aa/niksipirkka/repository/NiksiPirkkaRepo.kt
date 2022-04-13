package ax.ha.it.aa.niksipirkka.repository

import androidx.lifecycle.LiveData
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

    fun insertAdvice(advice: Advice) {
        Thread{
            adviceDao.insert(advice)
        }.start()
    }
    fun insertCategory(vararg category: Category?) {
        Thread{
            categoryDao.insert(*category)
        }.start()
    }
}