package com.odai.architecturedemo.event

import io.reactivex.functions.Consumer

interface  DataObserver<T>: Consumer<T> {
}
