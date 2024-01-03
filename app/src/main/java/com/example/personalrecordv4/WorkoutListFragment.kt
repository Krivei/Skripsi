package com.example.personalrecordv4

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.personalrecordv4.model.WorkoutPlan
import com.example.personalrecordv4.viewmodel.UserViewModel
import com.example.personalrecordv4.adapter.WorkoutPlanAdapter
import com.example.personalrecordv4.databinding.FragmentProfileBinding
import com.example.personalrecordv4.listener.onItemClickListener
import com.example.personalrecordv4.viewmodel.WorkoutPlanViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton



class WorkoutListFragment : Fragment(R.layout.fragment_workout_list) , onItemClickListener {
    private val userViewModel : UserViewModel by activityViewModels()
    private var recyclerView : RecyclerView? = null
    private val workoutViewModel : WorkoutPlanViewModel by activityViewModels()
    private var workoutplanlist : MutableList<String> = mutableListOf()
    override fun OnItemSelect(name: String, reps: Int, sets: Int, spltids: Array<String>) {
        super.OnItemSelect(name, reps, sets, spltids)
        val intent = Intent(activity,EditSplitActivity::class.java)
        intent.putExtra("splitId",spltids)
        intent.putExtra("repetition",reps)
        intent.putExtra("set",sets)
        intent.putExtra("workoutName",name)
        startActivity(intent)

    }

    override fun OnItemDelete(nama: String, reps: Int, sets: Int, splitId: String) {
        super.OnItemDelete(nama, reps, sets, splitId)
        val konfirmasi = AlertDialog.Builder(requireActivity())
        konfirmasi.setTitle("Delete Exercise?")
        konfirmasi.setMessage(nama)
        konfirmasi.setNegativeButton("Cancel"){dialog, which -> dialog.dismiss()}
        konfirmasi.setPositiveButton("Confirm"){dialog,which->
            val targetId = workoutViewModel.listWorkoutPlanId.value!!.get(reps)
            userViewModel.deleteWorkoutPlan(targetId)
            Log.i("WorkoutListFragment",targetId)
        }
        konfirmasi.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userViewModel.userData.observe(this) {
            if (it != null) {
                if (it.workoutPlanId.isEmpty()){
                    recyclerView?.visibility = View.GONE
                } else {
                    workoutViewModel.getWorkoutPlan(it.workoutPlanId)
                    recyclerView?.visibility = View.VISIBLE
                }

            }
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_workout_list, container, false)
        // Inflate the layout for this fragment
    }
    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        val addWorkoutBtn = itemView.findViewById<FloatingActionButton>(R.id.addWorkout)
        addWorkoutBtn.setOnClickListener {
            val intent = Intent(activity, AddWorkoutPlanActivity::class.java)
            var templateMaker = AlertDialog.Builder(requireActivity())
            templateMaker.setTitle("Workout Plan Creation")
            templateMaker.setMessage("Would you like to use our template?")
            templateMaker.setNegativeButton("No"){
                dialog, which ->
                intent.putExtra("Template","No")
                dialog.dismiss()
                startActivity(intent)
            }
            templateMaker.setPositiveButton("Yes"){
                dialog,which ->
                intent.putExtra("Template","Yes")
                dialog.dismiss()
                startActivity(intent)
            }
            templateMaker.show()
        }
        var progressBar = view?.findViewById<ProgressBar>(R.id.progress_loader)
        workoutViewModel.isLoadingData.observe(viewLifecycleOwner) {
            if (it == null) {
                Log.i("Test", "Loading: Gagal")
            } else {
                if (it) {
                    progressBar?.visibility = View.VISIBLE
                } else {
                    progressBar?.visibility = View.GONE
                }
            }
        }
        workoutViewModel.listWorkoutPlan.observe(viewLifecycleOwner) {
           if (it == null || it.isEmpty()) {
               Log.i("WorkoutListFragment","ListWorkoutPlan empty")
               recyclerView?.visibility = View.GONE
           } else {
               recyclerView.apply {
                   Log.i("WorkoutListFragment","ListWorkoutPlan exist")
                   // set a LinearLayoutManager to handle Android
                   // RecyclerView behavior
                   recyclerView = view?.findViewById(R.id.workoutRecyclerView)
                   recyclerView?.layoutManager = LinearLayoutManager(activity)
                   recyclerView?.setHasFixedSize(true)
                   // set the custom adapter to the RecyclerView
                   recyclerView?.adapter = WorkoutPlanAdapter(it, this@WorkoutListFragment)
               }
           }
       }
    }

}