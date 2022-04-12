package ax.ha.it.aa.niksipirkka

import android.content.Context
import android.util.AttributeSet
import androidx.lifecycle.MutableLiveData
import androidx.preference.ListPreference

class SelectorTestPreference(context: Context, attrs: AttributeSet): ListPreference(context, attrs) {
    private val test = AdviceDatabase.getInstance(context)!!.categoryDao()
    private val categories: MutableLiveData<List<Category>> = MutableLiveData()
    init {
        //val map: MutableMap<String, String> = mutableMapOf()
        val names = mutableListOf<String>()
        val ids = mutableListOf<String>()
        test.getAllCategories().observeForever{
            categories.value = it
            categories.value!!.toTypedArray().forEachIndexed {idx, item -> names.add(item.getCategory()); ids.add(idx.toString())}
            entries = names.toTypedArray()
            entryValues = ids.toTypedArray()
            setDefaultValue(entries[0])
            summaryProvider = SimpleSummaryProvider.getInstance()
        }
    }
}