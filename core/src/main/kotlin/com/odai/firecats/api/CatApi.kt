package com.odai.firecats.api

import com.odai.firecats.cat.model.Cat
import com.odai.firecats.cats.model.Cats
import io.reactivex.Flowable

interface CatApi {

    fun getCats(): Flowable<Cats>;

    fun getFavouriteCats(): Flowable<Cats>

    fun addToFavourite(cat: Cat): Flowable<Cat>

    fun removeFromFavourite(cat: Cat): Flowable<Cat>

}
