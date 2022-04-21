package ax.ha.it.aa.niksipirkka

import android.content.Context
import android.util.AttributeSet
import androidx.lifecycle.MutableLiveData
import androidx.preference.ListPreference
import ax.ha.it.aa.niksipirkka.entities.Category

class SelectorTestPreference(context: Context, attrs: AttributeSet): ListPreference(context, attrs) {
    private val catDao = AdviceDatabase.getInstance(context)!!.categoryDao()
    private var categories: MutableLiveData<List<Category>> = MutableLiveData()
    init {
        val names = mutableListOf<String>()
        val ids = mutableListOf<String>()
        catDao.getAllCategories().observeForever{
            categories.value = it
            categories.value?.forEachIndexed {idx, item -> names.add(item.getCategory()); ids.add(idx.toString())}
            entries = names.toTypedArray()
            entryValues = ids.toTypedArray()
            setDefaultValue(entries[0])
            summaryProvider = SimpleSummaryProvider.getInstance()
        }
    }
}
