package com.odai.firecats.cats.model

import com.odai.firecats.favourite.model.FavouriteCats
import com.odai.firecats.login.model.User

data class CatsState(val user: User?, val cats: Cats?, val favourites: FavouriteCats)
