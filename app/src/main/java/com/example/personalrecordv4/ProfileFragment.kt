package com.example.personalrecordv4

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.personalrecordv4.databinding.FragmentProfileBinding
import com.example.personalrecordv4.model.User
import com.example.personalrecordv4.viewmodel.UserViewModel


class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private val userViewModel : UserViewModel by activityViewModels()
    private lateinit var binding: FragmentProfileBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)


        userViewModel.userData.observe(viewLifecycleOwner, Observer<User>{
            if(it == null){
                startActivity(Intent(activity, AuthActivity::class.java))
                requireActivity().finish()
            }else{
                binding.tvusername.text = userViewModel.userData.value!!.name
                binding.tvusername2.text = userViewModel.userData.value!!.name
                binding.tvemail.text = userViewModel.userData.value!!.email
            }
        })
        binding.btnSignout.setOnClickListener {
            userViewModel.signOut()
            startActivity(Intent(activity, AuthActivity::class.java))
            requireActivity().finish()
        }

        binding.btnEditProfile.setOnClickListener {
            val intent = Intent(activity, EditProfileActivity::class.java)
            intent.putExtra(EditProfileActivity.EXTRA_NAMA, binding.tvusername.text.toString())
            startActivity(intent)
        }

    }


}