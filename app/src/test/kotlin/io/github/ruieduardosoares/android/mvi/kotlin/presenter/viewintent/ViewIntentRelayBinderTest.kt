package io.github.ruieduardosoares.android.mvi.kotlin.presenter.viewintent

import io.github.ruieduardosoares.android.mvi.kotlin.MviView
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.observers.SafeObserver
import org.amshove.kluent.shouldBeFalse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*

@ExtendWith(MockitoExtension::class)
internal class ViewIntentRelayBinderTest {

    @Mock
    lateinit var mViewIntentBinder: ViewIntentBinder<MviView<Any>, Any>

    @Mock
    lateinit var mIntentRelay: IntentRelay<Any>

    @Mock
    lateinit var mView: MviView<Any>

    @InjectMocks
    lateinit var mBinder: ViewIntentRelayBinder<MviView<Any>, Any>

    @Test
    internal fun bind_thenViewIntentBoundToIntentRelay() {

        //Given
        val observable = spy(Observable.never<Any>())
        whenever(mViewIntentBinder.bind(eq(mView))).thenReturn(observable)

        //When
        val disposable = mBinder.bind(mView)

        //Then
        disposable.isDisposed.shouldBeFalse()
        verify(mViewIntentBinder, only()).bind(eq(mView))
        verify(observable).subscribeWith(any<SafeObserver<Any>>())
        verify(mIntentRelay).onSubscribe(any<SafeObserver<Any>>())
    }

    @Test
    internal fun bind_whenBindAgain_thenViewIntentBoundToIntentRelayAndRelayCopied() {

        //Given
        val intentRelayCopy = mock<IntentRelay<Any>>()
        val observable = spy(Observable.never<Any>())
        whenever(mViewIntentBinder.bind(eq(mView))).thenReturn(observable)
        whenever(mIntentRelay.copy()).thenReturn(intentRelayCopy)
        mBinder.bind(mView)
        clearInvocations(mViewIntentBinder, mIntentRelay, observable)

        //When
        val disposable = mBinder.bind(mView)

        //Then
        disposable.isDisposed.shouldBeFalse()
        verify(mViewIntentBinder, only()).bind(eq(mView))
        verify(mIntentRelay, only()).copy()
        verify(observable).subscribeWith(any<SafeObserver<Any>>())
        verify(intentRelayCopy, only()).onSubscribe(any<SafeObserver<Any>>())
    }
}
