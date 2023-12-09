package com.example.personalrecordv4.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.personalrecordv4.model.Exercise
import com.example.personalrecordv4.model.ExerciseRepository

class TutorialViewModel : ViewModel(){
    private var _listTutorial: MutableLiveData<MutableList<Exercise>> = MutableLiveData()
    private var _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    private var repo = ExerciseRepository()

    val isLoading: MutableLiveData<Boolean>
        get() = _isLoading

    val listTutorial: MutableLiveData<MutableList<Exercise>>
        get() = _listTutorial

    init {
        _listTutorial = repo._listTutorial
        _isLoading = repo._isLoading
    }

    fun getTutorial(){
        repo.getTutorial()
    }

}