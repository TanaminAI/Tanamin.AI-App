package com.dicoding.tanaminai.view.auth.signup

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.tanaminai.R
import com.dicoding.tanaminai.data.remote.auth.response.RegisterResponse
import com.dicoding.tanaminai.databinding.ActivitySignUpBinding
import com.dicoding.tanaminai.utils.ResultState
import com.dicoding.tanaminai.view.auth.signin.SignInActivity
import com.dicoding.tanaminai.view.factory.AuthViewModelFactory

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    private val signUpViewModeL by viewModels<SignUpViewModel> {
        AuthViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupAction()
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                alertDialog(getString(R.string.registration_failed),
                    getString(R.string.please_fill_all_the_fields), getString(R.string.alert_ok))
            } else {
                signUpViewModeL.register(name, lastName = "", email, password)
                    .observe(this) { resultState: ResultState<RegisterResponse> ->
                        when (resultState) {
                            is ResultState.Loading -> {
                                showLoading(true)
                            }

                            is ResultState.Success -> {
                                showLoading(false)
                                alertDialog(
                                    getString(R.string.registration_success),
                                    getString(R.string.pengguna_berhasil_dibuat, name),
                                    getString(R.string.alert_ok)
                                )
                            }

                            is ResultState.Error -> {
                                showLoading(false)
                                val errorMessage = resultState.error
                                alertDialog(getString(R.string.registration_failed), errorMessage, getString(R.string.alert_close))

                            }
                        }
                    }

            }
        }
        binding.tvClickSignin.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun alertDialog(title: String, message: String, positiveButtonText: String) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(positiveButtonText) { dialog, _ -> dialog.dismiss() }
            create()
            show()
        }
    }

}