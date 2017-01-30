package com.odai.firecats.cats.view

import com.odai.firecats.cats.displayer.CatsDisplayer
import com.odai.firecats.cats.model.Cats
import com.odai.firecats.favourite.model.FavouriteCats
import com.odai.firecats.loading.AndroidLoadingView
import com.odai.firecats.loading.AndroidLoadingView.RetryListener

class AndroidCatsDisplayer(val view: AndroidCatsView, val loadingView: AndroidLoadingView): CatsDisplayer {
    override fun attach(listener: CatsDisplayer.CatsActionListener) {
        view.attach(listener)
        loadingView.attach(object : RetryListener {
            override fun onRetry() {
                listener.onRetry()
            }
        })
    }

    override fun detach(listener: CatsDisplayer.CatsActionListener) {
        view.detach(listener)
        loadingView.detach()
    }

    override fun display(cats: Cats, favouriteCats: FavouriteCats) {
        view.display(cats, favouriteCats)
        loadingView.showData()
    }

    override fun displayEmpty() {
        loadingView.showEmptyScreen()
    }

    override fun displayLoading() {
        loadingView.showLoadingScreen()
    }

    override fun displayLoading(cats: Cats, favouriteCats: FavouriteCats) {
        view.display(cats, favouriteCats)
        loadingView.showLoadingIndicator()
    }

    override fun displayError() {
        loadingView.showEmptyScreen()
    }

    override fun displayError(cats: Cats, favouriteCats: FavouriteCats) {
        view.display(cats, favouriteCats)
        loadingView.showErrorIndicator()
    }
}
