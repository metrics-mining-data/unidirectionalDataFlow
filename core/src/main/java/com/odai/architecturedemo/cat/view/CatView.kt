package com.odai.architecturedemo.cat.view

import com.odai.architecturedemo.cat.model.Cat

interface CatView {

    fun display(cat: Cat)

    fun showLoadingIndicator()

    fun showLoadingScreen()

    fun showData()

    fun showEmptyScreen()

    fun showErrorIndicator()

    fun showErrorScreen()

}
