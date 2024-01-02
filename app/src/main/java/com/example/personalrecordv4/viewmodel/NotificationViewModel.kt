package com.example.personalrecordv4.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.personalrecordv4.model.Notifications
import com.example.personalrecordv4.model.NotificationRepository

class NotificationViewModel : ViewModel() {
    private var repo = NotificationRepository()
    private var _notifications : MutableLiveData<Notifications> = MutableLiveData()
    private var _token : MutableLiveData<String> = MutableLiveData()
    private var _days: MutableLiveData<MutableList<Int>> = MutableLiveData(mutableListOf())

    val notifications : MutableLiveData<Notifications>
        get() = _notifications

    val token : MutableLiveData<String>
        get() = _token

    val days : MutableLiveData<MutableList<Int>>
        get() = _days

    init {
        _notifications = repo._notifications
        _token = repo._token
        _days = repo._days
    }

    fun updateToken(){
        repo.updateToken()
    }

    fun setNotif(timestamp: String, days: MutableList<Int>){
        repo.setNotif(timestamp,days)
    }

    fun getNotif(){
        repo.getNotif()
    }
}