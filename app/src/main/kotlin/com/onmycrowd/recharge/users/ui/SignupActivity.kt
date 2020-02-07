
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


class SignupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        signup.setOnClickListener {

        }
    }
}
