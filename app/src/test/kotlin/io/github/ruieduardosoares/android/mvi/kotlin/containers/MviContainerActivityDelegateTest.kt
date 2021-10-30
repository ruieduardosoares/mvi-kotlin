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
internal class MviContainerActivityDelegateTest {

    @Mock
    lateinit var mMviContainerActivity: MviContainerActivity<Any, MviView<Any>>

    @Mock
    lateinit var mMviView: MviView<Any>

    @Mock
    lateinit var mPresenter: AbstractMviPresenter<Any, MviView<Any>>

    @Mock
    lateinit var mLazyMviPresenterMemento: Lazy<MviContainerPresenterMemento>

    @Mock
    lateinit var mMviPresenterMemento: MviContainerPresenterMemento

    lateinit var mDelegate: MviContainerActivityDelegate<Any, MviView<Any>>


    @BeforeEach
    internal fun setUp() {
        mDelegate = MviContainerActivityDelegate(mMviContainerActivity, mLazyMviPresenterMemento)
    }

    @Test
    internal fun onCreate_whenWithoutSavedInstance_thenActivityContainerGetViewAndCreatePresenter() {

        //Given
        whenever(mMviContainerActivity.createPresenter()).thenReturn(mPresenter)

        //When
        mDelegate.onCreate(null)

        //Then
        verify(mMviContainerActivity).createPresenter()
        verify(mMviContainerActivity).getView()
        verifyNoMoreInteractions(mMviContainerActivity)
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
        verify(mMviContainerActivity, only()).getView()
        verify(mLazyMviPresenterMemento, only()).value
        verify(mMviPresenterMemento, only()).recover()
    }

    @Test
    internal fun onCreate_whenWithSavedInstanceAndMementoWithoutPresenterInstance_thenActivityContainerGetViewCreatePresenter() {

        //Given
        val savedInstance = mock<Bundle>()
        whenever(mLazyMviPresenterMemento.value).thenReturn(mMviPresenterMemento)
        whenever(mMviPresenterMemento.recover()).thenReturn(null)
        whenever(mMviContainerActivity.createPresenter()).thenReturn(mPresenter)

        //When
        mDelegate.onCreate(savedInstance)

        //Then
        verify(mMviContainerActivity).getView()
        verify(mLazyMviPresenterMemento, only()).value
        verify(mMviContainerActivity).createPresenter()
        verifyNoMoreInteractions(mMviContainerActivity)
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
        whenever(mMviContainerActivity.createPresenter()).thenReturn(mPresenter)
        whenever(mMviContainerActivity.getView()).thenReturn(mMviView)
        mDelegate = MviContainerActivityDelegate(mMviContainerActivity, mLazyMviPresenterMemento)
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
    internal fun onStop_whenCreatedAndStoppingForConfigurationChanges_thenDetachViewAndKeepPresenterInMemento() {

        //Given
        whenever(mMviContainerActivity.createPresenter()).thenReturn(mPresenter)
        whenever(mMviContainerActivity.getView()).thenReturn(mMviView)
        whenever(mLazyMviPresenterMemento.value).thenReturn(mMviPresenterMemento)
        mDelegate = MviContainerActivityDelegate(mMviContainerActivity, mLazyMviPresenterMemento)
        mDelegate.onCreate(null)
        whenever(mMviContainerActivity.isChangingConfigurations).thenReturn(true)
        clearInvocations(
            mMviContainerActivity,
            mPresenter,
            mLazyMviPresenterMemento,
            mMviPresenterMemento
        )

        //When
        mDelegate.onStop()

        //Then
        verify(mMviContainerActivity, only()).isChangingConfigurations
        verify(mPresenter, only()).detachView()
        verify(mLazyMviPresenterMemento, only()).value
        verify(mMviPresenterMemento, only()).keep(eq(mPresenter))
    }

    @Test
    internal fun onStop_whenCreatedAndStoppingNotForConfigurationChanges_thenDetachViewButDontStorePresenterInMemento() {

        //Given
        whenever(mMviContainerActivity.createPresenter()).thenReturn(mPresenter)
        whenever(mMviContainerActivity.getView()).thenReturn(mMviView)
        mDelegate = MviContainerActivityDelegate(mMviContainerActivity, mLazyMviPresenterMemento)
        mDelegate.onCreate(null)
        whenever(mMviContainerActivity.isChangingConfigurations).thenReturn(false)
        clearInvocations(
            mMviContainerActivity,
            mPresenter,
            mLazyMviPresenterMemento,
            mMviPresenterMemento
        )

        //When
        mDelegate.onStop()

        //Then
        verify(mMviContainerActivity, only()).isChangingConfigurations
        verify(mPresenter, only()).detachView()
        verifyZeroInteractions(mLazyMviPresenterMemento, mMviPresenterMemento)
    }

    @Test
    internal fun onDestroy_whenForConfigurationChanges_thenDoNothing() {

        //Given
        whenever(mMviContainerActivity.createPresenter()).thenReturn(mPresenter)
        whenever(mMviContainerActivity.getView()).thenReturn(mMviView)
        mDelegate = MviContainerActivityDelegate(mMviContainerActivity, mLazyMviPresenterMemento)
        mDelegate.onCreate(null)
        whenever(mMviContainerActivity.isChangingConfigurations).thenReturn(true)
        clearInvocations(
            mMviContainerActivity,
            mPresenter,
            mLazyMviPresenterMemento,
            mMviPresenterMemento
        )

        //When
        mDelegate.onDestroy()

        //Then
        verify(mMviContainerActivity, only()).isChangingConfigurations
        verifyZeroInteractions(mPresenter, mLazyMviPresenterMemento, mMviPresenterMemento)
    }

    @Test
    internal fun onDestroy_whenNotForConfigurationChanges_thenDestroyPresenter() {

        //Given
        whenever(mMviContainerActivity.createPresenter()).thenReturn(mPresenter)
        whenever(mMviContainerActivity.getView()).thenReturn(mMviView)
        mDelegate = MviContainerActivityDelegate(mMviContainerActivity, mLazyMviPresenterMemento)
        mDelegate.onCreate(null)
        whenever(mMviContainerActivity.isChangingConfigurations).thenReturn(false)
        clearInvocations(
            mMviContainerActivity,
            mPresenter,
            mLazyMviPresenterMemento,
            mMviPresenterMemento
        )

        //When
        mDelegate.onDestroy()

        //Then
        verify(mMviContainerActivity, only()).isChangingConfigurations
        verify(mPresenter, only()).destroy()
        verifyZeroInteractions(mLazyMviPresenterMemento, mMviPresenterMemento)
    }
}
