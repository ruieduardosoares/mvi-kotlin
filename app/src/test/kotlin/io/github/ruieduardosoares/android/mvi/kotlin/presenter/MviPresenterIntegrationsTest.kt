package io.github.ruieduardosoares.android.mvi.kotlin.presenter

import io.github.ruieduardosoares.android.mvi.kotlin.MviView
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.exceptions.CompositeException
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import org.amshove.kluent.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.firstValue
import org.mockito.kotlin.verify

@ExtendWith(MockitoExtension::class)
internal class MviPresenterIntegrationsTest {

    @Mock
    lateinit var mGlobalErrorHandler: Consumer<Throwable>

    @Captor
    lateinit var mThrowableCaptor: ArgumentCaptor<Throwable>

    private interface TestView : MviView<Int> {

        fun testIntentOne(): Observable<Unit>
    }

    @BeforeEach
    internal fun setUp() {
        RxJavaPlugins.setErrorHandler(mGlobalErrorHandler)
    }

    @AfterEach
    internal fun tearDown() {
        RxJavaPlugins.setErrorHandler(null)
    }

    @Test
    internal fun attachView_whenAttachDetachAttach_thenBindIntentsOnlyCalledOnce() {

        //Given
        val isViewPermanentlyDetached = false
        var bindIntentsCounter = 0
        val view = object : TestView {
            override fun testIntentOne(): Observable<Unit> = Observable.just(Unit)
            override fun render(state: Int) {}
        }
        val mviPresenter = object : AbstractMviPresenter<Int, TestView>() {
            override fun bindIntents(): Observable<Int> {
                bindIntentsCounter++
                return Observable.empty()
            }
        }

        //When
        mviPresenter.attachView(view)
        mviPresenter.detachView(isViewPermanentlyDetached)
        mviPresenter.attachView(view)

        //Then
        bindIntentsCounter.shouldBeEqualTo(1)
    }

    @Test
    internal fun attachView_whenFirstAttached_thenRenderIntent() {

        //Given
        var renderCalledCount = 0
        val view = object : TestView {
            override fun testIntentOne(): Observable<Unit> = Observable.just(Unit)
            override fun render(state: Int) {
                renderCalledCount++
            }
        }

        val mviPresenter = object : AbstractMviPresenter<Int, TestView>() {
            override fun bindIntents(): Observable<Int> {
                return intent().create(TestView::testIntentOne).map { 1 }
            }
        }

        //When
        mviPresenter.attachView(view)

        //Then
        renderCalledCount.shouldBeEqualTo(1)
    }

    @Test
    internal fun attachView_whenAttachDetachMultipleTimes_thenRenderOnlyLastItem() {

        //Given
        val firstValue = 1
        val lastValue = 85
        var lastRenderedValue = 0
        val isViewPermanentlyDetached = false
        val view = object : TestView {
            override fun testIntentOne(): Observable<Unit> = Observable.just(Unit)
            override fun render(state: Int) {
                lastRenderedValue = state
            }
        }
        val mviPresenter = object : AbstractMviPresenter<Int, TestView>() {
            override fun bindIntents(): Observable<Int> {
                return intent().create(TestView::testIntentOne)
                    .map { lastValue }
                    .startWithItem(firstValue)
            }
        }

        //When
        mviPresenter.attachView(view)
        mviPresenter.detachView(isViewPermanentlyDetached)
        mviPresenter.attachView(view)
        mviPresenter.detachView(isViewPermanentlyDetached)
        mviPresenter.attachView(view)
        mviPresenter.detachView(isViewPermanentlyDetached)
        mviPresenter.attachView(view)
        mviPresenter.detachView(isViewPermanentlyDetached)

        //Then
        lastRenderedValue.shouldBeEqualTo(lastValue)
    }

    @Test
    internal fun attachView_whenOnBindIntentIndividualIntentSubscriptionsAndSubscribeWith_thenCompositeError() {

        //Given
        val firstValue = 1
        val lastValue = 85
        val view = object : TestView {
            override fun testIntentOne(): Observable<Unit> = Observable.just(Unit)
            override fun render(state: Int) {}
        }
        val mviPresenter = object : AbstractMviPresenter<Int, TestView>() {
            override fun bindIntents(): Observable<Int> {
                val intentStream = intent().create(TestView::testIntentOne)
                    .map { lastValue }
                    .startWithItem(firstValue)
                intentStream.subscribe()
                return intentStream
            }
        }

        //When
        mviPresenter.attachView(view)

        //Then
        verify(mGlobalErrorHandler).accept(mThrowableCaptor.capture())
        val exception = mThrowableCaptor.firstValue
        exception.shouldBeInstanceOf<CompositeException>()
        val compositeException = exception as CompositeException
        compositeException.exceptions
        compositeException.message!!.trim().shouldBeEqualTo("2 exceptions occurred.")
        val firstCause = compositeException.exceptions.first()
        firstCause.shouldNotBeNull()
        firstCause.shouldBeInstanceOf<IllegalStateException>()
        firstCause.message.shouldBeEqualTo("Only a single observer allowed.")
        val secondCause = compositeException.exceptions.last()
        secondCause.shouldNotBeNull()
        secondCause.shouldBeInstanceOf<IllegalStateException>()
        secondCause.message.shouldBeEqualTo("Error must not reach view but rather handled into a proper error state.")
    }

    @Test
    internal fun detachView_whenViewPermanentlyDetached_thenUnbindIntents() {

        //Given
        val isViewPermanentlyDetached = true
        var unbindIntentsCalled = false
        val view = object : TestView {
            override fun testIntentOne(): Observable<Unit> = Observable.just(Unit)
            override fun render(state: Int) {}
        }
        val mviPresenter = object : AbstractMviPresenter<Int, TestView>() {
            override fun bindIntents(): Observable<Int> {
                return Observable.empty()
            }

            override fun unbindIntents() {
                unbindIntentsCalled = true
            }
        }
        mviPresenter.attachView(view)

        //When
        mviPresenter.detachView(isViewPermanentlyDetached)

        //Then
        unbindIntentsCalled.shouldBeTrue()
    }

    @Test
    internal fun detachView_whenViewNotPermanentlyDetached_thenDontUnbindIntents() {

        //Given
        val isViewPermanentlyDetached = false
        var unbindIntentsCalled = false
        val view = object : TestView {
            override fun testIntentOne(): Observable<Unit> = Observable.just(Unit)
            override fun render(state: Int) {}
        }
        val mviPresenter = object : AbstractMviPresenter<Int, TestView>() {
            override fun bindIntents(): Observable<Int> {
                return Observable.empty()
            }

            override fun unbindIntents() {
                unbindIntentsCalled = true
            }
        }
        mviPresenter.attachView(view)

        //When
        mviPresenter.detachView(isViewPermanentlyDetached)

        //Then
        unbindIntentsCalled.shouldBeFalse()
    }

    @Test
    internal fun destroy_thenDontUnbindIntents() {

        //Given
        val isViewPermanentlyDetached = true
        var unbindIntentsCalled = false
        val view = object : TestView {
            override fun testIntentOne(): Observable<Unit> = Observable.just(Unit)
            override fun render(state: Int) {}
        }
        val mviPresenter = object : AbstractMviPresenter<Int, TestView>() {
            override fun bindIntents(): Observable<Int> {
                return Observable.empty()
            }

            override fun unbindIntents() {
                unbindIntentsCalled = true
            }
        }
        mviPresenter.attachView(view)
        mviPresenter.detachView(isViewPermanentlyDetached)

        //When
        mviPresenter.destroy()

        //Then
        unbindIntentsCalled.shouldBeTrue()
    }
}
