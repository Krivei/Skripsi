package com.example.personalrecordv4

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.personalrecordv4.databinding.FragmentBasicsDetailBinding
import com.example.personalrecordv4.viewmodel.WorkoutBasicsViewModel


class BasicsDetailFragment : Fragment(R.layout.fragment_basics_detail) {
    private val workoutBasicsViewModel : WorkoutBasicsViewModel by activityViewModels()
    private lateinit var binding : FragmentBasicsDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_basics_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBasicsDetailBinding.bind(view)
        val data = arguments
        if (data!=null){
            requireArguments().getString("basics").let {
                if (it!=null){
                    workoutBasicsViewModel.getBasicsDetails(it)
                }
            }
        }
        workoutBasicsViewModel.basics.observe(viewLifecycleOwner){
            if (it!=null){
                binding.tvBasicTitle.text = it.name
                val imageView = requireView().findViewById<ImageView>(R.id.ivBasics)
                Glide.with(this).load(it.url).into(imageView)
                binding.tvBasics.text = it.descriptions.joinToString("\n\n")
            }
        }
        binding.imageView3.setOnClickListener {
            val fragmentTransaction = requireFragmentManager().beginTransaction()
            val fragment = WorkoutBasicsFragment()
            fragmentTransaction.replace(R.id.fragmentContainerView, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }


}