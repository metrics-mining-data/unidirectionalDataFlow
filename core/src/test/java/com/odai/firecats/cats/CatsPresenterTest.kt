package com.odai.firecats.cats

import com.odai.firecats.cat.model.Cat
import com.odai.firecats.cats.displayer.CatsDisplayer
import com.odai.firecats.cats.model.Cats
import com.odai.firecats.cats.service.CatsService
import com.odai.firecats.event.Event
import com.odai.firecats.event.Status
import com.odai.firecats.favourite.model.ActionState
import com.odai.firecats.favourite.model.FavouriteCats
import com.odai.firecats.favourite.model.FavouriteState
import com.odai.firecats.favourite.model.FavouriteStatus
import com.odai.firecats.favourite.service.FavouriteCatsService
import com.odai.firecats.loading.LoadingDisplayer
import com.odai.firecats.navigation.Navigator
import io.reactivex.processors.BehaviorProcessor
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import java.net.URI
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CatsPresenterTest {

    var catsSubject: BehaviorProcessor<Cats> = BehaviorProcessor.create()
    var catsEventSubject: BehaviorProcessor<Event<Cats>> = BehaviorProcessor.create()
    var service: CatsService = mock(CatsService::class.java)

    var favouriteCatsSubject: BehaviorProcessor<FavouriteCats> = BehaviorProcessor.create()
    var favouriteService: FavouriteCatsService = mock(FavouriteCatsService::class.java)

    var displayer: CatsDisplayer = mock(CatsDisplayer::class.java)
    var loadingDisplayer: LoadingDisplayer = mock(LoadingDisplayer::class.java)

    var navigator: Navigator = mock(Navigator::class.java)

    var presenter = CatsPresenter(service, favouriteService, navigator, displayer, loadingDisplayer)

    @Before
    fun setUp() {
        setUpService()
        presenter = CatsPresenter(service, favouriteService, navigator, displayer, loadingDisplayer)
    }

    @After
    fun tearDown() {
        reset(displayer, loadingDisplayer, service, favouriteService)
    }

    @Test
    fun given_ThePresenterIsPresenting_on_EmissionOfNewCats_it_ShouldPresentTheCatsToTheView() {
        givenThePresenterIsPresenting()

        catsSubject.onNext(Cats(listOf(Cat(42, "NewCat", URI.create("")))))

        verify(displayer).display(Cats(listOf(Cat(42, "NewCat", URI.create("")))))
    }

    @Test
    fun given_ThePresenterIsPresenting_on_EmissionOfNewFavouritesCats_it_ShouldPresentTheCatsToTheView() {
        givenThePresenterIsPresenting()

        val favouriteCats = FavouriteCats(
                mapOf(Pair(Cat(42, "NewCat", URI.create("")), FavouriteState(FavouriteStatus.FAVOURITE, ActionState.CONFIRMED)))
        )
        favouriteCatsSubject.onNext(favouriteCats)

        verify(displayer).display(favouriteCats)
    }

    @Test
    fun given_ThePresenterIsPresenting_on_EmissionOfALoadingEventWithNoData_it_ShouldPresentTheLoadingScreen() {
        givenThePresenterIsPresenting()

        catsEventSubject.onNext(Event(Status.LOADING, null, null))

        verify(loadingDisplayer).showLoadingScreen()
    }

    @Test
    fun given_ThePresenterIsPresenting_on_EmissionOfALoadingEventWithData_it_ShouldPresentTheLoadingIndicator() {
        givenThePresenterIsPresenting()

        catsEventSubject.onNext(Event(Status.LOADING, Cats(listOf(Cat(42, "NewCat", URI.create("")))), null))

        verify(loadingDisplayer).showLoadingIndicator()
    }

    @Test
    fun given_ThePresenterIsPresenting_on_EmissionOfAnIdleEventWithNoData_it_ShouldPresentTheEmptyScreen() {
        givenThePresenterIsPresenting()

        catsEventSubject.onNext(Event(Status.IDLE, null, null))

        verify(loadingDisplayer).showEmptyScreen()
    }

    @Test
    fun given_ThePresenterIsPresenting_on_EmissionOfAnIdleEventWithData_it_ShouldPresentData() {
        givenThePresenterIsPresenting()

        catsEventSubject.onNext(Event(Status.IDLE, Cats(listOf(Cat(42, "NewCat", URI.create("")))), null))

        verify(loadingDisplayer).showData()
    }

    @Test
    fun given_ThePresenterIsPresenting_on_EmissionOfAnErrorEventWithNoData_it_ShouldPresentTheErrorScreen() {
        givenThePresenterIsPresenting()

        catsEventSubject.onNext(Event(Status.ERROR, null, null))

        verify(loadingDisplayer).showErrorScreen()
    }

    @Test
    fun given_ThePresenterIsPresenting_on_EmissionOfAnErrorEventWithData_it_ShouldPresentTheErrorIndicator() {
        givenThePresenterIsPresenting()

        catsEventSubject.onNext(Event(Status.ERROR, Cats(listOf(Cat(42, "NewCat", URI.create("")))), null))

        verify(loadingDisplayer).showErrorIndicator()
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

        catsSubject.onNext(Cats(listOf(Cat(42, "NewCat", URI.create("")))))

        verifyZeroInteractions(displayer)
    }

    @Test
    fun given_ThePresenterStoppedPresenting_on_EmissionOfNewFavouritesCats_it_ShouldNotPresentTheCatsToTheView() {
        givenThePresenterStoppedPresenting()

        val favouriteCats = FavouriteCats(
                mapOf(Pair(Cat(42, "NewCat", URI.create("")), FavouriteState(FavouriteStatus.FAVOURITE, ActionState.CONFIRMED)))
        )
        favouriteCatsSubject.onNext(favouriteCats)

        verifyZeroInteractions(displayer)
    }

    @Test
    fun given_ThePresenterStoppedPresenting_on_EmissionOfAnEvent_it_ShouldNotPresentToTheLoadingView() {
        givenThePresenterStoppedPresenting()

        catsEventSubject.onNext(Event(Status.LOADING, null, null))

        verifyZeroInteractions(loadingDisplayer)
    }

    @Test
    fun given_ThePresenterIsNotPresenting_on_StartPresenting_it_ShouldAttachListenerToTheLoadingView() {
        givenThePresenterIsNotPresenting()

        presenter.startPresenting()

        verify(loadingDisplayer).attach(presenter.retryListener)
    }

    @Test
    fun given_ThePresenterIsNotPresenting_on_StartPresenting_it_ShouldAttachListenerToTheView() {
        givenThePresenterIsNotPresenting()

        presenter.startPresenting()

        verify(displayer).attach(presenter.catClickedListener)
    }

    @Test
    fun given_ThePresenterIsNotPresenting_on_StartPresenting_it_ShouldSubscribeToTheCatStream() {
        givenThePresenterIsNotPresenting()

        presenter.startPresenting()

        assertTrue(catsSubject.hasSubscribers())
    }

    @Test
    fun given_ThePresenterIsNotPresenting_on_StartPresenting_it_ShouldSubscribeToTheEventStream() {
        givenThePresenterIsNotPresenting()

        presenter.startPresenting()

        assertTrue(catsEventSubject.hasSubscribers())
    }

    @Test
    fun given_ThePresenterIsNotPresenting_on_StartPresenting_it_ShouldSubscribeToTheFavouriteCatStream() {
        givenThePresenterIsNotPresenting()

        presenter.startPresenting()

        assertTrue(favouriteCatsSubject.hasSubscribers())
    }

    @Test
    fun given_ThePresenterIsPresenting_on_StopPresenting_it_ShouldUnsubscribeFromTheCatStream() {
        givenThePresenterIsPresenting()

        presenter.stopPresenting()

        assertFalse(catsSubject.hasSubscribers())
    }

    @Test
    fun given_ThePresenterIsPresenting_on_StopPresenting_it_ShouldUnsubscribeFromTheEventStream() {
        givenThePresenterIsPresenting()

        presenter.stopPresenting()

        assertFalse(catsEventSubject.hasSubscribers())
    }

    @Test
    fun given_ThePresenterIsPresenting_on_StopPresenting_it_ShouldUnsubscribeFromTheFavouriteCatStream() {
        givenThePresenterIsPresenting()

        presenter.stopPresenting()

        assertFalse(favouriteCatsSubject.hasSubscribers())
    }

    @Test
    fun given_ThePresenterIsPresenting_on_CatClicked_it_ShouldNavigateToTheClickedCat() {
        givenThePresenterIsPresenting()

        presenter.catClickedListener.onCatClicked(Cat(42, "NewCat", URI.create("")))

        verify(navigator).toCat(Cat(42, "NewCat", URI.create("")))
    }

    @Test
    fun given_ThePresenterIsPresenting_on_FavouriteClickedForFavouriteCat_it_ShouldRemoveCatFromFavourites() {
        givenThePresenterIsPresenting()

        presenter.catClickedListener.onFavouriteClicked(
                Cat(42, "NewCat", URI.create("")),
                FavouriteState(FavouriteStatus.FAVOURITE, ActionState.CONFIRMED)
        )

        verify(favouriteService).removeFromFavourite(Cat(42, "NewCat", URI.create("")))
    }

    @Test
    fun given_ThePresenterIsPresenting_on_FavouriteClickedForUnFavouriteCat_it_ShouldAddCatToFavourites() {
        givenThePresenterIsPresenting()

        presenter.catClickedListener.onFavouriteClicked(
                Cat(42, "NewCat", URI.create("")),
                FavouriteState(FavouriteStatus.UN_FAVOURITE, ActionState.CONFIRMED)
        )

        verify(favouriteService).addToFavourite(Cat(42, "NewCat", URI.create("")))
    }

    @Test
    fun given_ThePresenterIsPresenting_on_FavouriteClickedForPendingFavouriteCat_it_ShouldDoNothing() {
        givenThePresenterIsPresenting()

        presenter.catClickedListener.onFavouriteClicked(
                Cat(42, "NewCat", URI.create("")),
                FavouriteState(FavouriteStatus.FAVOURITE, ActionState.PENDING)
        )

        verifyZeroInteractions(favouriteService)
    }

    @Test
    fun given_ThePresenterIsPresenting_on_FavouriteClickedForPendingUnFavouriteCat_it_ShouldDoNothing() {
        givenThePresenterIsPresenting()

        presenter.catClickedListener.onFavouriteClicked(
                Cat(42, "NewCat", URI.create("")),
                FavouriteState(FavouriteStatus.UN_FAVOURITE, ActionState.PENDING)
        )

        verifyZeroInteractions(favouriteService)
    }

    private fun givenThePresenterIsPresenting() {
        presenter.startPresenting()
        reset(service, favouriteService)
    }

    private fun givenThePresenterIsNotPresenting() {
    }

    private fun givenThePresenterStoppedPresenting() {
        presenter.startPresenting()
        reset(loadingDisplayer, displayer)
        presenter.stopPresenting()
    }

    private fun setUpService() {
        catsSubject = BehaviorProcessor.create()
        catsEventSubject = BehaviorProcessor.create()
        favouriteCatsSubject = BehaviorProcessor.create()
        `when`(service.getCats()).thenReturn(catsSubject)
        `when`(service.getCatsEvents()).thenReturn(catsEventSubject)
        `when`(favouriteService.getFavouriteCats()).thenReturn(favouriteCatsSubject)
    }
}


