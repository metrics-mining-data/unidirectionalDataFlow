package com.odai.firecats.login.service

import com.odai.firecats.login.model.Authentication
import io.reactivex.Flowable


interface LoginService {
    fun getAuthentication(): Flowable<Authentication>

    fun loginWithGoogle(idToken: String)

    fun loginAnonymous()
}
