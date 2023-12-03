package com.example.personalrecordv4

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.personalrecordv4.databinding.FragmentWorkoutListBinding
import com.example.personalrecordv4.viewmodel.WorkoutPlanViewModel

class WorkoutListFragment : Fragment(R.layout.fragment_workout_list) {
   private val workoutPlanViewModel : WorkoutPlanViewModel by activityViewModels()
    private lateinit var binding : FragmentWorkoutListBinding
    private val workoutPlanAdapter = WorkoutPlanAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWorkoutListBinding.bind(view)

        workoutPlanViewModel.getWorkoutPlans(object : WorkoutPlanCallback{
            override fun onResponse(response: WorkoutPlanResponse) {
                workoutPlanAdapter.updateListData(response.workoutPlan)
                binding.worv.layoutManager = LinearLayoutManager(activity)
                binding.worv.adapter = workoutPlanAdapter
                binding.worv.visibility = View.VISIBLE

            }
        })
    }
}