package com.odai.architecturedemo.cat.usecase

import com.odai.architecturedemo.cat.model.Cat
import com.odai.architecturedemo.cats.model.Cats
import com.odai.architecturedemo.cats.usecase.CatsUseCase
import com.odai.architecturedemo.event.Event
import com.odai.architecturedemo.event.Status
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.*
import rx.subjects.BehaviorSubject


class PersistedCatUseCaseTest {

    var catsEventSubject: BehaviorSubject<Event<Cats>> = BehaviorSubject.create()
    var catsUseCase: CatsUseCase = mock(CatsUseCase::class.java)

    var useCase: CatUseCase = PersistedCatUseCase(catsUseCase)

    @Before
    fun setUp() {
        setUpUseCase()
    }

    @Test
    fun given_aUseCase_on_refreshCats_it_ShouldCallRefreshCatsOnTheCatsUseCase() {
        useCase.refreshCat()

        verify(catsUseCase).refreshCats()
    }

    @Test
    fun given_aUseCaseWithCats_on_getCatWithId_it_ShouldCallReturnCatForId() {
        catsEventSubject.onNext(Event(Status.IDLE, Cats(listOf(Cat(42, "Foo"), Cat(24, "Bar"))), null))

        val cat = useCase.getCat(24).toBlocking().first()

        assertEquals(Cat(24, "Bar"), cat)
    }

    @Test
    fun given_aUseCaseWithCats_on_getCatEvents_it_ShouldCallReturnCatForId() {
        catsEventSubject.onNext(Event(Status.IDLE, Cats(listOf(Cat(42, "Foo"), Cat(24, "Bar"))), null))

        val catEvent = useCase.getCatEvents(42).toBlocking().first()

        assertEquals(Cat(42, "Foo"), catEvent.data)
    }

    @Test
    fun given_aUseCaseWithStatus_on_getCatEvents_it_ShouldCallReturnMatchingStatus() {
        catsEventSubject.onNext(Event(Status.IDLE, Cats(listOf(Cat(42, "Foo"), Cat(24, "Bar"))), null))

        val catEvent = useCase.getCatEvents(24).toBlocking().first()

        assertEquals(Status.IDLE, catEvent.status)
    }

    @Test
    fun given_aUseCaseWithError_on_getCatEvents_it_ShouldCallReturnMatchingError() {
        var error = Throwable("Failed")
        catsEventSubject.onNext(Event(Status.ERROR, Cats(listOf(Cat(42, "Foo"), Cat(24, "Bar"))), error))

        val catEvent = useCase.getCatEvents(24).toBlocking().first()

        assertEquals(Status.ERROR, catEvent.status)
        assertEquals(error, catEvent.error)
    }

    private fun setUpUseCase() {
        catsEventSubject = BehaviorSubject.create()
        `when`(catsUseCase.getCatsEvents()).thenReturn(catsEventSubject)
    }

}
