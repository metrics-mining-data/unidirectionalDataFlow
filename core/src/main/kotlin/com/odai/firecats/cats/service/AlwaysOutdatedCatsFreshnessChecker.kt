package com.odai.firecats.cats.service

import com.odai.firecats.cats.model.Cats
import com.odai.firecats.favourite.model.FavouriteCats

class AlwaysOutdatedCatsFreshnessChecker: CatsFreshnessChecker {

    override fun isFresh(cats: Cats): Boolean {
        return false
    }

    override fun isFresh(cats: FavouriteCats): Boolean {
        return false
    }

}
