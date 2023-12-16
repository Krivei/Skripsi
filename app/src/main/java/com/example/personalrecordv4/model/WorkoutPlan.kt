package com.example.personalrecordv4.model

import com.google.firebase.Timestamp

data class WorkoutPlan(val name: String, val type: String, val splitId: MutableList<String>, val timestamp: Timestamp){
    constructor() : this("", "", mutableListOf(), Timestamp.now()) // default constructor needed for firebase
 }
