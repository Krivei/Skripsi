package com.example.personalrecordv4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.personalrecordv4.databinding.ActivityEditProfilBinding
import com.example.personalrecordv4.viewmodel.UserViewModel

class EditProfileActivity : AppCompatActivity() {

    private val userViewModel : UserViewModel by viewModels()
    private lateinit var binding : ActivityEditProfilBinding

    companion object{
        const val EXTRA_NAMA = "extra_nama"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etNameEdit.setText(intent.getStringExtra(EXTRA_NAMA))

        binding.btnSaveChange.setOnClickListener {
            userViewModel.editData(binding.etNameEdit.text.toString().trim(), binding.etPasswordEdit.text.toString().trim())
            finish()
            Toast.makeText(this,"Changes Saved Successfully", Toast.LENGTH_SHORT).show()
        }
    }
}