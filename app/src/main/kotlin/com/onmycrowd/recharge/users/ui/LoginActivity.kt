
package com.onmycrowd.recharge.users.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.onmycrowd.recharge.R
import com.amazonaws.mobile.client.UserStateDetails
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import android.widget.TextView
import com.amazonaws.mobile.client.UserState
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        signup.setOnClickListener {
            val signupActivity = Intent(this@LoginActivity, SignupActivity::class.java)
            startActivity(signupActivity)
        }

        AWSMobileClient.getInstance().initialize(applicationContext, object : Callback<UserStateDetails> {
            override fun onResult(userStateDetails: UserStateDetails) {
                when (userStateDetails.userState) {
                    UserState.SIGNED_IN -> runOnUiThread {
                        Log.i("INIT", "onResult: " + userStateDetails.userState)
                    }
                    UserState.SIGNED_OUT -> runOnUiThread {
                        Log.i("INIT", "onResult: " + userStateDetails.userState)
                    }
                    else -> AWSMobileClient.getInstance().signOut()
                }
            }

            override fun onError(e: Exception) {
                Log.e("INIT", "Initialization error.", e)
            }
        })
    }
}
