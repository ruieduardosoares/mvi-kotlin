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

package com.mvi.kotlin.presenter

import com.mvi.kotlin.MviView
import com.mvi.kotlin.presenter.viewintent.IntentCreator
import com.mvi.kotlin.presenter.viewintent.ViewIntentsBinder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.clearInvocations
import org.mockito.kotlin.eq
import org.mockito.kotlin.only
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.verifyZeroInteractions

@ExtendWith(MockitoExtension::class)
internal class MviPresenterDelegateTest {

    @Mock
    lateinit var mStateRelayToViewRenderBinder: StateRelayToViewRenderBinder<Any>

    @Mock
    lateinit var mStateStreamToStateRelayBinder: StateStreamToStateRelayBinder<Any>

    @Mock
    lateinit var mViewIntentsBinder: ViewIntentsBinder<TestView>

    @Mock
    lateinit var mIntentCreator: IntentCreator<TestView>

    interface TestView : MviView<Any>

    @Mock
    lateinit var mView: TestView

    @Mock
    lateinit var mAbstractMviPresenter: AbstractMviPresenter<Any, TestView>

    private lateinit var mPresenterDelegate: MviPresenterDelegate<Any, TestView>

    @BeforeEach
    internal fun setUp() {
        mPresenterDelegate = MviPresenterDelegate(
            mStateRelayToViewRenderBinder,
            mStateStreamToStateRelayBinder,
            mViewIntentsBinder,
            mIntentCreator,
        )
    }

    @Test
    internal fun attachView_whenAttach_thenBindPresenterIntentsAndViewToInternalComponents() {

        //When
        mPresenterDelegate.attachView(mView, mAbstractMviPresenter)

        //Then
        verify(mAbstractMviPresenter, only()).bindIntents()
        verify(mStateRelayToViewRenderBinder, only()).bind(eq(mView))
        verify(mViewIntentsBinder, only()).bind(eq(mView))
    }

    @Test
    internal fun attachView_whenAlreadyAttached_thenDoNothing() {

        //Given
        mPresenterDelegate.attachView(mView, mAbstractMviPresenter)
        clearInvocations(mAbstractMviPresenter, mStateRelayToViewRenderBinder, mViewIntentsBinder)

        //When
        mPresenterDelegate.attachView(mView, mAbstractMviPresenter)

        //Then
        verifyZeroInteractions(
            mAbstractMviPresenter,
            mStateRelayToViewRenderBinder,
            mViewIntentsBinder
        )
    }

    @Test
    internal fun attachView_whenDestroyed_thenDoNothing() {

        //Given
        mPresenterDelegate.attachView(mView, mAbstractMviPresenter)
        mPresenterDelegate.detachView(false, mAbstractMviPresenter)
        mPresenterDelegate.destroy(mAbstractMviPresenter)
        clearInvocations(mAbstractMviPresenter, mStateRelayToViewRenderBinder, mViewIntentsBinder)

        //When
        mPresenterDelegate.attachView(mView, mAbstractMviPresenter)

        //Then
        verifyZeroInteractions(
            mAbstractMviPresenter,
            mStateRelayToViewRenderBinder,
            mViewIntentsBinder
        )
    }

    @Test
    internal fun attachView_whenDetached_thenBindViewToInternalComponentsOnly() {

        //Given
        mPresenterDelegate.attachView(mView, mAbstractMviPresenter)
        mPresenterDelegate.detachView(false, mAbstractMviPresenter)
        clearInvocations(mAbstractMviPresenter, mStateRelayToViewRenderBinder, mViewIntentsBinder)

        //When
        mPresenterDelegate.attachView(mView, mAbstractMviPresenter)

        //Then
        verify(mStateRelayToViewRenderBinder, only()).bind(eq(mView))
        verify(mViewIntentsBinder, only()).bind(eq(mView))
        verifyZeroInteractions(mAbstractMviPresenter)
    }

    @Test
    internal fun attachView_whenAttachMultipleTimes_thenBindPresenterIntentsAndViewToComponentsOnlyOnce() {

        //When
        mPresenterDelegate.attachView(mView, mAbstractMviPresenter)
        mPresenterDelegate.attachView(mView, mAbstractMviPresenter)
        mPresenterDelegate.attachView(mView, mAbstractMviPresenter)

        //Then
        verify(mAbstractMviPresenter, only()).bindIntents()
        verify(mStateRelayToViewRenderBinder, only()).bind(eq(mView))
        verify(mViewIntentsBinder, only()).bind(eq(mView))
    }

    @Test
    internal fun attachView_whenFromAttachToDetach_thenBindViewToComponentsOnly() {

        //Given
        mPresenterDelegate.attachView(mView, mAbstractMviPresenter)
        mPresenterDelegate.detachView(false, mAbstractMviPresenter)
        clearInvocations(mAbstractMviPresenter, mStateRelayToViewRenderBinder, mViewIntentsBinder)

        //When
        mPresenterDelegate.attachView(mView, mAbstractMviPresenter)

        //Then
        verifyZeroInteractions(mAbstractMviPresenter)
        verify(mStateRelayToViewRenderBinder, only()).bind(eq(mView))
        verify(mViewIntentsBinder, only()).bind(eq(mView))
    }

    @Test
    internal fun detachView_whenNotAttached_thenDoNothing() {

        //When
        mPresenterDelegate.detachView(false, mAbstractMviPresenter)

        //Then
        verifyZeroInteractions(
            mStateRelayToViewRenderBinder,
            mViewIntentsBinder,
            mAbstractMviPresenter
        )
    }

    @Test
    internal fun detachView_whenDestroyed_thenDoNothing() {

        //Given
        mPresenterDelegate.attachView(mView, mAbstractMviPresenter)
        mPresenterDelegate.detachView(true, mAbstractMviPresenter)
        mPresenterDelegate.destroy(mAbstractMviPresenter)
        clearInvocations(mAbstractMviPresenter, mStateRelayToViewRenderBinder, mViewIntentsBinder)

        //When
        mPresenterDelegate.detachView(false, mAbstractMviPresenter)

        //Then
        verifyZeroInteractions(
            mStateRelayToViewRenderBinder,
            mViewIntentsBinder,
            mAbstractMviPresenter
        )
    }

    @Test
    internal fun detachView_whenViewPermanentlyDetached_thenUnbindPresenterIntentsAndDisposeViewFromComponents() {

        //Given
        mPresenterDelegate.attachView(mView, mAbstractMviPresenter)
        clearInvocations(mAbstractMviPresenter, mStateRelayToViewRenderBinder, mViewIntentsBinder)
        val isViewPermanentlyDetached = true

        //When
        mPresenterDelegate.detachView(isViewPermanentlyDetached, mAbstractMviPresenter)

        //Then
        verify(mStateRelayToViewRenderBinder, only()).dispose()
        verify(mViewIntentsBinder, only()).unbind()
        verify(mAbstractMviPresenter, only()).unbindIntents()
    }

    @Test
    internal fun detachView_whenViewNotPermanentlyDetached_thenDisposeViewFromComponentsOnly() {

        //Given
        mPresenterDelegate.attachView(mView, mAbstractMviPresenter)
        clearInvocations(mAbstractMviPresenter, mStateRelayToViewRenderBinder, mViewIntentsBinder)
        val isViewPermanentlyDetached = false

        //When
        mPresenterDelegate.detachView(isViewPermanentlyDetached, mAbstractMviPresenter)

        //Then
        verify(mStateRelayToViewRenderBinder, only()).dispose()
        verify(mViewIntentsBinder, only()).unbind()
        verifyZeroInteractions(mAbstractMviPresenter)
    }

    @Test
    internal fun destroy_whenAlreadyDestroyed_thenDoNothing() {

        //Given
        mPresenterDelegate.attachView(mView, mAbstractMviPresenter)
        mPresenterDelegate.detachView(true, mAbstractMviPresenter)
        mPresenterDelegate.destroy(mAbstractMviPresenter)
        clearInvocations(mAbstractMviPresenter, mStateRelayToViewRenderBinder, mViewIntentsBinder)

        //When
        mPresenterDelegate.destroy(mAbstractMviPresenter)

        //Then
        verifyZeroInteractions(
            mAbstractMviPresenter,
            mStateRelayToViewRenderBinder,
            mViewIntentsBinder
        )
    }

    @Test
    internal fun destroy_whenAttached_thenDetachFirstWithViewPermanentDetachAndDestroyViewIntents() {

        //Given
        mPresenterDelegate.attachView(mView, mAbstractMviPresenter)
        clearInvocations(
            mAbstractMviPresenter,
            mStateRelayToViewRenderBinder,
            mViewIntentsBinder,
            mStateStreamToStateRelayBinder
        )

        //When
        mPresenterDelegate.destroy(mAbstractMviPresenter)

        //Then
        verify(mStateRelayToViewRenderBinder, only()).dispose()
        verify(mViewIntentsBinder).unbind()
        verify(mAbstractMviPresenter, only()).unbindIntents()
        verify(mStateStreamToStateRelayBinder, only()).dispose()
        verify(mViewIntentsBinder).destroy()
        verifyNoMoreInteractions(mViewIntentsBinder)
    }

    @Test
    internal fun destroy_whenDetached_thenDestroyViewIntents() {

        //Given
        mPresenterDelegate.attachView(mView, mAbstractMviPresenter)
        mPresenterDelegate.detachView(true, mAbstractMviPresenter)
        clearInvocations(
            mAbstractMviPresenter,
            mStateRelayToViewRenderBinder,
            mViewIntentsBinder,
            mStateStreamToStateRelayBinder
        )

        //When
        mPresenterDelegate.destroy(mAbstractMviPresenter)

        //Then
        verify(mStateStreamToStateRelayBinder, only()).dispose()
        verify(mViewIntentsBinder, only()).destroy()
    }
}
