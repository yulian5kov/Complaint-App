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

    private fun FragmentActivity.replaceFragment(fragment: Fragment) {
        val backStateName: String = fragment.javaClass.name
        val frameLayout: FrameLayout = findViewById(R.id.frame_layout)
        //val frameLayout: FrameLayout = LoginActivity().

        val popFragment: Boolean = supportFragmentManager.popBackStackImmediate(backStateName, 0)
        if (!popFragment) {
            val beginTrans = supportFragmentManager.beginTransaction()
            beginTrans.setCustomAnimations(
                /* enter = */ R.anim.slide_in,
                /* exit = */ R.anim.fade_out,
                /* popEnter = */ R.anim.fade_in,
                /* popExit = */ R.anim.slide_out
            )
            beginTrans.replace(frameLayout.id, fragment)
            beginTrans.addToBackStack(backStateName)
            beginTrans.commit()
        }
    }

}


