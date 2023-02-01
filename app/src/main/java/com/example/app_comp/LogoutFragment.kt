package com.example.app_comp

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.example.app_comp.databinding.FragmentLogoutBinding
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

class LogoutFragment : Fragment() {
    private lateinit var binding: FragmentLogoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLogoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonLogout.setOnClickListener {
            logoutUser()
        }
    }

    private fun logoutUser() {
        config.isLoggedIn = false
        config.userId = ""
        config.userName = ""
        config.userEmail = ""
        config.userRole = ""
        findNavController().navigate(R.id.action_logoutFragment_to_loginFragment)
    }
}
