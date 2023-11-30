package com.example.personalrecordv4.model

data class User( val email:String="", val name: String=""){
    private var WorkoutplanId = mutableMapOf<String, String>()
    fun addWorkoutPlanId(nama: String, id: String){
        WorkoutplanId.putIfAbsent(nama, id)

    }
    fun getWorkoutPlanId(): MutableMap<String, String>{
        return WorkoutplanId
    }

}
