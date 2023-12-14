package com.example.personalrecordv4.model

data class Exercise(val name: String, val tutorialVid: String, val muscleType: MutableList<String>,
                    val instruction: MutableList<String>, val defaultSets: Int, val defaultReps: Int){
    constructor() : this("","", mutableListOf(), mutableListOf(), 3, 10)

}

