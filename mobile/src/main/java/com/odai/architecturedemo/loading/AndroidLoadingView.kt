package com.odai.architecturedemo.loading

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.*
import com.odai.architecturedemo.R
import com.odai.architecturedemo.cats.CatsPresenter

class AndroidLoadingView(context: Context, attrs: AttributeSet): LoadingView, FrameLayout(context, attrs) {

    private var _content: View? = null
    private var _loadingContainer: View? = null

    private var _button: Button? = null
    private var _loadingLabel: TextView? = null

    private var toast: Toast? = null

    private var content: View
        get() = _content!!
        set(value) {
            _content = value
        };

    private var loadingContainer: View
        get() = _loadingContainer!!
        set(value) {
            _loadingContainer = value
        };

    private var button: Button
        get() = _button!!
        set(value) {
            _button = value
        };

    private var loadingLabel: TextView
        get() = _loadingLabel!!
        set(value) {
            _loadingLabel = value
        };

    override fun onFinishInflate() {
        super.onFinishInflate()
        content = findViewById(R.id.content)
        loadingContainer = findViewById(R.id.loadingContainer)
        button = findViewById(R.id.loadingButton) as Button
        loadingLabel = findViewById(R.id.loadingLabel) as TextView
    }

    override fun attach(retryListener: RetryClickedListener) {
        button.setOnClickListener {
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
        content.visibility = GONE
        loadingContainer.visibility = VISIBLE
        loadingLabel.text = "LOADING"
        setBackgroundColor(resources.getColor(android.R.color.transparent, null))
    }

    override fun showData() {
        content.visibility = VISIBLE
        loadingContainer.visibility = GONE
        setBackgroundColor(resources.getColor(android.R.color.transparent, null))
    }

    override fun showEmptyScreen() {
        content.visibility = GONE
        loadingContainer.visibility = VISIBLE
        loadingLabel.text = "EMPTY"
        setBackgroundColor(resources.getColor(android.R.color.transparent, null))
    }

    override fun showErrorScreen() {
        content.visibility = GONE
        loadingContainer.visibility = VISIBLE
        loadingLabel.text = "ERROR"
        setBackgroundColor(resources.getColor(android.R.color.transparent, null))
    }

    private fun displayToast(message: String) {
        toast?.cancel()
        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        toast!!.show()
    }

}
