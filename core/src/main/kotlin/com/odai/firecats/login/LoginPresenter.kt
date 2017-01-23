package com.odai.firecats.cat

import com.odai.firecats.login.displayer.LoginDisplayer
import com.odai.firecats.login.service.LoginService
import com.odai.firecats.navigation.LoginNavigator
import io.reactivex.disposables.CompositeDisposable



class LoginPresenter(
        private val loginService: LoginService,
        private val loginDisplayer: LoginDisplayer,
        private val navigator: LoginNavigator
) {

    private var subscriptions = CompositeDisposable()

    fun startPresenting() {
        navigator.attach(loginResultListener);
        loginDisplayer.attach(actionListener)
        subscriptions.add(
                loginService.getAuthentication()
                        .subscribe {
                            if (it.isSuccess) {
                                navigator.toCats();
                            } else {
                                loginDisplayer.showAuthenticationError(it.failure?.message ?: ""); //TODO improve error display
                            }
                        }
        )
    }

    fun stopPresenting() {
        navigator.detach(loginResultListener)
        loginDisplayer.detach(actionListener)
        subscriptions.clear()
        subscriptions = CompositeDisposable()
    }

    private val actionListener = object : LoginDisplayer.LoginActionListener {

        override fun onAnonymousLoginSelected() {
            loginService.loginAnonymous()
        }

        override fun onGooglePlusLoginSelected() {
            navigator.toGooglePlusLogin();
        }

    }

    private val loginResultListener = object : LoginNavigator.LoginResultListener {
        override fun onGooglePlusLoginSuccess(tokenId: String) {
            loginService.loginWithGoogle(tokenId)
        }

        override fun onGooglePlusLoginFailed(statusMessage: String) {
            loginDisplayer.showAuthenticationError(statusMessage)
        }
    }

}
