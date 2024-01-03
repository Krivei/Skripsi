package com.example.personalrecordv4.model

data class Exercise(val name: String, val tutorialVid: String, val muscleType: MutableList<String>,
                    val instruction: MutableList<String>, var defaultSets: Int, var defaultReps: Int){
    constructor() : this("","", mutableListOf(), mutableListOf(), 3, 10)

}

