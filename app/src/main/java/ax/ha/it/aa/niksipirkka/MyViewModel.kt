package ax.ha.it.aa.niksipirkka

import android.app.Application
import androidx.lifecycle.*

class MyViewModel(application: Application, savedStateHandle: SavedStateHandle) : AndroidViewModel(application) {
    private val advices: MutableLiveData<List<AdviceWithCategory>> = MutableLiveData()
    private val categories: MutableLiveData<List<Category>> = MutableLiveData()

    private val catDao: CategoryDao = AdviceDatabase.getInstance(application.applicationContext)!!.categoryDao()
    private val adviceDao: AdviceDao = AdviceDatabase.getInstance(application.applicationContext)!!.adviceDao()
    private val adviceWithCategoryDao: AdviceWithCategoryDao = AdviceDatabase.getInstance(application.applicationContext)!!.adviceWithCat()

    private val adviceObserver = Observer<List<AdviceWithCategory>> {
        advices.value = it
    }

    init {
        adviceWithCategoryDao.getAdviceWithCategoryString().observeForever(adviceObserver)
        /*Thread{
            catDao.deleteAllCategories()
            catDao.insert(Category("Lifestyle"),Category("Technology"),Category("Miscellaneous"))
        }.start()*/

        catDao.getAllCategories().observeForever{
            categories.value = it
        }

    }
    fun getAdvices(): LiveData<List<AdviceWithCategory>> {
        return advices
    }
    fun addAdvice(advice: Advice) {
        Thread{
            adviceDao.insert(advice)
        }.start()
    }
    fun getCategories(): LiveData<List<Category>> {
        return categories
    }
    fun addCategory(category: Category) {
        Thread{
            catDao.insert(category)
        }.start()
    }

    override fun onCleared() {
        adviceWithCategoryDao.getAdviceWithCategoryString().removeObserver(adviceObserver)
        super.onCleared()
    }
}
