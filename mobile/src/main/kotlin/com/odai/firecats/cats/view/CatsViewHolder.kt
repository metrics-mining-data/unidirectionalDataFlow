package com.odai.firecats.cats.view

import android.support.v7.widget.RecyclerView
import com.odai.firecats.cat.model.Cat
import com.odai.firecats.favourite.model.FavouriteState

class CatsViewHolder(itemView: CatEntryView) : RecyclerView.ViewHolder(itemView) {

    fun bind(cat: Cat, favouriteState: FavouriteState, listener: AndroidCatsView.Listener) {
        (itemView as CatEntryView).display(cat, favouriteState, listener)
    }

}
