package com.odai.firecats.navigation

import com.odai.firecats.cat.model.Cat

interface Navigator {

    fun toCat(cat: Cat)

    fun toCats()

}
