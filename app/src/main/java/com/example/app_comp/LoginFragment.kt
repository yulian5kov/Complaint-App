package com.example.app_comp

import android.content.Context
import android.content.Intent
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
    val sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
    private lateinit var repository: FirestoreRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        binding.btLogin.setOnClickListener {
            if (validateInputData()) {
                val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

                if (isLoggedIn) {
                    val userRole = sharedPreferences.getString("userRole", "")
                    if (userRole != null) {
                        navigateToUserOrAdmin(userRole)
                    }
                } else {
                    viewModel.loginEvent.observe(viewLifecycleOwner) { event ->
                        when (event) {
                            is Event.Loading -> showProgress()
                            is Event.Success -> {
                                hideProgress()
                                val userRole = (event.data as? User)?.user_role
                                if (userRole != null) {
                                    navigateToUserOrAdmin(userRole)
                                }
                            }
                            is Event.Error -> {
                                hideProgress()
                                showToast("event.error pri login")
                            }
                            is Event.Failed -> {
                                hideProgress()
                                showToast("event.failed pri login")
                            }
                        }
                    }
                    viewModel.loginUser(binding.etEmail.text.toString(), binding.etPassword.text.toString())
                }
            }
        }
        binding.tvGoToRegister.setOnClickListener {
            requireActivity().replaceFragment(RegisterFragment())
        }

        return binding.root

    }

    private fun navigateToUserOrAdmin(userRole: String) {
        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
        sharedPreferences.edit().putString("userRole", userRole).apply()
        if (userRole == USER_ROLE) {
            startActivity(Intent(requireActivity(), UserActivity::class.java))
        } else if (userRole == ADMIN_ROLE) {
            startActivity(Intent(requireActivity(), AdminActivity::class.java))
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

        }
    }

}


