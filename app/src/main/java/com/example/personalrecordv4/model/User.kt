package com.example.personalrecordv4.model

data class User(var email:String="", var name: String="", var workoutPlanId: MutableList<String>){
    constructor() : this("", "", mutableListOf()) // default constructor needed for firebase


}
