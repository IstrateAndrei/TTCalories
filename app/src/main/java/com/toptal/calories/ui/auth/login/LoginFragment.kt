package com.toptal.calories.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.toptal.calories.MainActivity
import com.toptal.calories.R
import com.toptal.calories.databinding.FragmentUserLoginBinding
import com.toptal.calories.utils.base.BaseFragment
import com.toptal.calories.utils.getViewVisibility
import com.toptal.calories.utils.matchesEmailPattern
import com.toptal.calories.utils.showSnackMessage
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm

class LoginFragment : BaseFragment() {

    var _binding: FragmentUserLoginBinding? = null
    val binding get() = _binding!!

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        initViews()
        initListeners()
        observe()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun observe() {
        loginViewModel.loginObservable.observe(viewLifecycleOwner, Observer { response ->
            toggleLoading(false)
            if (response.isSuccessful) {
                FirebaseAuth.getInstance().currentUser?.let {
                    if (it.isEmailVerified) {
                        // open entry list screen
//                        it.getIdToken(false).result.token // bearer token for account
                        openMainScreen()
                    } else {
                        showSnackMessage(
                            requireView(),
                            getString(R.string.email_verification_message_string)
                        )
                    }
                }
            } else {
                showSnackMessage(requireView(), getString(R.string.unable_to_connect_string))
            }
        })
    }


    fun openMainScreen() {
        startActivity(Intent(requireContext(), MainActivity::class.java))
        requireActivity().finish()
    }


    override fun toggleLoading(isLoading: Boolean) {
        binding.fulProgressBar.visibility = getViewVisibility(isLoading)
    }

    override fun initViews() {
//keep this here for now

    }

    override fun initListeners() {

        initEditFieldWatchers()

        binding.fulLoginBtn.setOnClickListener {

            val email = binding.fulEmailEt.text.toString()
            val password = binding.fulPasswordEt.text.toString()
            if (!matchesEmailPattern(email)) {
                showSnackMessage(requireView(), getString(R.string.error_invalid_email_format))
                return@setOnClickListener
            }
            toggleLoading(true)
            loginViewModel.loginAction(email, password)

//            val jwt = Jwts.builder().claim("email", email).claim("password", password)
//                .signWith(SignatureAlgorithm.HS256, "secret".encodeToByteArray()).compact()
//
//            loginViewModel.loginWithToken(jwt)
        }

        binding.fulRegisterMsgTv.setOnClickListener {
            findNavController().navigate(R.id.login_to_register_action)
        }

    }

    fun initEditFieldWatchers() {
        binding.fulEmailEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.fulLoginBtn.isEnabled =
                    (p0?.isNotEmpty() == true && binding.fulPasswordEt.text.isNotEmpty())
            }

            override fun afterTextChanged(p0: Editable?) {
            }


        })
        binding.fulPasswordEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.fulLoginBtn.isEnabled =
                    (p0?.isNotEmpty() == true && binding.fulEmailEt.text.isNotEmpty())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

    }
}