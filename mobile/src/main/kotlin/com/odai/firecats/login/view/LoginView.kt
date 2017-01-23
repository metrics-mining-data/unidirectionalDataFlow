package com.odai.firecats.login.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.odai.firecats.R
import com.odai.firecats.login.displayer.LoginDisplayer
import kotlinx.android.synthetic.main.login_view.view.*

class LoginView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs), LoginDisplayer {

    init {
        orientation = LinearLayout.VERTICAL
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        View.inflate(context, R.layout.login_view, this)
    }

    override fun attach(actionListener: LoginDisplayer.LoginActionListener) {
        sign_in_button.setOnClickListener { actionListener.onGooglePlusLoginSelected() }
        sign_in_anonymous.setOnClickListener { actionListener.onAnonymousLoginSelected() }
    }

    override fun detach(actionListener: LoginDisplayer.LoginActionListener) {
        sign_in_button.setOnClickListener(null)
        sign_in_anonymous.setOnClickListener(null)
    }

    override fun showAuthenticationError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show() //TODO improve error display
    }

}
