package com.odai.firecats.cat.displayer

import com.odai.firecats.cat.model.Cat

interface CatDisplayer {

    fun display(cat: Cat)

    fun displayEmpty()

    fun displayLoading()

    fun displayLoading(cat: Cat)

    fun displayError()

    fun displayError(cat: Cat)


}
