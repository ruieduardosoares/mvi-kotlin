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

package io.github.ruieduardosoares.android.mvi.kotlin.presenter

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
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
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyZeroInteractions
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
internal class StateStreamToStateRelayBinderTest {

    @Mock
    lateinit var mViewRenderRelaySubject: BehaviorSubject<Any>

    @Mock
    lateinit var mDisposable: Disposable

    @InjectMocks
    lateinit var mBinder: StateStreamToStateRelayBinder<Any>

    @Test
    internal fun bind_thenSubscribe() {

        //Given
        val observable = mock<Observable<Any>>()

        //When
        mBinder.bind(observable)

        //Then
        verify(observable, only()).subscribe(any(), any(), any())
    }

    @Test
    internal fun bind_whenAlreadyBound_thenSubscribe() {

        //Given
        whenever(mDisposable.isDisposed).thenReturn(false)
        val observable = mock<Observable<Any>>()
        whenever(observable.subscribe(any(), any(), any())).thenReturn(mDisposable)
        mBinder.bind(observable)
        clearInvocations(observable, mViewRenderRelaySubject)

        //When
        mBinder.bind(observable)

        //Then
        verify(mDisposable, only()).isDisposed
        verifyZeroInteractions(observable, mViewRenderRelaySubject)
    }

    @Test
    internal fun bind_whenValueEmitted_thenRelayOnNextCalled() {

        //Given
        val publishSubject = PublishSubject.create<Any>()
        mBinder.bind(publishSubject.hide())

        //When
        publishSubject.onNext(Unit)

        //Then
        verify(mViewRenderRelaySubject, only()).onNext(eq(Unit))
    }

    @Test
    internal fun bind_whenErrorEmitted_thenRelayOnErrorCalled() {

        //Given
        val publishSubject = PublishSubject.create<Any>()
        mBinder.bind(publishSubject.hide())
        val throwable = Throwable()

        //When
        publishSubject.onError(throwable)

        //Then
        verify(mViewRenderRelaySubject, only()).onError(eq(throwable))
    }

    @Test
    internal fun bind_whenCompleteEmitted_thenRelayOnCompleteCalled() {

        //Given
        val publishSubject = PublishSubject.create<Any>()
        mBinder.bind(publishSubject.hide())

        //When
        publishSubject.onComplete()

        //Then
        verify(mViewRenderRelaySubject, only()).onComplete()
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
        val observable = mock<Observable<Any>>()
        whenever(observable.subscribe(any(), any(), any())).thenReturn(mDisposable)
        mBinder.bind(observable)

        //When
        mBinder.dispose()

        //Then
        verify(mDisposable, only()).dispose()
    }

    @Test
    internal fun dispose_whenAlreadyDisposed_thenDoNothing() {

        //Given
        val observable = mock<Observable<Any>>()
        whenever(observable.subscribe(any(), any(), any())).thenReturn(mDisposable)
        mBinder.bind(observable)
        mBinder.dispose()
        clearInvocations(mDisposable, observable, mViewRenderRelaySubject)

        //When
        mBinder.dispose()

        //Then
        verifyZeroInteractions(mDisposable)
    }
}
