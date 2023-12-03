package com.example.personalrecordv4.model

data class WorkoutPlan(val name: String, val Type: String){
    val splitId= mutableMapOf<String,String>()
    fun addSplitId(nomor: String, id: String){
        splitId.putIfAbsent(nomor,id)
    }
    fun getSplit(): MutableMap<String, String>{
        return splitId
    }
}
