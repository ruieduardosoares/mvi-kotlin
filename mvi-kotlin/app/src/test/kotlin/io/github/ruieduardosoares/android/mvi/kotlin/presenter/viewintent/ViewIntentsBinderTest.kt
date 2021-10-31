/**
 * MIT License
 *
 * Copyright (c) [2021] [Rui Eduardo Soares]
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.ruieduardosoares.android.mvi.kotlin.presenter.viewintent

import io.github.ruieduardosoares.android.mvi.kotlin.MviView
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
