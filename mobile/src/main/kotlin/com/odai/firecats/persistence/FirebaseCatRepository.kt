package com.odai.firecats.persistence

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.odai.firecats.cat.model.Cat
import com.odai.firecats.cats.model.Cats
import com.odai.firecats.favourite.model.FavouriteCats
import com.odai.firecats.favourite.model.FavouriteState
import io.reactivex.Flowable

class FirebaseCatRepository(val db: DatabaseReference) : CatRepository {

    override fun observeCats(): Flowable<Cats> {
        return observeValueEvents(db.child("cats")) {
            val catsEntries = it?.children ?: emptyList()
            Cats(catsEntries.map { it.getValue(Cat::class.java) })
        }.doOnError {
            Log.e("FireCats", "!!!! Read Failed", it)
        }
    }

    override fun observeFavouriteCats(): Flowable<FavouriteCats> {
        return Flowable.empty()
    }

    override fun saveCatFavoriteStatus(it: Pair<Cat, FavouriteState>) {
        // Ignore for now
    }

}
