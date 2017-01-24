package com.odai.firecats.cats.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.odai.firecats.R
import com.odai.firecats.cats.displayer.CatsDisplayer
import com.odai.firecats.cats.model.Cats
import com.odai.firecats.favourite.model.FavouriteCats

class CatsAdapter(
        private val layoutInflater: LayoutInflater,
        private val listener: AndroidCatsView.Listener,
        var cats: Cats,
        var favouriteCats: FavouriteCats
) : RecyclerView.Adapter<CatsViewHolder>() {

    override fun onBindViewHolder(p0: CatsViewHolder, p1: Int) {
        val cat = cats.get(p1)
        p0.bind(cat, favouriteCats.getStatusFor(cat.id), listener)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CatsViewHolder? {
        return CatsViewHolder(layoutInflater.inflate(R.layout.cat_entry_view, p0, false) as CatEntryView);
    }

    override fun getItemCount(): Int {
        return cats.size()
    }

}
