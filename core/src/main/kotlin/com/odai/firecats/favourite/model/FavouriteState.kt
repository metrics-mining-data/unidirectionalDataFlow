package com.odai.firecats.favourite.model

data class FavouriteState(val status: FavouriteStatus = FavouriteStatus.UN_FAVOURITE, val state: ActionState = ActionState.CONFIRMED)
