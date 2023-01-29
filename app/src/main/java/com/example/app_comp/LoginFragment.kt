package com.example.app_comp

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.fragment.app.viewModels
import com.example.app_comp.databinding.FragmentLoginBinding
import androidx.lifecycle.ViewModelProvider
import com.android.volley.NetworkError
import java.io.IOException


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    //private val viewModel = ViewModelProvider(requireActivity())[LoginViewModel::class.java]
    private val viewModel: LoginViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        initListeners()
        return binding.root
    }

    private fun initListeners() {
        binding.btLogin.setOnClickListener {
            if (validateInputData()) {
                viewModel.loginEvent.observe(viewLifecycleOwner) { event ->
                    when (event) {
                        is Result.Loading -> showProgress()
                        is Result.Success<User> -> {
                            hideProgress()
                            val user = event.data /*as? User*/


                            if (user != null) {
                                config.isLoggedIn = true
                                config.userId = user.id
                                config.userRole = user.user_role
                                config.userName = user.name
                                config.userEmail = user.email

                                config.prefs.edit {
                                    putBoolean("isLoggedIn", config.isLoggedIn)
                                    putString("userId", config.userId)
                                    putString("userRole", config.userRole)
                                    putString("userName", config.userName)
                                    putString("userEmail", config.userEmail)
                                }
                            } else {
                                showToast("user is null")
                            }

                        }
                        is Result.Error -> {
                            hideProgress()
                            Log.e(DEBUGGING, "tyler Error: ${event.exception}")
                            showToast("event.error pri login")
                        }
                        is Result.Failed -> {
                            hideProgress()
                            showToast("event.failed pri login")
                        }
                    }
                }
                viewModel.loginUser(binding.etEmail.text.toString(), binding.etPassword.text.toString())
            }
        }

        binding.tvGoToRegister.setOnClickListener {
            requireActivity().replaceFragment(RegisterFragment())
        }
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

    override fun onAttach(context: Context) {
        if(isAdded){
            super.onAttach(context)
        }else{
            Log.i(DEBUGGING, "not attached")
        }
    }

}


