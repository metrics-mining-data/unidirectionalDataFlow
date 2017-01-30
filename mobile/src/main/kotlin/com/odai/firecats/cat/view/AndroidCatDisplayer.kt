package com.odai.firecats.cat.view

import com.odai.firecats.cat.displayer.CatDisplayer
import com.odai.firecats.cat.model.Cat
import com.odai.firecats.loading.AndroidLoadingView

class AndroidCatDisplayer(val view: AndroidCatView, val loadingView: AndroidLoadingView) : CatDisplayer {

    override fun display(cat: Cat) {
        view.display(cat)
        loadingView.showData()
    }

    override fun displayEmpty() {
        loadingView.showEmptyScreen()
    }

    override fun displayLoading() {
        loadingView.showLoadingScreen()
    }

    override fun displayLoading(cat: Cat) {
        view.display(cat)
        loadingView.showLoadingIndicator()
    }

    override fun displayError() {
        loadingView.showEmptyScreen()
    }

    override fun displayError(cat: Cat) {
        view.display(cat)
        loadingView.showErrorIndicator()
    }
}
