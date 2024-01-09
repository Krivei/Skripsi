package com.example.personalrecordv4

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.personalrecordv4.databinding.FragmentTutorialDetailBinding
import com.example.personalrecordv4.model.Exercise
import com.example.personalrecordv4.viewmodel.TutorialViewModel


class TutorialDetailFragment : Fragment(R.layout.fragment_tutorial_detail) {
    private val tutorialViewModel : TutorialViewModel by activityViewModels()
    private lateinit var binding : FragmentTutorialDetailBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tutorial_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTutorialDetailBinding.bind(view)
        val data = arguments
        if (data != null){
            requireArguments().getString("exercise").let {
                if (it!=null){
                    tutorialViewModel.getSingleTutorial(it)

                } else {
                    Log.i("Test", "Tutorial: Fetch")
                }
            }
        }
        tutorialViewModel.tutorial.observe(viewLifecycleOwner, Observer<Exercise> {
            if (it == null) {
                Log.i("Test", "Tutorial Observer Gagal")
            } else {
                binding.tvTutorialName.text = tutorialViewModel.tutorial.value!!.name
                binding.tvMuscles.text = tutorialViewModel.tutorial.value!!.muscleType.toString()
                binding.tvInstructionDetails.text =
                    tutorialViewModel.tutorial.value!!.instruction.joinToString("\n")
                binding.vvTutorial.setVideoURI(Uri.parse(tutorialViewModel.tutorial.value!!.tutorialVid))
                binding.vvTutorial.setOnPreparedListener {
                    it.setLooping(true)
                    it.start()
                }
                binding.btnTryTutorial.text = "Try ${tutorialViewModel.tutorial.value!!.name}"
            }
        })

        binding.btnBack.setOnClickListener {
            val fragmentTransaction = requireFragmentManager().beginTransaction()
            val fragment = TutorialsFragment()
            fragmentTransaction.replace(R.id.fragmentContainerView, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }

}