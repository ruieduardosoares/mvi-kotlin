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

import androidx.lifecycle.ViewModelProvider
import io.github.ruieduardosoares.android.mvi.kotlin.presenter.AbstractMviPresenter
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.eq
import org.mockito.kotlin.only
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.concurrent.atomic.AtomicReference

@ExtendWith(MockitoExtension::class)
internal class MviPresenterMementoTest {

    @Mock
    lateinit var mViewModelProvider: ViewModelProvider

    @Mock
    lateinit var mPresenter: AbstractMviPresenter<*, *>

    @Mock
    lateinit var mPresenterStoreModel: MviContainerPresenterMemento.PresenterStoreModel

    @Mock
    lateinit var mPresenterReference: AtomicReference<AbstractMviPresenter<*, *>>

    lateinit var mMemento: MviContainerPresenterMemento

    @BeforeEach
    internal fun setUp() {
        whenever(mPresenterStoreModel.presenterReference).thenReturn(mPresenterReference)
        whenever(mViewModelProvider.get(eq(MviContainerPresenterMemento.PresenterStoreModel::class.java)))
            .thenReturn(mPresenterStoreModel)
        mMemento = MviContainerPresenterMemento(mViewModelProvider)
    }

    @Test
    internal fun keep_thenSetPresenterIntoStoreModelReference() {

        //When
        mMemento.keep(mPresenter)

        //Then
        verify(mPresenterStoreModel, only()).presenterReference
        verify(mPresenterReference, only()).set(eq(mPresenter))
    }

    @Test
    internal fun recover_thenGetPresenterAndClearItFromStoreModel() {

        //Given
        whenever(mPresenterReference.getAndSet(eq(null))).thenReturn(mPresenter)

        //When
        val presenter = mMemento.recover()

        //Then
        presenter.shouldNotBeNull()
        verify(mPresenterReference, only()).getAndSet(eq(null))
    }
}
