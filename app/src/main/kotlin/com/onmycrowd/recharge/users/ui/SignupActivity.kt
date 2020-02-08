
package com.onmycrowd.recharge.users.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.EditText
import com.onmycrowd.recharge.R
import com.amazonaws.mobile.client.UserStateDetails
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import android.widget.TextView
import android.widget.Toast
import com.amazonaws.mobile.client.UserState
import com.amazonaws.mobile.client.results.SignUpResult
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_signup.*
import java.util.regex.Pattern
import com.amazonaws.mobile.client.results.UserCodeDeliveryDetails




class SignupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        signupUser.setOnClickListener {
            name.validate("Name should not be empty") { s -> s.isNotEmpty() }
            phoneNumber.validate("Phone number should not be empty") { s -> s.isNotEmpty() }

            signupEmail.validate("Email should not be empty") { s -> s.isNotEmpty() }
            signupEmail.validate("Valid email address required") { s -> s.isValidEmail() }

            signupPassword.validate("Password should not be empty") { s -> s.isNotEmpty() }
            signupPassword.validate("Password should contain at least one upper case letter, numbers and a special character") { s -> s.isValidPassword() }

            signupConfirmPassword.validate("Password confirmation should not be empty") { s -> s.isNotEmpty() }
            signupConfirmPassword.validate("Password should contain at least one upper case letter, numbers and a special character") { s -> s.isValidPassword() }

            if(signupPassword.text.toString() != signupConfirmPassword.text.toString()) {
                Toast.makeText(this, "Password confirmation should match password.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val email = signupEmail.text.toString()
            val attributes = mapOf(
                    "name" to name.text.toString(),
                    "phone_number" to phoneNumber.text.toString(),
                    "email" to email
            )

            signUpUser(email, signupPassword.text.toString(), attributes)

            Toast.makeText(this, "Thank you. An email was sent to your email address.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                afterTextChanged.invoke(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })
    }

    private fun EditText.validate(message: String, validator: (String) -> Boolean) {
        this.afterTextChanged {
            this.error = if (validator(it)) null else message
        }
        this.error = if (validator(this.text.toString())) null else message
    }

    private fun String.isValidEmail(): Boolean
            = this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

    private fun String.isValidPassword(): Boolean {
        val pattern = Pattern.compile("^(?=.*[a-zA-Z])(?=.*[@#$%^&+=.])(?=\\S+$).{8,}$")
        return this.isNotEmpty() && pattern.matcher(this).matches()
    }

    private fun signUpUser(email: String, password: String, attributes: Map<String, String>) {
        AWSMobileClient.getInstance().signUp(email, password, attributes, null, object : Callback<SignUpResult> {
            override fun onResult(signUpResult: SignUpResult) {
                runOnUiThread {
                    Log.d("SIGN_UP", "Sign-up callback state: " + signUpResult.confirmationState)
                    if (!signUpResult.confirmationState) {
                        val details = signUpResult.userCodeDeliveryDetails
                        Log.d("SIGN_UP", "Confirm sign-up with: " + details.destination)
                    } else {
                        Log.d("SIGN_UP", "Sign-up done.")
                    }
                }
            }

            override fun onError(e: Exception) {
                Log.e("SIGN_UP", "Sign-up error", e)
            }
        })
    }
}