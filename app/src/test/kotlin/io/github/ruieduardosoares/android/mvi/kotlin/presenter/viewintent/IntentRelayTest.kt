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

import io.reactivex.rxjava3.subjects.Subject
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldNotBeEqualTo
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.eq
import org.mockito.kotlin.only
import org.mockito.kotlin.verify

@ExtendWith(MockitoExtension::class)
internal class IntentRelayTest {

    @Mock
    lateinit var mRelaySubject: Subject<Any>

    @InjectMocks
    lateinit var mRelay: IntentRelay<Any>

    @Test
    internal fun onNext_thenCallRelaySubjectOnNext() {

        //When
        mRelay.onNext(Unit)

        //Then
        verify(mRelaySubject, only()).onNext(eq(Unit))
    }

    @Test
    internal fun onError_thenThrowViewMustNotEmitError() {

        //When
        val exception = assertThrows<IllegalStateException>("Should throw an Exception") {
            mRelay.onError(Throwable())
        }

        //Then
        val message = exception.message
        message.shouldNotBeNull()
        message.shouldBeEqualTo("View must not emit errors")
    }

    @Test
    internal fun onComplete_thenCallRelaySubjectOnComplete() {

        //When
        mRelay.onComplete()

        //Then
        verify(mRelaySubject, only()).onComplete()
    }

    @Test
    internal fun copy_thenReturnNewIntentRelayInstanceWithSameRelaySubject() {

        //When
        val relayCopy = mRelay.copy()

        //Then
        relayCopy.shouldNotBeEqualTo(mRelay)
    }

    @Test
    internal fun onComplete_whenCopiedIntentRelay_thenCallSameRelaySubjectOnComplete() {

        //Given
        val relayCopy = mRelay.copy()

        //When
        relayCopy.onComplete()

        //Then
        verify(mRelaySubject, only()).onComplete()
    }
}
