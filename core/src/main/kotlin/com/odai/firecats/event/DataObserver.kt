package com.odai.firecats.event

import io.reactivex.functions.Consumer

interface  DataObserver<T>: Consumer<T> {
}
