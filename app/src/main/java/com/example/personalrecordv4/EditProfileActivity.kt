package com.example.personalrecordv4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.personalrecordv4.databinding.ActivityEditProfileBinding
import com.example.personalrecordv4.viewmodel.UserViewModel

class EditProfileActivity : AppCompatActivity() {

    private val userViewModel : UserViewModel by viewModels()
    private lateinit var binding : ActivityEditProfileBinding

    companion object {
        const val EXTRA_NAMA = "extra_nama"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etEditName.setText(intent.getStringExtra(EXTRA_NAMA))

        binding.btnSaveProfile.setOnClickListener{
            userViewModel.editData(binding.etEditName.text.toString().trim(), binding.etEditPassword.text.toString().trim())
        }
        binding.ivBack.setOnClickListener{
            finish()
        }
    }
}