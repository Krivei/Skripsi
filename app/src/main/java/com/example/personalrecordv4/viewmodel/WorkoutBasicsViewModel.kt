package com.example.personalrecordv4.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.personalrecordv4.model.Basics
import com.example.personalrecordv4.model.BasicsRepository

class WorkoutBasicsViewModel : ViewModel() {
    private var _listBasics : MutableLiveData<MutableList<Basics>> = MutableLiveData(mutableListOf())
    private var _basics : MutableLiveData<Basics> = MutableLiveData()
    private var repo = BasicsRepository()
    val listBasics : MutableLiveData<MutableList<Basics>>
        get() = _listBasics

    val basics : MutableLiveData<Basics>
        get() = _basics

    init {
        _basics = repo._basics
        _listBasics = repo._listBasics
    }

    fun getBasics(){
        repo.getBasics()
    }

    fun getBasicsDetails(nama: String){
        repo.getBasicsDetails(nama)
    }
}