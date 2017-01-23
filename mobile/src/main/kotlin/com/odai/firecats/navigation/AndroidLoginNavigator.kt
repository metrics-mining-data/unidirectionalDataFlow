package com.odai.firecats.navigation

import android.content.Intent
import android.support.v4.app.FragmentActivity
import android.util.Log
import com.odai.firecats.cat.model.Cat
import com.odai.firecats.login.LoginGoogleApiClient

class AndroidLoginNavigator(
        private val activity: FragmentActivity,
        private val googleApiClient: LoginGoogleApiClient,
        private val navigator: Navigator
) : LoginNavigator {

    private val RC_SIGN_IN = 242

    private var loginResultListener: LoginNavigator.LoginResultListener? = null

    override fun toCat(cat: Cat) {
        navigator.toCat(cat)
    }

    override fun toCats() {
        navigator.toCats()
    }


    override fun toGooglePlusLogin() {
        val signInIntent = googleApiClient.signInIntent
        activity.startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun attach(loginResultListener: LoginNavigator.LoginResultListener) {
        this.loginResultListener = loginResultListener
    }

    override fun detach(loginResultListener: LoginNavigator.LoginResultListener) {
        this.loginResultListener = null
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        if (requestCode != RC_SIGN_IN) {
            return false
        }
        val result = googleApiClient.getSignInResultFromIntent(data)
        if (result.isSuccess) {
            val account = result.signInAccount
            loginResultListener!!.onGooglePlusLoginSuccess(account!!.idToken!!)
        } else {
            Log.e("FireCats", "Failed to authenticate GooglePlus " + result.status.statusCode)
            loginResultListener!!.onGooglePlusLoginFailed(result.status.statusMessage!!)
        }
        return true
    }

}
