package com.example.app_comp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.app_comp.databinding.FragmentLoginBinding


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
                val email = binding.etEmail.text.toString()
                val password = binding.etPassword.text.toString()

                viewModel.loginUser(email, password)
                    .observe(viewLifecycleOwner, Observer { result ->
                    when (result) {
                        is Result.Success -> {
                            hideProgress()
                            var user = result.data
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
                            hideProgress()
                            Log.d(DEBUGGING, "putka1 Error: ${result.exception}")
                            showToast("putka1 Login failed: ${result.exception}")
                        }
                        is Result.Loading -> {
                            showProgress()
                            Log.d(DEBUGGING,"putka2")
                        }
                        is Result.Failed -> {
                            hideProgress()
                            Log.d(DEBUGGING,"putka3")

                        }
                    }
                })
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


