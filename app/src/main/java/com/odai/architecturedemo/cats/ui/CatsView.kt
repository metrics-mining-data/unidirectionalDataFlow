package com.odai.architecturedemo.cats.ui

import com.odai.architecturedemo.cats.model.Cats
import com.odai.architecturedemo.favourite.model.FavouriteCats

interface CatsView {

    fun attach(listener: CatsPresenter.CatClickedListener)

    fun display(cats: Cats)

    fun display(favouriteCats: FavouriteCats)

    fun showLoadingIndicator()

    fun showLoadingScreen()

    fun showData()

    fun showEmptyScreen()

    fun showErrorIndicator()

    fun showErrorScreen()

}
