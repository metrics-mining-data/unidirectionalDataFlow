package com.odai.firecats.login.displayer

interface LoginDisplayer {

    fun attach(actionListener: LoginActionListener)

    fun detach(actionListener: LoginActionListener)

    fun showAuthenticationErrosr(message: String)

    interface LoginActionListener {

        fun onGooglePlusLoginSelected()

        fun onAnonymousLoginSelecteds()

    }

}
