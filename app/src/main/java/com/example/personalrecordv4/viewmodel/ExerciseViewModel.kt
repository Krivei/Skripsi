package com.example.personalrecordv4.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.personalrecordv4.model.Exercise
import com.example.personalrecordv4.model.ExerciseRepository

class ExerciseViewModel : ViewModel() {
    private var _listExercise: MutableLiveData<MutableList<Exercise>?> = MutableLiveData()
    private var repo = ExerciseRepository()
    private var isLoading: MutableLiveData<Boolean> = MutableLiveData()
    private var _pickExercise : MutableLiveData<MutableList<Exercise>> = MutableLiveData(mutableListOf())
    private var _loopNumber : MutableLiveData<Int> = MutableLiveData()
    private var _isDone : MutableLiveData<Boolean> = MutableLiveData(false)

    val isDone : MutableLiveData<Boolean>
        get() = _isDone

    val pickExercise: MutableLiveData<MutableList<Exercise>>
        get() = _pickExercise

    val loopNumber : MutableLiveData<Int>
        get() = _loopNumber
    val isLoadingData: MutableLiveData<Boolean>
        get() = isLoading

    val listExercise: MutableLiveData<MutableList<Exercise>?>
        get() = _listExercise
    var listId: MutableLiveData<MutableList<String>>
        get() = repo._listId

    init{
        _isDone = repo._isDone
        _listExercise = repo._listExercise
        listId = repo._listId
        isLoading = repo._isLoading
        _pickExercise = repo._pickExercise
        _loopNumber = repo._loopNumber
    }

    fun getExercise(mutableList: MutableList<String>){
        repo.getExercise(mutableList)
    }

    fun assignTemplateExercise(mutableList: MutableList<String>, rep: Int, set: Int, splitId: String,splitNumber: Int, loopNumber: Int){
        repo.assignTemplateExercise(mutableList, rep, set, splitId, splitNumber, loopNumber)
    }

    fun checkExercise(rep: Int, set: Int){
        repo.checkExercise(rep,set)
    }

    fun getExerciseByNumbers(rep: Int, set: Int){
        repo.getExerciseByNumbers(rep,set)
    }
    fun pickExercise(nama: String, rep: Int, set: Int, splitId: String){
        repo.pickExercise(nama,rep,set,splitId)
    }
    fun removeExercise(nama: String, rep: Int, set: Int, splitId: String){
        repo.removeExercise(nama,rep,set,splitId)
    }
}