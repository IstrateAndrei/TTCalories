package com.toptal.calories.ui.auth.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.toptal.calories.R
import com.toptal.calories.databinding.RegisterFragmentLayoutBinding
import com.toptal.calories.utils.base.BaseFragment
import com.toptal.calories.utils.getViewVisibility
import com.toptal.calories.utils.matchesEmailPattern
import com.toptal.calories.utils.showSnackMessage

class RegisterFragment : BaseFragment() {

    private lateinit var registerViewModel: RegisterViewModel

    var _binding: RegisterFragmentLayoutBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RegisterFragmentLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        initViews()
        initListeners()
        observe()
    }

    override fun observe() {
        registerViewModel.registerObservable.observe(viewLifecycleOwner, Observer {
            if (it.isSuccessful) {
                //automatically update username after registration completed
                registerViewModel.sendVerificationEmail()
                registerViewModel.updateUserName(binding.rfUsernameEt.text.toString())
            } else {
                showSnackMessage(requireView(), getString(R.string.registration_failed_string))
            }
        })

        registerViewModel.sendVerificationObservable.observe(viewLifecycleOwner, Observer {
            if (it.isSuccessful) {
                showSnackMessage(
                    requireView(),
                    getString(R.string.email_verification_message_string)
                )
                FirebaseAuth.getInstance().currentUser?.let { user ->
                    registerViewModel.registerToFireStore(
                        user.email!!,
                        user.displayName!!,
                        user.uid
                    )
                }

            } else {
                showSnackMessage(
                    requireView(),
                    getString(R.string.error_send_email_verification_message_string)
                )
            }
            toggleLoading(false)
            findNavController().popBackStack()
        })
    }

    override fun toggleLoading(isLoading: Boolean) {
        binding.rfProgressBar.visibility = getViewVisibility(isLoading)
    }

    override fun initViews() {

    }

    override fun initListeners() {
        binding.rfRegisterBtn.setOnClickListener {
            val email = binding.rfEmailEt.text.toString()
            val username = binding.rfUsernameEt.text.toString()
            val password = binding.rfPasswordEt.text.toString()
            val confirmPassword = binding.rfConfirmPasswordEt.text.toString()

            if (email.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                showSnackMessage(requireView(), getString(R.string.complete_fields_warning_string))
                return@setOnClickListener
            }

            if (!matchesEmailPattern(email)) {
                showSnackMessage(requireView(), getString(R.string.error_invalid_email_format))
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                showSnackMessage(
                    requireView(),
                    getString(R.string.password_and_confirm_password_different_string)
                )
                return@setOnClickListener
            }

            if (!binding.rfTermsCb.isChecked) {
                showSnackMessage(
                    requireView(),
                    getString(R.string.accept_terms_and_conditions_warning_string)
                )
                return@setOnClickListener
            }
            toggleLoading(true)
            registerViewModel.registerAuthUser(email, password)
        }
        binding.rfOrSignInTv.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}