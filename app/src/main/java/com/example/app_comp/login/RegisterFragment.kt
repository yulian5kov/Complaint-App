package com.example.app_comp.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.app_comp.*
import com.example.app_comp.data.User
import com.example.app_comp.databinding.FragmentRegisterBinding
import com.example.app_comp.utils.Result
import com.example.app_comp.utils.USER_ROLE
import com.example.app_comp.utils.config
import com.example.app_comp.utils.isValidEmail
import com.example.app_comp.utils.mAuth
import com.example.app_comp.utils.showToast
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater)

        binding.btLogin.setOnClickListener {
                if (!validateInputData()) return@setOnClickListener
                val name = binding.etName.text.toString()
                val email = binding.etEmail.text.toString()
                val password = binding.etPassword.text.toString()

                if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
                    showToast("Please fill in all the fields")
                    return@setOnClickListener
                }

                val user = User(name = name, email = email, user_role = USER_ROLE)

                viewModel.addUser(user, password)
                    .onEach { result ->
                        when (result) {
                            is Result.Success -> {
                                showToast("User successfully registered")
                                config.userId = mAuth.currentUser!!.uid
                                config.userName = user.name
                                config.userEmail = user.email
                                config.userRole = USER_ROLE
                                config.isLoggedIn = true
                                startActivity(Intent(requireActivity(), UserActivity::class.java))
                                requireActivity().finish()
                            }
                            is Result.Error -> {
                                showToast(result.exception)
                            }
                            is Result.Failed -> {
                                showToast("${result.message} + ${result.error}")
                            }
                            is Result.Loading -> {
                            }
                        }
                    }
                    .launchIn(viewLifecycleOwner.lifecycleScope)
        }
        return binding.root
    }

    private fun validateInputData(): Boolean {
        if (binding.etName.text.toString().isEmpty()) {
            binding.tiName.error = "Email Required"
            binding.etName.requestFocus()
            return false
        } else if (binding.etEmail.text.toString().isEmpty()) {
            binding.tiEmail.error = "Email Required"
            binding.etEmail.requestFocus()
            return false
        } else if (!isValidEmail(binding.etEmail.text.toString())) {
            binding.tiEmail.error = "Please enter a valid email"
            binding.etEmail.requestFocus()
            return false
        } else if (binding.etPassword.text.toString().isEmpty()) {
            binding.tiPassword.error = "Password Required"
            binding.etPassword.requestFocus()
            return false
        } else if (binding.etConfirmPassword.text.toString().isEmpty()) {
            binding.tiConfirmPassword.error = "Confirm Password Required"
            binding.etConfirmPassword.requestFocus()
            return false
        } else if (binding.etPassword.text.toString() != binding.etConfirmPassword.text.toString()) {
            binding.tiConfirmPassword.error = "Confirm Password doesn't match with password"
            binding.etConfirmPassword.requestFocus()
            return false
        } else if (binding.etPassword.text.toString().length < 6) {
            binding.tiPassword.error = "Please enter 6 characters long password"
            binding.etPassword.requestFocus()
            return false
        }
        return true
    }

}