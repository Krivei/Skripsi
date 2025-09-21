package com.example.personalrecordv4

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.personalrecordv4.databinding.FragmentTutorialPickBinding


class TutorialPickFragment : Fragment(R.layout.fragment_tutorial_pick) {

    private lateinit var binding : FragmentTutorialPickBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tutorial_pick, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTutorialPickBinding.bind(view)
        binding.clExerciseTutorials.setOnClickListener {

            val fragmentTransaction = requireFragmentManager().beginTransaction()
            val fragment = TutorialsFragment()
            fragmentTransaction.replace(R.id.fragmentContainerView, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }

        binding.clWorkoutBasic.setOnClickListener {
            val fragmentTransaction = requireFragmentManager().beginTransaction()
            val fragment = WorkoutBasicsFragment()
            fragmentTransaction.replace(R.id.fragmentContainerView, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }


}