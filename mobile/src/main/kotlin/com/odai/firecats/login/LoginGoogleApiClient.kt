package com.odai.firecats.login

import android.content.Intent
import android.support.v4.app.FragmentActivity
import android.util.Log
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.api.GoogleApiClient
import com.odai.firecats.R

class LoginGoogleApiClient(private val activity: FragmentActivity) {

    private var apiClient: GoogleApiClient? = null

    fun setupGoogleApiClient() {
        val string = activity.getString(R.string.default_web_client_id)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(string)
                .requestEmail()
                .build()
        apiClient = GoogleApiClient.Builder(activity)
                .enableAutoManage(
                        activity) {
                    Log.d("FireCats", "Failed to connect to GMS")
                    //TODO handle error
                }
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()
    }

    val signInIntent: Intent
        get() = Auth.GoogleSignInApi.getSignInIntent(apiClient)

    fun getSignInResultFromIntent(data: Intent?): GoogleSignInResult {
        return Auth.GoogleSignInApi.getSignInResultFromIntent(data)
    }

}
