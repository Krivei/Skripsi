package com.example.personalrecordv4.listener

interface onItemClickListener {
    fun OnItemClick(position: Int){

    }

    fun OnItemSelect(name: String, reps: Int, sets: Int, spltids: Array<String>){

    }

    fun OnItemDelete(nama: String, reps: Int, sets: Int, splitId: String){

    }

    fun OnWorkoutStart(nama: String, reps: Int, sets: Int, splitIds: Array<String>){

    }


}
