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

package com.mvi.kotlin.presenter.viewintent

import com.mvi.kotlin.MviView
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.observers.SafeObserver
import org.amshove.kluent.shouldBeFalse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.clearInvocations
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.only
import org.mockito.kotlin.spy
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

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
