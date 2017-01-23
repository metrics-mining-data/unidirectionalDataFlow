package com.odai.firecats.cat

import com.odai.firecats.cat.displayer.CatDisplayer
import com.odai.firecats.cat.model.Cat
import com.odai.firecats.cat.service.CatService
import com.odai.firecats.event.Event
import com.odai.firecats.event.Status
import com.odai.firecats.loading.LoadingDisplayer
import io.reactivex.processors.BehaviorProcessor
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.*
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CatPresenterTest {

    var catSubject: BehaviorProcessor<Cat> = BehaviorProcessor.create()
    var catEventSubject: BehaviorProcessor<Event<Cat>> = BehaviorProcessor.create()
    var service: CatService = mock(CatService::class.java)

    var displayer: CatDisplayer = mock(CatDisplayer::class.java)
    var loadingDisplayer: LoadingDisplayer = mock(LoadingDisplayer::class.java)

    var presenter = CatPresenter(42, service, displayer, loadingDisplayer)

    @Before
    fun setUp() {
        setUpService()
        presenter = CatPresenter(42, service, displayer, loadingDisplayer)
    }

    @After
    fun tearDown() {
        reset(displayer, loadingDisplayer, service)
    }

    @Test
    fun given_ThePresenterIsPresenting_on_EmissionOfANewCat_it_ShouldPresentTheCatToTheView() {
        givenThePresenterIsPresenting()

        catSubject.onNext(Cat(42, "NewCat", ""))

        verify(displayer).display(Cat(42, "NewCat", ""))
    }

    @Test
    fun given_ThePresenterIsPresenting_on_EmissionOfALoadingEventWithNoData_it_ShouldPresentTheLoadingScreen() {
        givenThePresenterIsPresenting()

        catEventSubject.onNext(Event(Status.LOADING, null, null))

        verify(loadingDisplayer).showLoadingScreen()
    }

    @Test
    fun given_ThePresenterIsPresenting_on_EmissionOfALoadingEventWithData_it_ShouldPresentTheLoadingIndicator() {
        givenThePresenterIsPresenting()

        catEventSubject.onNext(Event(Status.LOADING, Cat(42, "NewCat", ""), null))

        verify(loadingDisplayer).showLoadingIndicator()
    }

    @Test
    fun given_ThePresenterIsPresenting_on_EmissionOfAnIdleEventWithNoData_it_ShouldPresentTheEmptyScreen() {
        givenThePresenterIsPresenting()

        catEventSubject.onNext(Event(Status.IDLE, null, null))

        verify(loadingDisplayer).showEmptyScreen()
    }

    @Test
    fun given_ThePresenterIsPresenting_on_EmissionOfAnIdleEventWithData_it_ShouldPresentData() {
        givenThePresenterIsPresenting()

        catEventSubject.onNext(Event(Status.IDLE, Cat(42, "NewCat", ""), null))

        verify(loadingDisplayer).showData()
    }

    @Test
    fun given_ThePresenterIsPresenting_on_EmissionOfAnErrorEventWithNoData_it_ShouldPresentTheErrorScreen() {
        givenThePresenterIsPresenting()

        catEventSubject.onNext(Event(Status.ERROR, null, null))

        verify(loadingDisplayer).showErrorScreen()
    }

    @Test
    fun given_ThePresenterIsPresenting_on_EmissionOfAnErrorEventWithData_it_ShouldPresentTheErrorIndicator() {
        givenThePresenterIsPresenting()

        catEventSubject.onNext(Event(Status.ERROR, Cat(42, "NewCat", ""), null))

        verify(loadingDisplayer).showErrorIndicator()
    }

    /*@Test
    fun given_ThePresenterIsPresenting_on_RetryClicked_it_ShouldRefreshTheService() {
        givenThePresenterIsPresenting()

        presenter.retryListener.onRetry()

        verify(service).refreshCat()
    } */

    @Test
    fun given_ThePresenterStoppedPresenting_on_EmissionOfANewCat_it_ShouldNotPresentTheCatToTheView() {
        givenThePresenterStoppedPresenting()

        catSubject.onNext(Cat(42, "NewCat", ""))

        verifyZeroInteractions(displayer)
    }

    @Test
    fun given_ThePresenterStoppedPresenting_on_EmissionOfAnEvent_it_ShouldNotPresentToTheLoadingView() {
        givenThePresenterStoppedPresenting()

        catEventSubject.onNext(Event(Status.LOADING, null, null))

        verifyZeroInteractions(loadingDisplayer)
    }

    @Test
    fun given_ThePresenterIsNotPresenting_on_StartPresenting_it_ShouldAttachListenerToTheLoadingView() {
        givenThePresenterIsNotPresenting()

        presenter.startPresenting()

        verify(loadingDisplayer).attach(presenter.retryListener)
    }

    @Test
    fun given_ThePresenterIsNotPresenting_on_StartPresenting_it_ShouldSubscribeToTheCatStream() {
        givenThePresenterIsNotPresenting()

        presenter.startPresenting()

        assertTrue(catSubject.hasSubscribers())
    }

    @Test
    fun given_ThePresenterIsNotPresenting_on_StartPresenting_it_ShouldSubscribeToTheEventStream() {
        givenThePresenterIsNotPresenting()

        presenter.startPresenting()

        assertTrue(catEventSubject.hasSubscribers())
    }

    @Test
    fun given_ThePresenterIsPresenting_on_StopPresenting_it_ShouldUnsubscribeFromTheCatStream() {
        givenThePresenterIsPresenting()

        presenter.stopPresenting()

        assertFalse(catSubject.hasSubscribers())
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
        reset(loadingDisplayer)
        presenter.stopPresenting()
    }

    private fun setUpService() {
        catSubject = BehaviorProcessor.create()
        catEventSubject = BehaviorProcessor.create()
        `when`(service.getCat(Mockito.anyInt())).thenReturn(catSubject)
        `when`(service.getCatEvents(Mockito.anyInt())).thenReturn(catEventSubject)
    }
}


