package com.example.personalrecordv4.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.personalrecordv4.model.WorkoutPlan
import com.example.personalrecordv4.model.WorkoutPlanRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WorkoutPlanViewModel: ViewModel() {

    private var _listWorkoutPlan: MutableLiveData<MutableList<WorkoutPlan>> = MutableLiveData()
    private var repo = WorkoutPlanRepository()
    private var isLoading: MutableLiveData<Boolean> = MutableLiveData()

    val isLoadingData: MutableLiveData<Boolean>
        get() = isLoading

    val listWorkoutPlan: MutableLiveData<MutableList<WorkoutPlan>>
        get() = _listWorkoutPlan

    init {
        _listWorkoutPlan = repo._listWorkoutPlan
        isLoading = repo.isLoading
    }

     fun getWorkoutPlan(mutableList: MutableList<String>) {
         repo.getWorkoutPlan(mutableList)
    }


}