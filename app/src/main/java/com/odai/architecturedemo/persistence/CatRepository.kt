package com.odai.architecturedemo.persistence

import com.odai.architecturedemo.model.Cat
import com.odai.architecturedemo.model.Cats
import rx.Observable

interface CatRepository {

    fun saveCats(cats: Cats): Unit

    fun readCats(): Observable<Cats>

    fun readFavouriteCats(): Observable<Cats>

    fun addToFavourite(cat: Cat): Unit

    fun removeFromFavourite(cat: Cat): Unit

    fun saveFavouriteCats(cats: Cats)

}
