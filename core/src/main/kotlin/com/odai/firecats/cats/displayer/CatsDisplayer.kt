package com.odai.firecats.cats.displayer

import com.odai.firecats.cat.model.Cat
import com.odai.firecats.cats.model.Cats
import com.odai.firecats.favourite.model.FavouriteCats
import com.odai.firecats.favourite.model.FavouriteState

interface CatsDisplayer {

    fun attach(listener: CatsActionListener)

    fun detach(listener: CatsActionListener)

    fun display(cats: Cats, favouriteCats: FavouriteCats)

    fun displayEmpty()

    fun displayLoading()

    fun displayLoading(cats: Cats, favouriteCats: FavouriteCats)

    fun displayError()

    fun displayError(cats: Cats, favouriteCats: FavouriteCats)

    interface CatsActionListener {
        fun onFavouriteClicked(cat: Cat, state: FavouriteState)
        fun onCatClicked(cat: Cat)
        fun onRetry()
    }

}
