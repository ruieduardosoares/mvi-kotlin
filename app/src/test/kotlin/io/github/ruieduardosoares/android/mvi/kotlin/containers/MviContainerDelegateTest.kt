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

package io.github.ruieduardosoares.android.mvi.kotlin.containers

import android.os.Bundle
import io.github.ruieduardosoares.android.mvi.kotlin.MviView
import io.github.ruieduardosoares.android.mvi.kotlin.presenter.AbstractMviPresenter
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.clearInvocations
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.only
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.verifyZeroInteractions
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
internal class MviContainerDelegateTest {

    @Mock
    lateinit var mMviContainer: MviContainer<Any, MviView<Any>>

    @Mock
    lateinit var mMviView: MviView<Any>

    @Mock
    lateinit var mPresenter: AbstractMviPresenter<Any, MviView<Any>>

    @Mock
    lateinit var mLazyMviPresenterMemento: Lazy<MviContainerPresenterMemento>

    @Mock
    lateinit var mMviPresenterMemento: MviContainerPresenterMemento

    lateinit var mDelegate: MviContainerDelegate<Any, MviView<Any>>


    @BeforeEach
    internal fun setUp() {
        mDelegate = MviContainerDelegate(mMviContainer, mLazyMviPresenterMemento)
    }

    @Test
    internal fun onCreate_whenWithoutSavedInstance_thenActivityContainerGetViewAndCreatePresenter() {

        //Given
        whenever(mMviContainer.createPresenter()).thenReturn(mPresenter)

        //When
        mDelegate.onCreate(null)

        //Then
        verify(mMviContainer).createPresenter()
        verify(mMviContainer).getView()
        verifyNoMoreInteractions(mMviContainer)
        verifyZeroInteractions(mLazyMviPresenterMemento, mMviPresenterMemento)
    }

    @Test
    internal fun onCreate_whenWithSavedInstanceAndMementoWithPresenterInstance_thenActivityContainerGetViewAndRecoverPresenterFromMemento() {

        //Given
        val savedInstance = mock<Bundle>()
        whenever(mLazyMviPresenterMemento.value).thenReturn(mMviPresenterMemento)
        whenever(mMviPresenterMemento.recover()).thenReturn(mPresenter)

        //When
        mDelegate.onCreate(savedInstance)

        //Then
        verify(mMviContainer, only()).getView()
        verify(mLazyMviPresenterMemento, only()).value
        verify(mMviPresenterMemento, only()).recover()
    }

    @Test
    internal fun onCreate_whenWithSavedInstanceAndMementoWithoutPresenterInstance_thenActivityContainerGetViewCreatePresenter() {

        //Given
        val savedInstance = mock<Bundle>()
        whenever(mLazyMviPresenterMemento.value).thenReturn(mMviPresenterMemento)
        whenever(mMviPresenterMemento.recover()).thenReturn(null)
        whenever(mMviContainer.createPresenter()).thenReturn(mPresenter)

        //When
        mDelegate.onCreate(savedInstance)

        //Then
        verify(mMviContainer).getView()
        verify(mLazyMviPresenterMemento, only()).value
        verify(mMviContainer).createPresenter()
        verifyNoMoreInteractions(mMviContainer)
    }

    @Test
    internal fun onStart_whenNotCreated_thenDoNothing() {

        //When
        mDelegate.onStart()

        //Then
        verifyZeroInteractions(mPresenter)
    }

    @Test
    internal fun onStart_whenCreated_thenAttachViewToPresenter() {

        //Given
        whenever(mMviContainer.createPresenter()).thenReturn(mPresenter)
        whenever(mMviContainer.getView()).thenReturn(mMviView)
        mDelegate = MviContainerDelegate(mMviContainer, mLazyMviPresenterMemento)
        mDelegate.onCreate(null)

        //When
        mDelegate.onStart()

        //Then
        verify(mPresenter, only()).attachView(eq(mMviView))
    }

    @Test
    internal fun onStop_whenNotCreated_thenDoNothing() {

        //When
        mDelegate.onStop()

        //Then
        verifyZeroInteractions(mPresenter)
    }

    @Test
    internal fun onStop_whenCreatedAndStoppingAndSurvivable_thenDetachViewAndKeepPresenterInMemento() {

        //Given
        whenever(mMviContainer.createPresenter()).thenReturn(mPresenter)
        whenever(mMviContainer.getView()).thenReturn(mMviView)
        whenever(mLazyMviPresenterMemento.value).thenReturn(mMviPresenterMemento)
        mDelegate = MviContainerDelegate(mMviContainer, mLazyMviPresenterMemento)
        mDelegate.onCreate(null)
        whenever(mMviContainer.isSurvivable()).thenReturn(true)
        clearInvocations(
            mMviContainer,
            mPresenter,
            mLazyMviPresenterMemento,
            mMviPresenterMemento
        )

        //When
        mDelegate.onStop()

        //Then
        verify(mMviContainer, only()).isSurvivable()
        verify(mPresenter, only()).detachView()
        verify(mLazyMviPresenterMemento, only()).value
        verify(mMviPresenterMemento, only()).keep(eq(mPresenter))
    }

    @Test
    internal fun onStop_whenCreatedAndStoppingAndNotSurvivable_thenDetachViewAndDoNotStorePresenterInMemento() {

        //Given
        whenever(mMviContainer.createPresenter()).thenReturn(mPresenter)
        whenever(mMviContainer.getView()).thenReturn(mMviView)
        mDelegate = MviContainerDelegate(mMviContainer, mLazyMviPresenterMemento)
        mDelegate.onCreate(null)
        whenever(mMviContainer.isSurvivable()).thenReturn(false)
        clearInvocations(
            mMviContainer,
            mPresenter,
            mLazyMviPresenterMemento,
            mMviPresenterMemento
        )

        //When
        mDelegate.onStop()

        //Then
        verify(mMviContainer, only()).isSurvivable()
        verify(mPresenter, only()).detachView()
        verifyZeroInteractions(mLazyMviPresenterMemento, mMviPresenterMemento)
    }

    @Test
    internal fun onDestroy_whenSurvivable_thenDoNothing() {

        //Given
        whenever(mMviContainer.createPresenter()).thenReturn(mPresenter)
        whenever(mMviContainer.getView()).thenReturn(mMviView)
        mDelegate = MviContainerDelegate(mMviContainer, mLazyMviPresenterMemento)
        mDelegate.onCreate(null)
        whenever(mMviContainer.isSurvivable()).thenReturn(true)
        clearInvocations(
            mMviContainer,
            mPresenter,
            mLazyMviPresenterMemento,
            mMviPresenterMemento
        )

        //When
        mDelegate.onDestroy()

        //Then
        verify(mMviContainer, only()).isSurvivable()
        verifyZeroInteractions(mPresenter, mLazyMviPresenterMemento, mMviPresenterMemento)
    }

    @Test
    internal fun onDestroy_whenNotSurvivable_thenDestroyPresenter() {

        //Given
        whenever(mMviContainer.createPresenter()).thenReturn(mPresenter)
        whenever(mMviContainer.getView()).thenReturn(mMviView)
        mDelegate = MviContainerDelegate(mMviContainer, mLazyMviPresenterMemento)
        mDelegate.onCreate(null)
        whenever(mMviContainer.isSurvivable()).thenReturn(false)
        clearInvocations(
            mMviContainer,
            mPresenter,
            mLazyMviPresenterMemento,
            mMviPresenterMemento
        )

        //When
        mDelegate.onDestroy()

        //Then
        verify(mMviContainer, only()).isSurvivable()
        verify(mPresenter, only()).destroy()
        verifyZeroInteractions(mLazyMviPresenterMemento, mMviPresenterMemento)
    }
}
