package com.example.personalrecordv4.model

data class Exercise(val Name: String, val TutorialVid: String, val MuscleType: MutableList<String>, val Instruction: MutableList<String>, val DefaultSets: Int, val DefaultReps: Int){
    constructor() : this("","", mutableListOf(), mutableListOf(), 3, 10)

}
