package com.odai.firecats.login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.odai.firecats.CatApplication
import com.odai.firecats.R
import com.odai.firecats.cat.LoginPresenter
import com.odai.firecats.navigation.AndroidLoginNavigator
import com.odai.firecats.navigation.AndroidNavigator
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var loginPresenter: LoginPresenter
    private lateinit var navigator: AndroidLoginNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val loginGoogleApiClient = LoginGoogleApiClient(this)
        loginGoogleApiClient.setupGoogleApiClient()
        navigator = AndroidLoginNavigator(this, loginGoogleApiClient, AndroidNavigator(this))
        loginPresenter = LoginPresenter(getCatApplication().loginService, login_view, navigator)
    }

    private fun getCatApplication(): CatApplication {
        return application as CatApplication
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!navigator.onActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onStart() {
        super.onStart()
        loginPresenter.startPresenting()
    }

    override fun onStop() {
        super.onStop()
        loginPresenter.stopPresenting()
    }

}
