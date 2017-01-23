package com.odai.firecats.login.model

data class Authentication(val user: User? = null, val failure: Throwable? = null) {

    val isSuccess: Boolean
        get() = user != null

}
