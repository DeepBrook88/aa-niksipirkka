package ax.ha.it.aa.niksipirkka

import android.app.Application
import androidx.lifecycle.*

class MyViewModel(application: Application, savedStateHandle: SavedStateHandle) : AndroidViewModel(application) {
    private val advices: MutableLiveData<List<AdviceWithCategory>>
    private lateinit var categories: MutableLiveData<List<Category>>

    private val catDao: CategoryDao = AdviceDatabase.getInstance(application.applicationContext)!!.categoryDao()
    private val adviceDao: AdviceDao = AdviceDatabase.getInstance(application.applicationContext)!!.adviceDao()
    private val adviceWithCategoryDao: AdviceWithCategoryDao = AdviceDatabase.getInstance(application.applicationContext)!!.adviceWithCat()

    init {
        if (savedStateHandle.contains("advices")) {
            advices = savedStateHandle.getLiveData("advices")
        } else {
            advices = MutableLiveData()
            adviceWithCategoryDao.getAdviceWithCategoryString().observeForever{
                val advCats: List<AdviceWithCategory> = it
                advices.value = advCats
                savedStateHandle.set("advices", advCats)
            }
        }
        if (savedStateHandle.contains("categories")) {
            categories = savedStateHandle.getLiveData("categories")
        } else {
            /*Thread{
                catDao.deleteAllCategories()
                catDao.insert(Category("Lifestyle"),Category("Technology"),Category("Miscellaneous"))
            }.start()*/

            catDao.getAllCategories().observeForever{
                val categoryList: List<Category> = it
                categories = MutableLiveData()
                categories.value = categoryList
                savedStateHandle.set("categories", categoryList)
            }
        }
    }
    fun getAdvices(): LiveData<List<AdviceWithCategory>> {
        return advices
    }
    fun addAdvice(advice: Advice) {
        Thread{
            adviceDao.insert(advice)
        }.start()
        /*val adviceList: MutableList<Advice> = mutableListOf()
        advices.value?.let { adviceList.addAll(it) }
        adviceList.add(advice)
        advices.value = adviceList*/
    }
    fun getCategories(): LiveData<List<Category>> {
        return categories
    }
    fun addCategory(category: Category) {
        Thread{
            catDao.insert(category)
        }.start()
        /*val categoryList: MutableList<Category> = mutableListOf()
        categories.value?.let { categoryList.addAll(it) }
        categoryList.add(category)
        categories.value = categoryList*/
    }
}
