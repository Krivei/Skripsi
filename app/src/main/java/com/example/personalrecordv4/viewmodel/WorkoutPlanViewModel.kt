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
    private var _newWorkoutPlanId : MutableLiveData<String> = MutableLiveData()
    private var _singleWorkoutPlan : MutableLiveData<WorkoutPlan> = MutableLiveData()
    private var _listWorkoutPlanId : MutableLiveData<MutableList<String>> = MutableLiveData()
    val singleWorkoutPlan : MutableLiveData<WorkoutPlan>
        get() = _singleWorkoutPlan

    val listWorkoutPlanId : MutableLiveData<MutableList<String>>
        get() = _listWorkoutPlanId

    val newWorkoutPlanId : MutableLiveData<String>
        get() = _newWorkoutPlanId

    val isLoadingData: MutableLiveData<Boolean>
        get() = isLoading

    val listWorkoutPlan: MutableLiveData<MutableList<WorkoutPlan>>
        get() = _listWorkoutPlan

    init {
        _listWorkoutPlanId = repo._listWorkoutPlanId
        _singleWorkoutPlan = repo._singleWorkoutPlan
        _newWorkoutPlanId = repo._newWorkoutPlanId
        _listWorkoutPlan = repo._listWorkoutPlan
        isLoading = repo.isLoading
    }

     fun getWorkoutPlan(mutableList: MutableList<String>) {
         repo.getWorkoutPlan(mutableList)
    }

    fun isInputValid(input: Int) : Boolean {
        return input>0
    }

    fun isNameValid(name: String) : Boolean {
        return name.isNotBlank()
    }

    fun isRadioChecked(input: Int) : Boolean {
        return input!=-1
    }

    fun addWorkoutPlan(name : String, type: String, sets: Int, reps: Int){
        repo.addWorkoutPlan(name,type,sets,reps)
    }

    fun getSingleWorkoutPlan(planId : String){
        repo.getSingleWorkoutPlan(planId)
    }

    fun createTemplate(i: Int,type: String,reps: Int,sets: Int, name: String){
        repo.createTemplate(i,type,reps,sets,name)
    }
    fun deleteWorkoutPlan(planId: String){
        repo.deleteWorkoutPlan(planId)
    }


}