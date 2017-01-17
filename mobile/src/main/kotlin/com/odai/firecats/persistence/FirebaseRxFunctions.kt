package com.odai.firecats.persistence

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableEmitter

fun <T> observeValueEvents(db: DatabaseReference, marshaller: (DataSnapshot?) -> T): Flowable<T> {
    return Flowable.create({ emitter: FlowableEmitter<T> ->
        val eventListener = db.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                emitter.onError(p0?.toException() ?: Exception())
            }

            override fun onDataChange(p0: DataSnapshot?) {
                if (emitter.isCancelled) {
                    return
                }
                emitter.onNext(marshaller.invoke(p0))
            }
        })
        emitter.setCancellable { db.removeEventListener(eventListener) }
    }, BackpressureStrategy.LATEST)
}
