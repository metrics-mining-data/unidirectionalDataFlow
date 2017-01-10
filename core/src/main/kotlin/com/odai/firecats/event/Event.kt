package com.odai.firecats.event

data class Event<T> (val status: Status, val data: T?, val error: Throwable?)
