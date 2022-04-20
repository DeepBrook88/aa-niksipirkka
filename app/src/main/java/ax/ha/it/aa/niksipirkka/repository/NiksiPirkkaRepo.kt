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

    init {
        /*categories.observeForever{
            it.forEach { item ->
                test[item.getCategory()] = item.getCategoryId()
                println(test[item.getCategory()])
            }
        }*/
        /*Transformations.map(categories) {
            it.forEach { item ->
                test[item.getCategory()] = item.getCategoryId()
                println(item.getCategory())
            }
        }*/
        /*categoryDao.getAllCategoriesNoLive().forEach {
            test[it.getCategory()] = it.getCategoryId()
            println("in loop")
        }*/
        /*categoryDao.getAllCategoriesNoLive().map {
            it.forEach { item ->
                println("in loop")
                test[item.getCategory()] = item.getCategoryId()
            }
            println("in map")
        }*/
    }

    fun insertAdvice(advice: AdviceWithCategory) {
        Thread{
            try {
                var catId = 0 //test[advice.category]
                categoryDao.getAllCategoriesNoLive().toTypedArray().forEach {
                    if (it.getCategory() == advice.category) {
                        catId = it.getCategoryId()
                    }
                }

                /*if (catId == null) {
                    catId = 0
                }*/
                //categories?.value?.toTypedArray()?.forEach { if (it.getCategory() == advice.category) catId = it.getCategoryId()}
                adviceDao.insert(Advice(advice.author,advice.content, catId))
            } catch (ex: SQLiteConstraintException){}
        }.start()
    }
    fun insertCategory(vararg category: Category?) {
        Thread{
            try {
                categoryDao.insert(*category)
                //categories = categoryDao.getAllCategories()
            } catch (ex: SQLiteConstraintException){}
        }.start()
    }
    fun getCategories(): Map<String,Int> {
        return test
    }
}