package com.example.personalrecordv4.model

data class WorkoutPlan(val Name: String, val Type: String, val SplitId: MutableList<String>){
    constructor() : this("", "", mutableListOf()) // default constructor needed for firebase
 }
