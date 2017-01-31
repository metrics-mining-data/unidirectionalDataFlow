package com.odai.firecats.cat

import com.odai.firecats.cat.displayer.CatDisplayer
import com.odai.firecats.cat.model.Cat
import com.odai.firecats.cat.service.CatService
import com.odai.firecats.event.Event
import com.odai.firecats.event.Status
import io.reactivex.processors.BehaviorProcessor
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.*
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CatPresenterTest {

    var catEventSubject: BehaviorProcessor<Event<Cat>> = BehaviorProcessor.create()
    var service: CatService = mock(CatService::class.java)

    var displayer: CatDisplayer = mock(CatDisplayer::class.java)

    var presenter = CatPresenter(42, service, displayer)

    @Before
    fun setUp() {
        setUpService()
        presenter = CatPresenter(42, service, displayer)
    }

    @After
    fun tearDown() {
        reset(displayer, service)
    }

    @Test
    fun given_ThePresenterIsPresenting_on_EmissionOfALoadingEventWithNoData_it_ShouldPresentTheLoadingScreen() {
        givenThePresenterIsPresenting()

        catEventSubject.onNext(Event(Status.LOADING, null, null))

        verify(displayer).displayLoading()
    }

    @Test
    fun given_ThePresenterIsPresenting_on_EmissionOfALoadingEventWithData_it_ShouldPresentTheLoadingIndicator() {
        givenThePresenterIsPresenting()

        catEventSubject.onNext(Event(Status.LOADING, Cat(42, "NewCat", ""), null))

        verify(displayer).displayLoading(Cat(42, "NewCat", ""))
    }

    @Test
    fun given_ThePresenterIsPresenting_on_EmissionOfAnIdleEventWithNoData_it_ShouldPresentTheEmptyScreen() {
        givenThePresenterIsPresenting()

        catEventSubject.onNext(Event(Status.IDLE, null, null))

        verify(displayer).displayEmpty()
    }

    @Test
    fun given_ThePresenterIsPresenting_on_EmissionOfAnIdleEventWithData_it_ShouldPresentData() {
        givenThePresenterIsPresenting()

        catEventSubject.onNext(Event(Status.IDLE, Cat(42, "NewCat", ""), null))

        verify(displayer).display(Cat(42, "NewCat", ""))
    }

    @Test
    fun given_ThePresenterIsPresenting_on_EmissionOfAnErrorEventWithNoData_it_ShouldPresentTheErrorScreen() {
        givenThePresenterIsPresenting()

        catEventSubject.onNext(Event(Status.ERROR, null, null))

        verify(displayer).displayError()
    }

    @Test
    fun given_ThePresenterIsPresenting_on_EmissionOfAnErrorEventWithData_it_ShouldPresentTheErrorIndicator() {
        givenThePresenterIsPresenting()

        catEventSubject.onNext(Event(Status.ERROR, Cat(42, "NewCat", ""), null))

        verify(displayer).displayError(Cat(42, "NewCat", ""))
    }

    /*@Test
    fun given_ThePresenterIsPresenting_on_RetryClicked_it_ShouldRefreshTheService() {
        givenThePresenterIsPresenting()

        presenter.retryListener.onRetry()

        verify(service).refreshCat()
    } */

    @Test
    fun given_ThePresenterStoppedPresenting_on_EmissionOfAnEvent_it_ShouldNotPresentToTheLoadingView() {
        givenThePresenterStoppedPresenting()

        catEventSubject.onNext(Event(Status.LOADING, null, null))

        verifyZeroInteractions(displayer)
    }

    @Test
    fun given_ThePresenterIsNotPresenting_on_StartPresenting_it_ShouldSubscribeToTheEventStream() {
        givenThePresenterIsNotPresenting()

        presenter.startPresenting()

        assertTrue(catEventSubject.hasSubscribers())
    }

    @Test
    fun given_ThePresenterIsPresenting_on_StopPresenting_it_ShouldUnsubscribeFromTheEventStream() {
        givenThePresenterIsPresenting()

        presenter.stopPresenting()

        assertFalse(catEventSubject.hasSubscribers())
    }

    private fun givenThePresenterIsPresenting() {
        presenter.startPresenting()
    }

    private fun givenThePresenterIsNotPresenting() {
    }

    private fun givenThePresenterStoppedPresenting() {
        presenter.startPresenting()
        reset(displayer)
        presenter.stopPresenting()
    }

    private fun setUpService() {
        catEventSubject = BehaviorProcessor.create()
        `when`(service.getCatEvents(Mockito.anyInt())).thenReturn(catEventSubject)
    }
}


