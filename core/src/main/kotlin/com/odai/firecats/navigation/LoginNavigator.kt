package com.odai.firecats.navigation

interface LoginNavigator: Navigator {

    fun toGooglePlusLogin()

    fun attach(loginResultListener: LoginResultListener)

    fun detach(loginResultListener: LoginResultListener)

    interface LoginResultListener {

        fun onGooglePlusLoginSuccess(tokenId: String)

        fun onGooglePlusLoginFailed(statusMessage: String)

    }

}
