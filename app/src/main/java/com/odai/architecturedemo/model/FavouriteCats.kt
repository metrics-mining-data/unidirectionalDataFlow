package com.odai.architecturedemo.model

class FavouriteCats(val favourites: Map<Cat, FavouriteState>) {

    fun put(p0: Pair<Cat, FavouriteState>): FavouriteCats {
        return FavouriteCats(favourites.plus(p0))
    }

    fun isEmpty(): Boolean {
        return favourites.isEmpty()
    }

    fun getStatusFor(cat: Cat): FavouriteState {
        if (favourites.containsKey(cat)) {
            return favourites[cat]!!
        } else {
            return FavouriteState.UN_FAVOURITE
        }
    }
}
