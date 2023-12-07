package com.example.personalrecordv4.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.personalrecordv4.model.WeightLog
import com.example.personalrecordv4.model.WeightLogRepository

class WeightLogViewModel : ViewModel() {
    private var _listWeightLog: MutableLiveData<MutableList<WeightLog>> = MutableLiveData()
    private var _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    private var repo = WeightLogRepository()

    val isLoading: MutableLiveData<Boolean>
        get() = _isLoading

    val listWeightLog: MutableLiveData<MutableList<WeightLog>>
        get() = _listWeightLog

    init {
        _listWeightLog = repo._listWeightLog
        _isLoading = repo._isLoading
    }

    fun getWeightLog(){
        repo.getWeightLog()
    }
}
