package ax.ha.it.aa.niksipirkka

import android.app.Application
import androidx.lifecycle.*
import ax.ha.it.aa.niksipirkka.dao.AdviceDao
import ax.ha.it.aa.niksipirkka.dao.AdviceWithCategoryDao
import ax.ha.it.aa.niksipirkka.dao.CategoryDao
import ax.ha.it.aa.niksipirkka.entities.Advice
import ax.ha.it.aa.niksipirkka.entities.AdviceWithCategory
import ax.ha.it.aa.niksipirkka.entities.Category
import ax.ha.it.aa.niksipirkka.repository.NiksiPirkkaRepo
import kotlinx.coroutines.launch

class MyViewModel(application: Application, savedStateHandle: SavedStateHandle) : AndroidViewModel(application) {
    private val advices: LiveData<List<AdviceWithCategory>>
    private val categories: LiveData<List<Category>>

    private val catDao: CategoryDao = AdviceDatabase.getInstance(application.applicationContext)!!.categoryDao()
    private val adviceDao: AdviceDao = AdviceDatabase.getInstance(application.applicationContext)!!.adviceDao()
    private val adviceWithCategoryDao: AdviceWithCategoryDao = AdviceDatabase.getInstance(application.applicationContext)!!.adviceWithCat()
    
    private val repository: NiksiPirkkaRepo = NiksiPirkkaRepo(adviceDao,catDao,adviceWithCategoryDao)

    init {
        advices = repository.advices
        categories = repository.categories
    }
    fun getAdvices(): LiveData<List<AdviceWithCategory>> {
        return advices
    }
    fun addAdvice(advice: AdviceWithCategory) = viewModelScope.launch {
        repository.insertAdvice(advice)
    }

    fun getCategories(): LiveData<List<Category>> {
        return categories
    }
    fun addCategory(vararg category: Category?) {
        repository.insertCategory(*category)
    }
    fun getAllCategories(): Map<String,Int> {
        return repository.getCategories()
    }

}
