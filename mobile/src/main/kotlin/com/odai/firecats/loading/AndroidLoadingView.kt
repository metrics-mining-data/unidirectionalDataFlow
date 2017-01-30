package com.odai.firecats.loading

import android.content.Context
import android.support.design.widget.Snackbar
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.odai.firecats.R
import kotlinx.android.synthetic.main.loading_view.view.*

class AndroidLoadingView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private lateinit var content: View

    private var snackBar: Snackbar? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        content = findViewById(R.id.content)
    }

    fun attach(retryListener: RetryListener) {
        loadingButton.setOnClickListener {
            retryListener.onRetry()
        }
    }

    fun detach() {
        loadingButton.setOnClickListener(null)
    }

    fun showLoadingIndicator() {
        content.visibility = VISIBLE
        loadingContainer.visibility = GONE
        displaySnackBar("Still loading data")
    }

    fun showErrorIndicator() {
        content.visibility = VISIBLE
        loadingContainer.visibility = GONE
        displaySnackBar("An error has occurred")
    }

    fun showLoadingScreen() {
        snackBar?.dismiss()
        content.visibility = GONE
        loadingContainer.visibility = VISIBLE
        loadingLabel.text = "LOADING"
    }

    fun showData() {
        snackBar?.dismiss()
        content.visibility = VISIBLE
        loadingContainer.visibility = GONE
    }

    fun showEmptyScreen() {
        snackBar?.dismiss()
        content.visibility = GONE
        loadingContainer.visibility = VISIBLE
        loadingLabel.text = "EMPTY"
    }

    fun showErrorScreen() {
        snackBar?.dismiss()
        content.visibility = GONE
        loadingContainer.visibility = VISIBLE
        loadingLabel.text = "ERROR"
    }

    private fun displaySnackBar(message: String) {
        if (snackBar?.isShown ?: false) {
            snackBar!!.setText(message)
        } else {
            snackBar = Snackbar.make(this, message, Snackbar.LENGTH_INDEFINITE)
            snackBar?.show()
        }
    }

    interface RetryListener {
        fun onRetry()
    }

}
