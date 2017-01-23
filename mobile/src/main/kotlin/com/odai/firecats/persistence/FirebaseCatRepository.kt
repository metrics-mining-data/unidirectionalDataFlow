package com.odai.firecats.persistence

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.odai.firecats.cat.model.Cat
import com.odai.firecats.cats.model.Cats
import com.odai.firecats.favourite.model.FavouriteCats
import com.odai.firecats.favourite.model.FavouriteState
import com.odai.firecats.login.model.User
import io.reactivex.Flowable
import java.lang.Integer.parseInt

class FirebaseCatRepository(val db: DatabaseReference) : CatRepository {

    override fun observeCats(): Flowable<Cats> {
        return observeValueEvents(db.child("cats")) {
            val catsEntries = it?.children ?: emptyList()
            Cats(catsEntries.map { it.getValue(Cat::class.java) })
        }.doOnError {
            Log.e("FireCats", "!!!! Read Failed", it)
        }
    }

    override fun observeFavouriteCats(user: User): Flowable<FavouriteCats> {
        return observeValueEvents(db.child(user.id).child("favourites")) {
            val catsEntries = it?.children ?: emptyList()
            return@observeValueEvents catsEntries.fold(FavouriteCats(emptyMap())) { acc, it ->
                acc.put(Pair<Int, FavouriteState>(parseInt(it.key), it.getValue(FavouriteState::class.java)))
            }
        }.doOnError {
            Log.e("FireCats", "!!!! Read Failed", it)
        }
    }

    override fun saveCatFavoriteStatus(user: User, it: Pair<Int, FavouriteState>) {
        db.child(user.id).child("favourites").child(it.first.toString()).setValue(it.second)
    }

}
