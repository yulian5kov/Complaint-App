package com.example.app_comp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.app_comp.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater)

        binding.btLogin.setOnClickListener {
            if (validateInputData()){
                val name = binding.etName.text.toString()
                val email = binding.etEmail.text.toString()
                val password = binding.etPassword.text.toString()

                if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
                    showToast("Please fill in all the fields")
                    return@setOnClickListener
                }

                val user = User(name = name, email = email, user_role = USER_ROLE)
                showProgress()

                viewModel.addUser(user, password)
                    .onEach { result ->
                        when (result) {
                            is Result.Success -> {
                                hideProgress()
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
                                hideProgress()
                                showToast(result.exception)
                            }
                            is Result.Failed -> {
                                hideProgress()
                                showToast("${result.message} + ${result.error}")
                            }
                            is Result.Loading -> {
                                showProgress()
                            }
                        }
                    }
                    .launchIn(viewLifecycleOwner.lifecycleScope)

            }
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