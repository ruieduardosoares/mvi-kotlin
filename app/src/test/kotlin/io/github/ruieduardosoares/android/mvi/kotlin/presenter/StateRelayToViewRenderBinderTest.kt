package io.github.ruieduardosoares.android.mvi.kotlin.presenter

import io.github.ruieduardosoares.android.mvi.kotlin.MviView
import io.reactivex.rxjava3.observers.SafeObserver
import io.reactivex.rxjava3.subjects.BehaviorSubject
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.clearInvocations
import org.mockito.kotlin.eq
import org.mockito.kotlin.only
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyZeroInteractions
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
internal class StateRelayToViewRenderBinderTest {

    @Mock
    lateinit var mStateRelaySubject: BehaviorSubject<Any>

    @Mock
    lateinit var mView: MviView<Any>

    @Mock
    lateinit var mDisposable: SafeObserver<Any>

    lateinit var mBinder: StateRelayToViewRenderBinder<Any>

    @BeforeEach
    internal fun setUp() {
        mBinder = StateRelayToViewRenderBinder(mStateRelaySubject)
    }

    @Test
    internal fun bind_thenViewBoundToStateRelaySubject() {

        //Given
        whenever(mStateRelaySubject.subscribeWith(any<SafeObserver<Any>>()))
            .thenReturn(mDisposable)

        //When
        mBinder.bind(mView)

        //Then
        verify(mStateRelaySubject, only()).subscribeWith(any<SafeObserver<Any>>())
    }

    @Test
    internal fun bind_whenAlreadyBound_thenNothing() {

        //Given
        whenever(mStateRelaySubject.subscribeWith(any<SafeObserver<Any>>()))
            .thenReturn(mDisposable)
        whenever(mDisposable.isDisposed).thenReturn(false)
        mBinder.bind(mView)
        clearInvocations(mView)

        //When
        mBinder.bind(mView)

        //Then
        verifyZeroInteractions(mStateRelaySubject)
    }

    @Test
    internal fun getRelay_thenGetStateRelaySubject() {

        //When
        val relay = mBinder.getRelay()

        //Then
        relay.shouldBeEqualTo(mStateRelaySubject)
    }

    @Test
    internal fun dispose_whenNotBound_thenDoNothing() {

        //When
        mBinder.dispose()

        //Then
        verifyZeroInteractions(mDisposable)
    }

    @Test
    internal fun dispose_whenBound_thenDispose() {

        //Given
        whenever(mStateRelaySubject.subscribeWith(any<SafeObserver<Any>>()))
            .thenReturn(mDisposable)
        mBinder.bind(mView)

        //When
        mBinder.dispose()

        //Then
        verify(mDisposable, only()).dispose()
    }

    @Test
    internal fun dispose_whenAlreadyDisposed_thenDoNothing() {

        //Given
        whenever(mStateRelaySubject.subscribeWith(any<SafeObserver<Any>>()))
            .thenReturn(mDisposable)
        mBinder.bind(mView)
        mBinder.dispose()
        clearInvocations(mDisposable, mStateRelaySubject, mView)

        //When
        mBinder.dispose()

        //Then
        verifyZeroInteractions(mDisposable)
    }

    @Test
    internal fun viewRenderObserverOnNext_thenRender() {

        //Given
        val viewRenderObserver = StateRelayToViewRenderBinder.ViewRenderObserver(mView)

        //When
        viewRenderObserver.onNext(Unit)

        //Then
        verify(mView).render(eq(Unit))
    }

    @Test
    internal fun viewRenderObserverOnError_thenThrowError() {

        //Given
        val viewRenderObserver = StateRelayToViewRenderBinder.ViewRenderObserver(mView)
        val throwable = Throwable()

        //When
        val exception = assertThrows<IllegalStateException>("Should thrown IllegalStateException") {
            viewRenderObserver.onError(throwable)
        }

        //Then
        exception.shouldBeInstanceOf<IllegalStateException>()
        val message = exception.message
        message.shouldNotBeNull()
        message.shouldBeEqualTo("Error must not reach view but rather handled into a proper error state.")
    }

    @Test
    internal fun viewRenderObserverOnComplete_thenDoNothing() {

        //Given
        val viewRenderObserver = StateRelayToViewRenderBinder.ViewRenderObserver(mView)

        //When
        viewRenderObserver.onComplete()

        //Then
        verifyZeroInteractions(mView)
    }
}
