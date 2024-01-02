package com.example.personalrecordv4.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.personalrecordv4.model.Split
import com.example.personalrecordv4.model.SplitRepository
import com.example.personalrecordv4.model.WeightLog
import com.example.personalrecordv4.model.WorkoutPlan
import com.google.firebase.firestore.FieldValue

class SplitViewModel: ViewModel() {
    private var _listSplit: MutableLiveData<MutableList<Split>?> = MutableLiveData()
    private var repo = SplitRepository()
    private var isLoading: MutableLiveData<Boolean> = MutableLiveData()
    private var _newSplitList: MutableLiveData<MutableList<String>> = MutableLiveData()
    private var _singleSplit : MutableLiveData<Split?> = MutableLiveData()
    private var _templateList : MutableLiveData<MutableList<String>> = MutableLiveData(mutableListOf())

    val templateList : MutableLiveData<MutableList<String>>
        get() = _templateList

    val singleSplit : MutableLiveData<Split?>
        get() = _singleSplit

    val isLoadingData: MutableLiveData<Boolean>
        get() = isLoading

    val splitData: MutableLiveData<MutableList<Split>?>
        get() = _listSplit

    var newaddedid: String
        get() = repo.newaddedid

    val newSplitList: MutableLiveData<MutableList<String>>
        get() = _newSplitList

    init {
        _newSplitList = repo._newSplitList
        _listSplit = repo._listSplit
        isLoading = repo.isLoading
        newaddedid = repo.newaddedid
        _singleSplit = repo._singleSplit
        _templateList = repo._templateList
    }

    fun getSplit(mutableList: MutableList<String>) {
        repo.getSplit(mutableList)
    }
    fun removeExercise(splitId: String, exerciseIdToRemove: String) {
        repo.removeExercise(splitId,exerciseIdToRemove)
    }

    fun addSplit(split: Split) {
        repo.addSplit(split)
    }

    fun assignSplit(planId : String, splitNumber: Int){
        repo.assignSplit(planId, splitNumber)
    }

    fun makeSplitTemplate(planId: String, splitNumber: Int, exampleNumber: Int, reps: Int, sets: Int){
        repo.makeSplitTemplate(planId,splitNumber,exampleNumber,reps,sets)
    }

    fun getSingleSplit(x: String){
        repo.getSingleSplit(x)
    }

    fun assignExercise(splitId: String, exerciseId: String){
        repo.assignExercise(splitId,exerciseId)
    }

    fun removeSplit(list: MutableList<String>){
        repo.removeSplit(list)
    }

}