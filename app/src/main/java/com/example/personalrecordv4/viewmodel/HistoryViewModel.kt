package com.example.personalrecordv4.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.personalrecordv4.model.History
import com.example.personalrecordv4.model.HistoryDetail
import com.example.personalrecordv4.model.HistoryRepository

class HistoryViewModel : ViewModel() {
    private var repo = HistoryRepository()
    private var _listHistory : MutableLiveData<MutableList<History>> = MutableLiveData()
    private var _listDetail : MutableLiveData<MutableList<HistoryDetail>> = MutableLiveData(mutableListOf())
    private var _historyId : MutableLiveData<String> = MutableLiveData()
    private var _historyDetail: MutableLiveData<String> = MutableLiveData()

    val historyId : MutableLiveData<String>
        get() = _historyId

    val historyDetail : MutableLiveData<String>
        get() = _historyDetail

    val listHistory: MutableLiveData<MutableList<History>>
        get() = _listHistory

    val listDetail : MutableLiveData<MutableList<HistoryDetail>>
        get() = _listDetail

    init {
        _historyId = repo._historyId
        _historyDetail = repo._historyDetail
        _listHistory = repo._listHistory
        _listDetail = repo._listDetail
    }

    fun getHistory(){
        repo.getHistory()
    }

    fun getExerciseHistory(id: MutableList<String>){
        repo.getExerciseHistory(id)
    }
    fun createHistory(name: String,workoutType: String,split: String,firstExercise: String){
        repo.createHistory(name,workoutType,split,firstExercise)
    }
    fun updateHistory(id: String, update: String){
        repo.updateHistory(id,update)
    }
    fun createHistoryDetail(name: String,reps: MutableList<Int>, weight: MutableList<Int>){
        repo.createHistoryDetail(name,reps,weight)
    }

    }