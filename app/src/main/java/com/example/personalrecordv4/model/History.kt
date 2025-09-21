package com.example.personalrecordv4.model

import com.google.firebase.Timestamp

data class History (val name: String , val userId : String, val workoutType : String ,val exerciseLogs : MutableList<String>, val date: Timestamp, val split: String){
    constructor() : this ("","","",  mutableListOf(), Timestamp.now(),"")
}