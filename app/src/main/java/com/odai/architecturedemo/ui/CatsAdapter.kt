package com.odai.architecturedemo.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.odai.architecturedemo.model.Cats
import com.odai.architecturedemo.R

class CatsAdapter(val layoutInflater: LayoutInflater, val listener: MainActivity.CatClickedListener, var cats: Cats, var favouriteCats: Cats) : RecyclerView.Adapter<CatsViewHolder>() {

    override fun onBindViewHolder(p0: CatsViewHolder, p1: Int) {
        val cat = cats.get(p1)
        p0.bind(cat, favouriteCats.contains(cat), listener)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CatsViewHolder? {
        return CatsViewHolder(layoutInflater.inflate(R.layout.cat_entry_view, p0, false) as TextView?);
    }

    override fun getItemCount(): Int {
        return cats.size()
    }

}
