package com.example.personalrecordv4

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.personalrecordv4.databinding.FragmentSignInBinding
import com.example.personalrecordv4.viewmodel.UserViewModel


class SignInFragment : Fragment(R.layout.fragment_sign_in) {
    private val userViewModel : UserViewModel by activityViewModels()
    private lateinit var binding : FragmentSignInBinding

    companion object {
         const val STATE_EMAIL = "state_email"
         const val STATE_PASSWORD = "state_password"
    }

    override fun onViewCreated(view: View,savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignInBinding.bind(view)
        if (savedInstanceState != null){
            val result = savedInstanceState.getString(STATE_EMAIL)
            binding.ETEmail.setText(result)
        }

        binding.BtnSignin.setOnClickListener {
            val email = binding.ETEmail.text.toString().trim()
            val password = binding.ETPassword.text.toString().trim()

            if(!userViewModel.isEmailValid(email)){
                binding.ETEmail.error = "Wrong Email!"
                return@setOnClickListener
            }
            if (!userViewModel.isPasswordValid(password)){
                binding.ETPassword.error = "Wrong Password!"
                return@setOnClickListener
            }
            userViewModel.signIn(email, password)
        }
        binding.BtnSignup.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainer, SignUpFragment())
                addToBackStack(null)
                commit()
            }
        }

    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(STATE_EMAIL, binding.ETEmail.text.toString().trim())
        outState.putString(STATE_PASSWORD, binding.ETPassword.text.toString().trim())
    }
}