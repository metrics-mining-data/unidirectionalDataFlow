package com.odai.architecturedemo.cats.ui

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.odai.architecturedemo.R
import com.odai.architecturedemo.cats.model.Cat
import com.odai.architecturedemo.favourite.model.FavouriteState

class CatsViewHolder(itemView: TextView?) : RecyclerView.ViewHolder(itemView) {
    fun bind(cat: Cat, favouriteState: FavouriteState, listener: CatsActivity.CatClickedListener) {
        val textView = itemView as TextView
        textView.text = cat.name
        val color = getColor(favouriteState)
        textView.setBackgroundColor(itemView.resources.getColor(getColor(favouriteState), null));
        textView.setOnClickListener {
            listener.onCatClicked(cat)
        }
    }

    private fun getColor(favouriteState: FavouriteState) = when (favouriteState) {
        FavouriteState.FAVOURITE -> R.color.red
        FavouriteState.PENDING_FAVOURITE -> R.color.grey
        FavouriteState.PENDING_UN_FAVOURITE -> R.color.blue
        FavouriteState.UN_FAVOURITE -> android.R.color.transparent
    }

}
