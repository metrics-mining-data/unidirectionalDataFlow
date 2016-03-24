package com.odai.architecturedemo.cats

import com.odai.architecturedemo.cat.model.Cat
import com.odai.architecturedemo.cats.model.Cats
import com.odai.architecturedemo.cats.usecase.CatsUseCase
import com.odai.architecturedemo.cats.view.CatsView
import com.odai.architecturedemo.event.Event
import com.odai.architecturedemo.event.Status
import com.odai.architecturedemo.favourite.model.FavouriteCats
import com.odai.architecturedemo.favourite.model.FavouriteState
import com.odai.architecturedemo.favourite.usecase.FavouriteCatsUseCase
import com.odai.architecturedemo.loading.LoadingView
import com.odai.architecturedemo.navigation.Navigator
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import rx.subjects.BehaviorSubject
import java.net.URI
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CatsPresenterTest {

    var catsSubject: BehaviorSubject<Cats> = BehaviorSubject.create()
    var catsEventSubject: BehaviorSubject<Event<Cats>> = BehaviorSubject.create()
    var useCase: CatsUseCase = mock(CatsUseCase::class.java)

    var favouriteCatsSubject: BehaviorSubject<FavouriteCats> = BehaviorSubject.create()
    var favouriteUseCase: FavouriteCatsUseCase = mock(FavouriteCatsUseCase::class.java)

    var view: CatsView = mock(CatsView::class.java)
    var loadingView: LoadingView = mock(LoadingView::class.java)

    var navigator: Navigator = mock(Navigator::class.java)

    var presenter = CatsPresenter(useCase, favouriteUseCase, navigator, view, loadingView)

    @Before
    fun setUp() {
        setUpUseCase()
        presenter = CatsPresenter(useCase, favouriteUseCase, navigator, view, loadingView)
    }

    @After
    fun tearDown() {
        reset(view, loadingView, useCase, favouriteUseCase)
    }

    @Test
    fun given_ThePresenterIsPresenting_on_EmissionOfNewCats_it_ShouldPresentTheCatsToTheView() {
        givenThePresenterIsPresenting()

        catsSubject.onNext(Cats(listOf(Cat(42, "NewCat", URI.create("")))))

        verify(view).display(Cats(listOf(Cat(42, "NewCat", URI.create("")))))
    }

    @Test
    fun given_ThePresenterIsPresenting_on_EmissionOfNewFavouritesCats_it_ShouldPresentTheCatsToTheView() {
        givenThePresenterIsPresenting()

        val favouriteCats = FavouriteCats(mapOf(Pair(Cat(42, "NewCat", URI.create("")), FavouriteState.FAVOURITE)))
        favouriteCatsSubject.onNext(favouriteCats)

        verify(view).display(favouriteCats)
    }

    @Test
    fun given_ThePresenterIsPresenting_on_EmissionOfALoadingEventWithNoData_it_ShouldPresentTheLoadingScreen() {
        givenThePresenterIsPresenting()

        catsEventSubject.onNext(Event(Status.LOADING, null, null))

        verify(loadingView).showLoadingScreen()
    }

    @Test
    fun given_ThePresenterIsPresenting_on_EmissionOfALoadingEventWithData_it_ShouldPresentTheLoadingIndicator() {
        givenThePresenterIsPresenting()

        catsEventSubject.onNext(Event(Status.LOADING, Cats(listOf(Cat(42, "NewCat", URI.create("")))), null))

        verify(loadingView).showLoadingIndicator()
    }

    @Test
    fun given_ThePresenterIsPresenting_on_EmissionOfAnIdleEventWithNoData_it_ShouldPresentTheEmptyScreen() {
        givenThePresenterIsPresenting()

        catsEventSubject.onNext(Event(Status.IDLE, null, null))

        verify(loadingView).showEmptyScreen()
    }

    @Test
    fun given_ThePresenterIsPresenting_on_EmissionOfAnIdleEventWithData_it_ShouldPresentData() {
        givenThePresenterIsPresenting()

        catsEventSubject.onNext(Event(Status.IDLE, Cats(listOf(Cat(42, "NewCat", URI.create("")))), null))

        verify(loadingView).showData()
    }

    @Test
    fun given_ThePresenterIsPresenting_on_EmissionOfAnErrorEventWithNoData_it_ShouldPresentTheErrorScreen() {
        givenThePresenterIsPresenting()

        catsEventSubject.onNext(Event(Status.ERROR, null, null))

        verify(loadingView).showErrorScreen()
    }

    @Test
    fun given_ThePresenterIsPresenting_on_EmissionOfAnErrorEventWithData_it_ShouldPresentTheErrorIndicator() {
        givenThePresenterIsPresenting()

        catsEventSubject.onNext(Event(Status.ERROR, Cats(listOf(Cat(42, "NewCat", URI.create("")))), null))

        verify(loadingView).showErrorIndicator()
    }

    @Test
    fun given_ThePresenterIsPresenting_on_RetryClicked_it_ShouldRefreshTheUseCase() {
        givenThePresenterIsPresenting()

        presenter.retryListener.onRetry()

        verify(useCase).refreshCats()
    }

    @Test
    fun given_ThePresenterStoppedPresenting_on_EmissionOfANewCat_it_ShouldNotPresentTheCatToTheView() {
        givenThePresenterStoppedPresenting()

        catsSubject.onNext(Cats(listOf(Cat(42, "NewCat", URI.create("")))))

        verifyZeroInteractions(view)
    }

    @Test
    fun given_ThePresenterStoppedPresenting_on_EmissionOfNewFavouritesCats_it_ShouldNotPresentTheCatsToTheView() {
        givenThePresenterStoppedPresenting()

        val favouriteCats = FavouriteCats(mapOf(Pair(Cat(42, "NewCat", URI.create("")), FavouriteState.FAVOURITE)))
        favouriteCatsSubject.onNext(favouriteCats)

        verifyZeroInteractions(view)
    }

    @Test
    fun given_ThePresenterStoppedPresenting_on_EmissionOfAnEvent_it_ShouldNotPresentToTheLoadingView() {
        givenThePresenterStoppedPresenting()

        catsEventSubject.onNext(Event(Status.LOADING, null, null))

        verifyZeroInteractions(loadingView)
    }

    @Test
    fun given_ThePresenterIsNotPresenting_on_StartPresenting_it_ShouldAttachListenerToTheLoadingView() {
        givenThePresenterIsNotPresenting()

        presenter.startPresenting()

        verify(loadingView).attach(presenter.retryListener)
    }

    @Test
    fun given_ThePresenterIsNotPresenting_on_StartPresenting_it_ShouldAttachListenerToTheView() {
        givenThePresenterIsNotPresenting()

        presenter.startPresenting()

        verify(view).attach(presenter.catClickedListener)
    }

    @Test
    fun given_ThePresenterIsNotPresenting_on_StartPresenting_it_ShouldSubscribeToTheCatStream() {
        givenThePresenterIsNotPresenting()

        presenter.startPresenting()

        assertTrue(catsSubject.hasObservers())
    }

    @Test
    fun given_ThePresenterIsNotPresenting_on_StartPresenting_it_ShouldSubscribeToTheEventStream() {
        givenThePresenterIsNotPresenting()

        presenter.startPresenting()

        assertTrue(catsEventSubject.hasObservers())
    }

    @Test
    fun given_ThePresenterIsNotPresenting_on_StartPresenting_it_ShouldSubscribeToTheFavouriteCatStream() {
        givenThePresenterIsNotPresenting()

        presenter.startPresenting()

        assertTrue(favouriteCatsSubject.hasObservers())
    }

    @Test
    fun given_ThePresenterIsPresenting_on_StopPresenting_it_ShouldUnsubscribeFromTheCatStream() {
        givenThePresenterIsPresenting()

        presenter.stopPresenting()

        assertFalse(catsSubject.hasObservers())
    }

    @Test
    fun given_ThePresenterIsPresenting_on_StopPresenting_it_ShouldUnsubscribeFromTheEventStream() {
        givenThePresenterIsPresenting()

        presenter.stopPresenting()

        assertFalse(catsEventSubject.hasObservers())
    }

    @Test
    fun given_ThePresenterIsPresenting_on_StopPresenting_it_ShouldUnsubscribeFromTheFavouriteCatStream() {
        givenThePresenterIsPresenting()

        presenter.stopPresenting()

        assertFalse(favouriteCatsSubject.hasObservers())
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

        presenter.catClickedListener.onFavouriteClicked(Cat(42, "NewCat", URI.create("")), FavouriteState.FAVOURITE)

        verify(favouriteUseCase).removeFromFavourite(Cat(42, "NewCat", URI.create("")))
    }

    @Test
    fun given_ThePresenterIsPresenting_on_FavouriteClickedForUnFavouriteCat_it_ShouldAddCatToFavourites() {
        givenThePresenterIsPresenting()

        presenter.catClickedListener.onFavouriteClicked(Cat(42, "NewCat", URI.create("")), FavouriteState.UN_FAVOURITE)

        verify(favouriteUseCase).addToFavourite(Cat(42, "NewCat", URI.create("")))
    }

    @Test
    fun given_ThePresenterIsPresenting_on_FavouriteClickedForPendingFavouriteCat_it_ShouldDoNothing() {
        givenThePresenterIsPresenting()

        presenter.catClickedListener.onFavouriteClicked(Cat(42, "NewCat", URI.create("")), FavouriteState.PENDING_FAVOURITE)

        verifyZeroInteractions(favouriteUseCase)
    }

    @Test
    fun given_ThePresenterIsPresenting_on_FavouriteClickedForPendingUnFavouriteCat_it_ShouldDoNothing() {
        givenThePresenterIsPresenting()

        presenter.catClickedListener.onFavouriteClicked(Cat(42, "NewCat", URI.create("")), FavouriteState.PENDING_UN_FAVOURITE)

        verifyZeroInteractions(favouriteUseCase)
    }

    private fun givenThePresenterIsPresenting() {
        presenter.startPresenting()
        reset(useCase, favouriteUseCase)
    }

    private fun givenThePresenterIsNotPresenting() {
    }

    private fun givenThePresenterStoppedPresenting() {
        presenter.startPresenting()
        reset(loadingView, view)
        presenter.stopPresenting()
    }

    private fun setUpUseCase() {
        catsSubject = BehaviorSubject.create()
        catsEventSubject = BehaviorSubject.create()
        favouriteCatsSubject = BehaviorSubject.create()
        `when`(useCase.getCats()).thenReturn(catsSubject)
        `when`(useCase.getCatsEvents()).thenReturn(catsEventSubject)
        `when`(favouriteUseCase.getFavouriteCats()).thenReturn(favouriteCatsSubject)
    }
}


