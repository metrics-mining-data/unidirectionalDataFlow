package com.odai.architecturedemo.model

data class Cats (val list: List<Cat>) {

    fun size(): Int {
        return list.size;
    }

    fun get(p1: Int): Cat {
        return list[p1]
    }

    fun contains(cat: Cat): Boolean {
        return list.contains(cat)
    }

    fun add(cat: Cat): Cats {
        return Cats(list.plus(cat))
    }

    fun remove(cat: Cat): Cats {
        return Cats(list.minus(cat))
    }

    fun isEmpty(): Boolean {
        return list.isEmpty();
    }

};
