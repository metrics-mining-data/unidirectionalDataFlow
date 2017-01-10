package com.odai.firecats.loading

interface LoadingDisplayer {

    fun attach(retryListener: LoadingActionListener)

    fun showLoadingIndicator()

    fun showLoadingScreen()

    fun showData()

    fun showEmptyScreen()

    fun showErrorIndicator()

    fun showErrorScreen()

    interface LoadingActionListener {
        fun onRetry()
    }


}
