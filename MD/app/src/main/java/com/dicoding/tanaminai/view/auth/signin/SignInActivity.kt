package com.dicoding.tanaminai.view.auth.signin

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.tanaminai.R
import com.dicoding.tanaminai.databinding.ActivitySignInBinding
import com.dicoding.tanaminai.utils.ResultState
import com.dicoding.tanaminai.view.auth.signup.SignUpActivity
import com.dicoding.tanaminai.view.factory.AuthViewModelFactory
import com.dicoding.tanaminai.view.main.MainActivity

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding

    private val signInViewModel by viewModels<SignInViewModel>{
        AuthViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySignInBinding.inflate(layoutInflater)
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
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                alertDialog(getString(R.string.login_failed), getString(R.string.please_fill_all_the_fields), getString(R.string.alert_ok))
            } else {
                signInViewModel.login(email, password).observe(this) { resultState ->
                    when(resultState) {
                        is ResultState.Loading -> {

                        }
                        is ResultState.Success -> {
                            AlertDialog.Builder(this).apply {
                                setTitle(getString(R.string.login_success))
                                setMessage(getString(R.string.greetings_login))
                                setPositiveButton(getString(R.string.alert_next)) { _, _ ->
                                    val intent = Intent(context, MainActivity::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                    finish()
                                }
                                create()
                                show()
                            }
                        }
                        is ResultState.Error -> {
                            val errorMessage = resultState.error
                            alertDialog(getString(R.string.login_failed), errorMessage, getString(R.string.alert_close))
                        }

                    }
                }

           }
        }

        binding.tvClickSignup.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
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