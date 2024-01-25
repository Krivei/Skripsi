package com.example.personalrecordv4

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.personalrecordv4.adapter.WorkoutBasicsAdapter
import com.example.personalrecordv4.databinding.FragmentWorkoutBasicsBinding
import com.example.personalrecordv4.viewmodel.UserViewModel
import com.example.personalrecordv4.viewmodel.WorkoutBasicsViewModel


class WorkoutBasicsFragment : Fragment(R.layout.fragment_workout_basics) {
    private val userViewModel : UserViewModel by activityViewModels()
    private val workoutBasicsViewModel : WorkoutBasicsViewModel by activityViewModels()
    private lateinit var binding : FragmentWorkoutBasicsBinding
    private var recyclerView : RecyclerView? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userViewModel.userData.observe(this){
            if (it!=null){
                workoutBasicsViewModel.getBasics()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_workout_basics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWorkoutBasicsBinding.bind(view)
        workoutBasicsViewModel.listBasics.observe(viewLifecycleOwner){
            if (it!=null){
                recyclerView.apply {
                    recyclerView = view?.findViewById(R.id.rvWorkoutBasics)
                    recyclerView?.layoutManager = LinearLayoutManager(activity)
                    recyclerView?.setHasFixedSize(true)
                    recyclerView?.adapter = WorkoutBasicsAdapter(it)
                }
            }
        }
        binding.btnBack.setOnClickListener {
            val fragmentTransaction = requireFragmentManager().beginTransaction()
            val fragment = TutorialPickFragment()
            fragmentTransaction.replace(R.id.fragmentContainerView, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }

}