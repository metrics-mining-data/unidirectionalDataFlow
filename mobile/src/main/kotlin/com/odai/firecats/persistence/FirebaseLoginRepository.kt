package com.odai.firecats.persistence

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.odai.firecats.login.model.Authentication
import com.odai.firecats.login.model.User
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable

class FirebaseLoginRepository(private val firebaseAuth: FirebaseAuth) : LoginRepository {

    override fun readAuthentication(): Flowable<Authentication> {
        return Flowable.create(
                {
                    val currentUser = firebaseAuth.currentUser
                    if (currentUser != null) {
                        it.onNext(authenticationFrom(currentUser))
                    }
                    it.onComplete()
                },
                BackpressureStrategy.LATEST
        )
    }

    override fun loginWithGoogle(idToken: String): Flowable<Authentication> {
        return Flowable.create(
                {
                    val credential = GoogleAuthProvider.getCredential(idToken, null)
                    firebaseAuth.signInWithCredential(credential)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val firebaseUser = task.result.user
                                    it.onNext(authenticationFrom(firebaseUser))
                                } else {
                                    it.onNext(Authentication(null, task.exception))
                                }
                                it.onComplete()
                            }
                },
                BackpressureStrategy.LATEST
        )
    }

    private fun authenticationFrom(currentUser: FirebaseUser): Authentication {
        return Authentication(User(currentUser.uid))
    }

}
