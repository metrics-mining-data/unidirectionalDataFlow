package com.odai.firecats.cats.view

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.odai.firecats.R
import com.odai.firecats.cats.CatsPresenter
import com.odai.firecats.cat.model.Cat
import com.odai.firecats.favourite.model.FavouriteState

class CatsViewHolder(itemView: CatEntryView) : RecyclerView.ViewHolder(itemView) {

    fun bind(cat: Cat, favouriteState: FavouriteState, listener: CatsPresenter.CatClickedListener) {
        (itemView as CatEntryView).display(cat, favouriteState, listener)
    }

}
