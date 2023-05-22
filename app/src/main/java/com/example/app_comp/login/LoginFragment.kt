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
import com.example.app_comp.databinding.FragmentLoginBinding
import com.example.app_comp.utils.Result
import com.example.app_comp.utils.USER_ROLE
import com.example.app_comp.utils.config
import com.example.app_comp.utils.isValidEmail
import com.example.app_comp.utils.mAuth
import com.example.app_comp.utils.showToast
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    // an instance of the LoginViewModel
    // "by viewModels()" means that it's tied to the lifecycle of the component - Activity/Fragment
    // it will be automatically destroyed when the component is destroyed (e.g., when the user navigates away from the screen).
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
                setUpLoginObservers()
            }
        }

        binding.tvGoToRegister.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply{
                replace(R.id.frame_layout, RegisterFragment())
                // adds the fragment to the backstack so it closes when back btn is pressed
                // null refers to a name that can be given to this transaction
                addToBackStack(null)
                commit()
            }
        }
    }

    // setting up observers for login events and performs actions based on the result
    private fun setUpLoginObservers() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        viewModel.loginUser(email, password)
            .onStart {

            }
            .onEach { result ->
                when (result) {
                    is Result.Success<User> -> {
                        val user = result.data
                        config.userId = mAuth.currentUser!!.uid
                        config.userName = user.name
                        config.userEmail = user.email
                        config.userRole = user.user_role
                        config.isLoggedIn = true
                        if(user.user_role == USER_ROLE){
                            startActivity(Intent(requireActivity(), UserActivity::class.java))
                        } else {
                            startActivity(Intent(requireActivity(), AdminActivity::class.java))
                        }
                    }
                    is Result.Error -> {
                        showToast("Login failed: ${result.exception}")
                    }
                    is Result.Loading -> {

                    }
                    else -> {

                    }
                }
            }
            .launchIn(lifecycleScope)
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


