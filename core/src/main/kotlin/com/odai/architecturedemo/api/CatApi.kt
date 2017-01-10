package com.odai.architecturedemo.api

import com.odai.architecturedemo.cat.model.Cat
import com.odai.architecturedemo.cats.model.Cats
import io.reactivex.Flowable

interface CatApi {

    fun getCats(): Flowable<Cats>;

    fun getFavouriteCats(): Flowable<Cats>

    fun addToFavourite(cat: Cat): Flowable<Cat>

    fun removeFromFavourite(cat: Cat): Flowable<Cat>

}
