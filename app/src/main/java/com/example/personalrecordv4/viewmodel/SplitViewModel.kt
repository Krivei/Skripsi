package com.example.personalrecordv4.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.personalrecordv4.model.Split
import com.example.personalrecordv4.model.SplitRepository
import com.example.personalrecordv4.model.WorkoutPlan

class SplitViewModel: ViewModel() {
    private var _listSplit: MutableLiveData<MutableList<Split>?> = MutableLiveData()

    private var repo = SplitRepository()
    private var isLoading: MutableLiveData<Boolean> = MutableLiveData()

    val isLoadingData: MutableLiveData<Boolean>
        get() = isLoading

    val splitData: MutableLiveData<MutableList<Split>?>
        get() = _listSplit
    init {
        _listSplit = repo._listSplit
        isLoading = repo.isLoading
    }

    fun getSplit(mutableList: MutableList<String>) {
        repo.getSplit(mutableList)
    }

}
