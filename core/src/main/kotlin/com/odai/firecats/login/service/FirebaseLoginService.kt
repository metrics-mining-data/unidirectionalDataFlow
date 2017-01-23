package com.odai.firecats.login.service

import com.jakewharton.rxrelay2.BehaviorRelay
import com.odai.firecats.login.model.Authentication
import com.odai.firecats.persistence.LoginRepository
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable

class FirebaseLoginService(private val loginRepository: LoginRepository) : LoginService {

    private val authRelay: BehaviorRelay<Authentication> = BehaviorRelay.create()

    override fun getAuthentication(): Flowable<Authentication> {
        return authRelay
                .toFlowable(BackpressureStrategy.LATEST)
                .startWith(initRelay())
    }

    private fun initRelay(): Flowable<Authentication> {
        return Flowable.defer {
            if (authRelay.hasValue() && authRelay.value.isSuccess) {
                return@defer Flowable.empty<Authentication>()
            } else {
                return@defer fetchUser()
            }
        }
    }

    private fun fetchUser(): Flowable<Authentication> {
        return loginRepository.readAuthentication()
                .doOnNext(authRelay)
                .ignoreElements()
                .toFlowable()
    }

    override fun loginWithGoogle(idToken: String) {
        loginRepository.loginWithGoogle(idToken)
                .subscribe {
                    authRelay.accept(it)
                }
    }

    override fun loginAnonymous() {
        loginRepository.loginAnonymous()
                .subscribe {
                    authRelay.accept(it)
                }
    }

}
