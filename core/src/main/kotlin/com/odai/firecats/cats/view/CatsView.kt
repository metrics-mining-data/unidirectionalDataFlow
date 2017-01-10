package com.odai.firecats.cats.view

import com.odai.firecats.cats.model.Cats
import com.odai.firecats.cats.CatsPresenter
import com.odai.firecats.favourite.model.FavouriteCats

interface CatsView {

    fun attach(listener: CatsPresenter.CatClickedListener)

    fun display(cats: Cats)

    fun display(favouriteCats: FavouriteCats)

}
