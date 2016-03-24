package com.odai.architecturedemo.cats.usecase

import com.odai.architecturedemo.cats.model.Cats

class AlwaysOutdatedCatsFreshnessChecker: CatsFreshnessChecker {

    override fun isFresh(cats: Cats): Boolean {
        return false
    }

}
