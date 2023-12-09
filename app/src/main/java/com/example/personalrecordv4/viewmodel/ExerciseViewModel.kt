package com.example.personalrecordv4.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.personalrecordv4.model.Exercise
import com.example.personalrecordv4.model.ExerciseRepository

class ExerciseViewModel : ViewModel() {
    private var _listExercise: MutableLiveData<MutableList<Exercise>?> = MutableLiveData()
    private var repo = ExerciseRepository()
    private var isLoading: MutableLiveData<Boolean> = MutableLiveData()

    val isLoadingData: MutableLiveData<Boolean>
        get() = isLoading

    val listExercise: MutableLiveData<MutableList<Exercise>?>
        get() = _listExercise

    init{
        _listExercise = repo._listExercise
        isLoading = repo._isLoading
    }

    fun getExercise(mutableList: MutableList<String>){
        repo.getExercise(mutableList)
    }
}