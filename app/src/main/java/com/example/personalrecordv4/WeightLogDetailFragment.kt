package com.example.personalrecordv4

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.personalrecordv4.databinding.FragmentWeightLogDetailBinding
import com.example.personalrecordv4.model.WeightLog
import com.example.personalrecordv4.viewmodel.WeightLogViewModel
import java.text.SimpleDateFormat


class WeightLogDetailFragment : Fragment(R.layout.fragment_weight_log_detail) {
    private val weightLogViewModel : WeightLogViewModel by activityViewModels()
    private lateinit var binding: FragmentWeightLogDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weight_log_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWeightLogDetailBinding.bind(view)
        val data = arguments
        if (data!=null){
            requireArguments().getString("weightlogpicture").let {
                if (it != null) {
                    Log.i("WeightLogDetailFragment", "Fetching Successful")
                    weightLogViewModel.getDetails(it)
                } else {
                    Log.i("WeightLogDetailFragment", "Fetching Failed")
                }
            }
        }
        weightLogViewModel.weightLog.observe(viewLifecycleOwner, Observer<WeightLog>{
            if (it == null){
                Log.i("WeightLogDetailFragment", "Observe Failed")
            } else {
                Log.i("WeightLogDetailFragment", "Observe Successful")
                binding.tvDetailWeight.text = "${weightLogViewModel.weightLog.value!!.weight} Kg"
                val time = weightLogViewModel.weightLog.value!!.created_at
                val date = time.toDate()
                val dateFormat = SimpleDateFormat("dd-MM-yyyy")
                val formattedDate = dateFormat.format(date)
                binding.tvWeightDate.text = formattedDate
                val imageView = requireView().findViewById<ImageView>(R.id.ivDetailPicture)
                Glide.with(this).load(weightLogViewModel.weightLog.value!!.url).into(imageView)
                binding.btnDeleteLog.setOnClickListener {
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Delete Confirmation")
                    builder.setMessage("Delete this log?")
                    builder.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, _ ->
                        //No Logic Yet
                        dialog.dismiss()
                    })
                    builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, _ ->
                        dialog.dismiss()
                    })
                    builder.show()
                }
            }
        })
    }

}