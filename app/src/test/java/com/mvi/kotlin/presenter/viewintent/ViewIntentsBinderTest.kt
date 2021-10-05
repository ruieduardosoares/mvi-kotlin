package com.mvi.kotlin.presenter.viewintent

import com.mvi.kotlin.MviView
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.eq
import org.mockito.kotlin.only
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyZeroInteractions
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
internal class ViewIntentsBinderTest {

    @Mock
    lateinit var mDisposable: Disposable

    @Mock
    lateinit var mViewIntentBinder: ViewIntentRelayBinder<MviView<Any>, *>

    @Mock
    lateinit var mIntentBinders: MutableList<ViewIntentRelayBinder<MviView<Any>, *>>

    @Mock
    lateinit var mCompositeDisposable: CompositeDisposable

    @Mock
    lateinit var mView: MviView<Any>

    @Test
    internal fun add_thenViewIntentRelayBinderAdded() {

        //Given
        val binder = ViewIntentsBinder(mIntentBinders, mCompositeDisposable)

        //When
        binder.add(mViewIntentBinder)

        //Then
        verify(mIntentBinders, only()).add(eq(mViewIntentBinder))
    }

    @Test
    internal fun bind_whenNoIntentBindersAdded_thenDoNothing() {

        //Given
        whenever(mIntentBinders.isEmpty()).thenReturn(true)
        val binder = ViewIntentsBinder(mIntentBinders, mCompositeDisposable)

        //When
        binder.bind(mView)

        //Then
        verify(mIntentBinders, only()).isEmpty()
        verifyZeroInteractions(mCompositeDisposable)
    }

    @Test
    internal fun bind_whenIntentBindersAdded_thenUnbindFirstAndBindAllAgain() {

        //Given
        whenever(mViewIntentBinder.bind(eq(mView))).thenReturn(mDisposable)
        val intentBinders = mutableListOf(mViewIntentBinder, mViewIntentBinder)
        val binder = ViewIntentsBinder(intentBinders, mCompositeDisposable)

        //When
        binder.bind(mView)

        //Then
        verify(mCompositeDisposable).clear()
        verify(mViewIntentBinder, times(2)).bind(mView)
        verify(mCompositeDisposable, times(2)).add(mDisposable)
        verifyZeroInteractions(mCompositeDisposable)
    }

    @Test
    internal fun unbind_thenClearInternalDisposable() {

        //Given
        val binder = ViewIntentsBinder(mIntentBinders, mCompositeDisposable)

        //When
        binder.unbind()

        //Then
        verify(mCompositeDisposable, only()).clear()
    }

    @Test
    internal fun destroy_thenClearInternalDisposable() {

        //Given
        val binder = ViewIntentsBinder(mIntentBinders, mCompositeDisposable)

        //When
        binder.destroy()

        //Then
        verify(mCompositeDisposable, only()).dispose()
        verify(mIntentBinders, only()).clear()
    }
}
