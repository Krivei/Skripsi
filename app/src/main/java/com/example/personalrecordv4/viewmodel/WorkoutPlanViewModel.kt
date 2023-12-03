package com.example.personalrecordv4.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.personalrecordv4.WorkoutPlanCallback
import com.example.personalrecordv4.model.WorkoutPlan
import com.example.personalrecordv4.model.WorkoutPlanRepository

class WorkoutPlanViewModel : ViewModel() {
    private var _listPlan: MutableLiveData<MutableList<WorkoutPlan>> = MutableLiveData(mutableListOf())
    val listPlan: LiveData<MutableList<WorkoutPlan>>
        get() = _listPlan

    private var _spinnerAdapter: MutableLiveData<MutableList<String>> = MutableLiveData(
        mutableListOf())
    val spinnerAdapter: LiveData<MutableList<String>>
        get() = _spinnerAdapter

    private val repo = WorkoutPlanRepository()

    init {
        _listPlan = repo._listWorkoutPlan
        _spinnerAdapter = repo._spinnerAdapter
    }

    fun getWorkoutPlans(callback: WorkoutPlanCallback){
        repo.get(callback)
    }

    fun delete(id: String){

    }


}