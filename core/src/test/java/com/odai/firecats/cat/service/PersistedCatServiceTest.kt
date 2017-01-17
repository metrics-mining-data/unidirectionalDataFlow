package com.odai.firecats.cat.service

import com.odai.firecats.cat.model.Cat
import com.odai.firecats.cats.model.Cats
import com.odai.firecats.cats.service.CatsService
import com.odai.firecats.event.Event
import com.odai.firecats.event.Status
import io.reactivex.processors.BehaviorProcessor
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock


class PersistedCatServiceTest {

    var catsEventSubject: BehaviorProcessor<Event<Cats>> = BehaviorProcessor.create()
    var catsService: CatsService = mock(CatsService::class.java)

    var service: CatService = PersistedCatService(catsService)

    @Before
    fun setUp() {
        setUpService()
    }

    /*@Test
    fun given_aService_on_refreshCats_it_ShouldCallRefreshCatsOnTheCatsService() {
        service.refreshCat()

        verify(catsService).refreshCats()
    } */

    @Test
    fun given_aServiceWithCats_on_getCatWithId_it_ShouldCallReturnCatForId() {
        catsEventSubject.onNext(Event(Status.IDLE, Cats(listOf(Cat(42, "Foo", ""), Cat(24, "Bar", ""))), null))

        val cat = service.getCat(24).firstOrError().blockingGet()

        assertEquals(Cat(24, "Bar", ""), cat)
    }

    @Test
    fun given_aServiceWithCats_on_getCatEvents_it_ShouldCallReturnCatForId() {
        catsEventSubject.onNext(Event(Status.IDLE, Cats(listOf(Cat(42, "Foo", ""), Cat(24, "Bar", ""))), null))

        val catEvent = service.getCatEvents(42).firstOrError().blockingGet()

        assertEquals(Cat(42, "Foo", ""), catEvent.data)
    }

    @Test
    fun given_aServiceWithStatus_on_getCatEvents_it_ShouldCallReturnMatchingStatus() {
        catsEventSubject.onNext(Event(Status.IDLE, Cats(listOf(Cat(42, "Foo", ""), Cat(24, "Bar", ""))), null))

        val catEvent = service.getCatEvents(24).firstOrError().blockingGet()

        assertEquals(Status.IDLE, catEvent.status)
    }

    @Test
    fun given_aServiceWithError_on_getCatEvents_it_ShouldCallReturnMatchingError() {
        var error = Throwable("Failed")
        catsEventSubject.onNext(Event(Status.ERROR, Cats(listOf(Cat(42, "Foo", ""), Cat(24, "Bar", ""))), error))

        val catEvent = service.getCatEvents(24).firstOrError().blockingGet()

        assertEquals(Status.ERROR, catEvent.status)
        assertEquals(error, catEvent.error)
    }

    private fun setUpService() {
        catsEventSubject = BehaviorProcessor.create()
        `when`(catsService.getCatsEvents()).thenReturn(catsEventSubject)
    }

}
