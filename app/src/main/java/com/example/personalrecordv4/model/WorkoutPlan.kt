package com.example.personalrecordv4.model

import com.google.firebase.Timestamp

data class WorkoutPlan(val name: String, val type: String, val splitId: MutableList<String>, val timestamp: Timestamp, val sets: Int, val reps: Int){
    constructor() : this("", "", mutableListOf(), Timestamp.now(),3,10) // default constructor needed for firebase
 }
