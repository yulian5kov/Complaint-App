package com.example.app_comp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.app_comp.databinding.FragmentLoginBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel = ViewModelProvider(requireActivity())[LoginViewModel::class.java]

    private lateinit var repository: FirestoreRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)

        binding.btLogin.setOnClickListener {
            if (validateInputData()){
                viewModel.loginEvent.observe(viewLifecycleOwner) { event ->
                    when(event) {
                        is Event.Loading -> {
                            // Show loading indicator
                        }
                        is Event.Success<*> -> {
                            // Login successful, navigate to next screen
                        }
                        is Event.Error -> {
                            // Show error message
                        }
                    }
                }
                viewModel.loginUser(binding.etEmail.text.toString(), binding.etPassword.text.toString())
            }
        }
        binding.tvGoToRegister.setOnClickListener {
            requireActivity().replaceFragment(RegisterFragment())
        }

        return binding.root

    }

    private fun validateInputData(): Boolean {
        if (binding.etEmail.text.toString().isEmpty()) {
            binding.tiEmail.error = "Email Required"
            binding.etEmail.requestFocus()
            return false
        } else if (binding.etPassword.text.toString().isEmpty()) {
            binding.tiPassword.error = "Password Required"
            binding.etPassword.requestFocus()
            return false
        } else if (!isValidEmail(binding.etEmail.text.toString())) {
            binding.tiEmail.error = "Please enter a valid email"
            binding.etEmail.requestFocus()
            return false
        }
        return true
    }



}


