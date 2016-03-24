package com.odai.architecturedemo.loading

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.*
import com.odai.architecturedemo.R
import com.odai.architecturedemo.cats.CatsPresenter
import kotlinx.android.synthetic.main.loading_view.view.*

class AndroidLoadingView(context: Context, attrs: AttributeSet) : LoadingView, FrameLayout(context, attrs) {

    private var _content: View? = null

    private var content: View
        get() = _content!!
        set(value) {
            _content = value
        };

    private var toast: Toast? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        content = findViewById(R.id.content)
    }

    override fun attach(retryListener: RetryClickedListener) {
        loadingButton.setOnClickListener {
            retryListener.onRetry()
        }
    }


    override fun showLoadingIndicator() {
        content.visibility = VISIBLE
        loadingContainer.visibility = GONE
        displayToast("Still loading data")
    }

    override fun showErrorIndicator() {
        content.visibility = VISIBLE
        loadingContainer.visibility = GONE
        displayToast("An error has occurred")
    }

    override fun showLoadingScreen() {
        toast?.cancel()
        content.visibility = GONE
        loadingContainer.visibility = VISIBLE
        loadingLabel.text = "LOADING"
    }

    override fun showData() {
        toast?.cancel()
        content.visibility = VISIBLE
        loadingContainer.visibility = GONE
    }

    override fun showEmptyScreen() {
        toast?.cancel()
        content.visibility = GONE
        loadingContainer.visibility = VISIBLE
        loadingLabel.text = "EMPTY"
    }

    override fun showErrorScreen() {
        toast?.cancel()
        content.visibility = GONE
        loadingContainer.visibility = VISIBLE
        loadingLabel.text = "ERROR"
    }

    private fun displayToast(message: String) {
        toast?.cancel()
        toast = Toast.makeText(context, message, Toast.LENGTH_LONG)
        toast!!.show()
    }

}
