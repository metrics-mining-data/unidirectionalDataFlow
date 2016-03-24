package com.odai.architecturedemo.cats.usecase

import com.odai.architecturedemo.cats.model.Cats

interface CatsFreshnessChecker {

    fun isFresh(cats: Cats): Boolean

}
