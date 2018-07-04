package com.odai.firecats.favourite.model

data class FavouriteCats(val favourites: Map<Int, FavouriteState>) {

    fun put(p0: Pair<Int, FavouriteState>): FavouriteCats {
        return FavouriteCats(favourites.plus(p0))
    }

    fun isEmpty(): Boolean {
        return favourites.isEmpty()
    }

    fun getStatusFosr(cat: Int): FavouriteState {
        if (favourites.containsKey(cat)) {
            return favourites[cat]!!
        } else {
            return FavouriteState(FavouriteStatus.UN_FAVOURITE, ActionState.CONFIRMED)
        }
    }
}
