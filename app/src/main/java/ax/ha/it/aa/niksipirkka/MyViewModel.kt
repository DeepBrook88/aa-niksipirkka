package ax.ha.it.aa.niksipirkka

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class MyViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private var advices: MutableLiveData<List<Advice>>

    init {
        if (savedStateHandle.contains("advices")) {
            advices = savedStateHandle.getLiveData("advices")
        } else {
            val adviceList: List<Advice> = ArrayList()
            advices = MutableLiveData()
            advices.value = adviceList
            savedStateHandle.set("advices", adviceList)
        }
    }
    fun getAdvices(): LiveData<List<Advice>> {
        return advices
    }
    fun addAdvice(advice: Advice) {
        val adviceList: MutableList<Advice> = mutableListOf()
        advices.value?.let { adviceList.addAll(it) }
        adviceList.add(advice)
        advices.value = adviceList
    }
}