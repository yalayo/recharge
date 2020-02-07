
package com.onmycrowd.recharge.users.ui

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


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        AWSMobileClient.getInstance().initialize(applicationContext, object : Callback<UserStateDetails> {
            override fun onResult(userStateDetails: UserStateDetails) {
                Log.i("INIT", "onResult: " + userStateDetails.userState)

                when (userStateDetails.userState) {
                    UserState.SIGNED_IN -> runOnUiThread {
                        val textView = findViewById<View>(R.id.text) as TextView
                        textView.text = "Logged IN"
                    }
                    UserState.SIGNED_OUT -> runOnUiThread {
                        val textView = findViewById<View>(R.id.text) as TextView
                        textView.text = "Logged OUT"
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
