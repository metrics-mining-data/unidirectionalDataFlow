package com.odai.firecats.cats

import com.odai.firecats.cat.model.Cat
import com.odai.firecats.cats.displayer.CatsDisplayer
import com.odai.firecats.cats.model.Cats
import com.odai.firecats.cats.model.CatsState
import com.odai.firecats.cats.service.CatsServiceClient
import com.odai.firecats.event.Event
import com.odai.firecats.event.Status
import com.odai.firecats.favourite.model.ActionState
import com.odai.firecats.favourite.model.FavouriteCats
import com.odai.firecats.favourite.model.FavouriteState
import com.odai.firecats.favourite.model.FavouriteStatus
import com.odai.firecats.navigation.Navigator
import io.reactivex.processors.BehaviorProcessor
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CatsPresenterTest {

    var catsEventSubject: BehaviorProcessor<Event<CatsState>> = BehaviorProcessor.create()
    var service: CatsServiceClient = mock(CatsServiceClient::class.java)

    var displayer: CatsDisplayer = mock(CatsDisplayer::class.java)

    var navigator: Navigator = mock(Navigator::class.java)

    var presenter = CatsPresenter(service, navigator, displayer)

    @Before
    fun setUp() {
        setUpService()
        presenter = CatsPresenter(service, navigator, displayer)
    }

    @After
    fun tearDown() {
        reset(displayer, service)
    }

    @Test
    fun given_ThePresenterIsPresenting_on_EmissionOfALoadingEventWithNoData_it_ShouldPresentTheLoadingScreen() {
        givenThePresenterIsPresenting()

        catsEventSubject.onNext(Event(Status.LOADING, null, null))

        verify(displayer).displayLoading()
    }

    @Test
    fun given_ThePresenterIsPresenting_on_EmissionOfALoadingEventWithData_it_ShouldPresentTheLoadingIndicator() {
        givenThePresenterIsPresenting()

        val cats = Cats(listOf(Cat(42, "NewCat", "")))
        val favouriteCats = FavouriteCats(
                mapOf(Pair(42, FavouriteState(FavouriteStatus.FAVOURITE, ActionState.CONFIRMED)))
        )
        catsEventSubject.onNext(Event(Status.LOADING, CatsState(null, cats, favouriteCats), null))

        verify(displayer).displayLoading(cats, favouriteCats)
    }

    @Test
    fun given_ThePresenterIsPresenting_on_EmissionOfAnIdleEventWithNoData_it_ShouldPresentTheEmptyScreen() {
        givenThePresenterIsPresenting()

        catsEventSubject.onNext(Event(Status.IDLE, null, null))

        verify(displayer).displayEmpty()
    }

    @Test
    fun given_ThePresenterIsPresenting_on_EmissionOfAnIdleEventWithData_it_ShouldPresentData() {
        givenThePresenterIsPresenting()

        val cats = Cats(listOf(Cat(42, "NewCat", "")))
        val favouriteCats = FavouriteCats(
                mapOf(Pair(42, FavouriteState(FavouriteStatus.FAVOURITE, ActionState.CONFIRMED)))
        )
        catsEventSubject.onNext(Event(Status.IDLE, CatsState(null, cats, favouriteCats), null))

        verify(displayer).display(cats, favouriteCats)
    }

    @Test
    fun given_ThePresenterIsPresenting_on_EmissionOfAnErrorEventWithNoData_it_ShouldPresentTheErrorScreen() {
        givenThePresenterIsPresenting()

        catsEventSubject.onNext(Event(Status.ERROR, null, null))

        verify(displayer).displayError()
    }

    @Test
    fun given_ThePresenterIsPresenting_on_EmissionOfAnErrorEventWithData_it_ShouldPresentTheErrorIndicator() {
        givenThePresenterIsPresenting()

        val cats = Cats(listOf(Cat(42, "NewCat", "")))
        val favouriteCats = FavouriteCats(
                mapOf(Pair(42, FavouriteState(FavouriteStatus.FAVOURITE, ActionState.CONFIRMED)))
        )
        catsEventSubject.onNext(Event(Status.ERROR, CatsState(null, cats, favouriteCats), null))

        verify(displayer).displayError(cats, favouriteCats)
    }

    /*@Test
    fun given_ThePresenterIsPresenting_on_RetryClicked_it_ShouldRefreshTheService() {
        givenThePresenterIsPresenting()

        presenter.retryListener.onRetry()

        verify(service).refreshCats()
    }*/

    @Test
    fun given_ThePresenterStoppedPresenting_on_EmissionOfANewCat_it_ShouldNotPresentTheCatToTheView() {
        givenThePresenterStoppedPresenting()

        val cats = Cats(listOf(Cat(42, "NewCat", "")))
        val favouriteCats = FavouriteCats(
                mapOf(Pair(42, FavouriteState(FavouriteStatus.FAVOURITE, ActionState.CONFIRMED)))
        )
        catsEventSubject.onNext(Event(Status.IDLE, CatsState(null, cats, favouriteCats), null))

        verifyZeroInteractions(displayer)
    }

    @Test
    fun given_ThePresenterIsNotPresenting_on_StartPresenting_it_ShouldAttachListenerToTheView() {
        givenThePresenterIsNotPresenting()

        presenter.startPresenting()

        verify(displayer).attach(presenter.catClickedListener)
    }

    @Test
    fun given_ThePresenterIsPresenting_on_StopPresenting_it_ShouldDetachListenerToTheView() {
        givenThePresenterIsPresenting()

        presenter.stopPresenting()

        verify(displayer).detach(presenter.catClickedListener)
    }

    @Test
    fun given_ThePresenterIsNotPresenting_on_StartPresenting_it_ShouldSubscribeToTheEventStream() {
        givenThePresenterIsNotPresenting()

        presenter.startPresenting()

        assertTrue(catsEventSubject.hasSubscribers())
    }

    @Test
    fun given_ThePresenterIsPresenting_on_StopPresenting_it_ShouldUnsubscribeFromTheEventStream() {
        givenThePresenterIsPresenting()

        presenter.stopPresenting()

        assertFalse(catsEventSubject.hasSubscribers())
    }

    @Test
    fun given_ThePresenterIsPresenting_on_CatClicked_it_ShouldNavigateToTheClickedCat() {
        givenThePresenterIsPresenting()

        presenter.catClickedListener.onCatClicked(Cat(42, "NewCat", ""))

        verify(navigator).toCat(Cat(42, "NewCat", ""))
    }

    @Test
    fun given_ThePresenterIsPresenting_on_FavouriteClickedForCat_it_ShouldToggleFavouriteState() {
        givenThePresenterIsPresenting()

        presenter.catClickedListener.onFavouriteClicked(
                Cat(42, "NewCat", ""),
                FavouriteState(FavouriteStatus.FAVOURITE, ActionState.CONFIRMED)
        )

        verify(service).toggleFavouriteStatus(Cat(42, "NewCat", ""),
                FavouriteState(FavouriteStatus.FAVOURITE, ActionState.CONFIRMED))
    }

    private fun givenThePresenterIsPresenting() {
        presenter.startPresenting()
        reset(service)
    }

    private fun givenThePresenterIsNotPresenting() {
    }

    private fun givenThePresenterStoppedPresenting() {
        presenter.startPresenting()
        presenter.stopPresenting()
        reset(displayer)
    }

    private fun setUpService() {
        catsEventSubject = BehaviorProcessor.create()
        `when`(service.getCatsStateEvents()).thenReturn(catsEventSubject)
    }
}


