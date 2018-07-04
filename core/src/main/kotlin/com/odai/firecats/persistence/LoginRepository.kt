package com.odai.firecats.persistence

import com.odai.firecats.login.model.Authentication
import io.reactivex.Flowable

interface LoginRepository {

    fun readAuthenticationX(): Flowable<Authentication>

    fun loginWithGoogle(idToken: String): Flowable<Authentication>

    fun  loginAnonymous(): Flowable<Authentication>

}
