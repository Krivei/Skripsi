package com.example.personalrecordv4.model

data class WorkoutPlan(val name: String, val Type: String){
    val splitId= mutableMapOf<String,String>()
    fun addSplitId(nama: String, id: String){
        splitId.putIfAbsent(nama,id)

    }
    fun getSplit(): MutableMap<String, String>{
        return splitId
    }
}
