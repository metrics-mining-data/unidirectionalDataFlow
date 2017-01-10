package com.odai.firecats.cats.service

import com.odai.firecats.cats.model.Cats
import com.odai.firecats.favourite.model.FavouriteCats

interface CatsFreshnessChecker {

    fun isFresh(cats: Cats): Boolean

    fun isFresh(cats: FavouriteCats): Boolean

}
