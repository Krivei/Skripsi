package com.example.personalrecordv4

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.personalrecordv4.databinding.FragmentSignInBinding
import com.example.personalrecordv4.databinding.FragmentSignUpBinding
import com.example.personalrecordv4.viewmodel.UserViewModel

class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private val userViewModel : UserViewModel by activityViewModels()
    private lateinit var binding : FragmentSignUpBinding

    companion object {
        private const val STATE_EMAIL = "state_email"
        private const val STATE_PASSWORD = "state_password"
        private const val STATE_NAMA = "state_nama"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignUpBinding.bind(view)

        userViewModel.isRegistered.observe(viewLifecycleOwner, {isRegistered ->
            if (isRegistered){
                startActivity(Intent(activity, MainActivity::class.java))
                requireActivity().finish()
            }
        })

        binding.signup.setOnClickListener {
            val nama = binding.etname.text.toString().trim()
            val email = binding.etaddress.text.toString().trim()
            val password = binding.etpass.text.toString().trim()

            if (!userViewModel.isNameValid(nama)){
                binding.etname.error = "Please type your name"
                return@setOnClickListener
            }
            if (!userViewModel.isEmailValid(email)){
                binding.etaddress.error = "Please type the correct email"
                return@setOnClickListener
            }
            if (!userViewModel.isPasswordValid(password)){
                binding.etpass.error = "Password must have more than 5 characters"
            }

            userViewModel.signUp(nama,email,password)
        }

        binding.signupback.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainer, SignInFragment())
                addToBackStack(null)
                commit()
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(STATE_NAMA, binding.etname.text.toString().trim())
        outState.putString(STATE_EMAIL, binding.etaddress.text.toString().trim())
        outState.putString(STATE_PASSWORD, binding.etpass.text.toString().trim())
    }
}