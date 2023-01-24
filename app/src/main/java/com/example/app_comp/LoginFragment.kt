package com.example.app_comp

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity
import com.example.app_comp.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)

        binding.btLogin.setOnClickListener {

        }
        binding.tvGoToRegister.setOnClickListener {
            requireActivity().replaceFragment(RegisterFragment())
        }

        return binding.root

    }

    private fun validateInputData(): Boolean {

        var email = binding.etEmail.text.toString()

        if (binding.etEmail.text.toString().isEmpty()) {
            binding.tiEmail.error = "Email Required"
            binding.etEmail.requestFocus()
            return false
        } else if (binding.etPassword.text.toString().isEmpty()) {
            binding.tiPassword.error = "Password Required"
            binding.etPassword.requestFocus()
            return false
        } else if (email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            //email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
            binding.tiEmail.error = "Please enter a valid email"
            binding.etEmail.requestFocus()
            return false
        }
        return true
    }



}


