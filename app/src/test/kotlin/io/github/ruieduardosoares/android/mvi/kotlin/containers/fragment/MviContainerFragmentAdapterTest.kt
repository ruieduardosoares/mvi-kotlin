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

package io.github.ruieduardosoares.android.mvi.kotlin.containers.fragment

import androidx.lifecycle.ViewModelStore
import io.github.ruieduardosoares.android.mvi.kotlin.MviView
import io.github.ruieduardosoares.android.mvi.kotlin.presenter.AbstractMviPresenter
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.only
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
internal class MviContainerFragmentAdapterTest {

    @Mock
    lateinit var mMviFragment: MviContainerFragment<Any, MviView<Any>>

    @Mock
    lateinit var mMviView: MviView<Any>

    @Mock
    lateinit var mViewModelStore: ViewModelStore

    @Mock
    lateinit var mMviPresenter: AbstractMviPresenter<Any, MviView<Any>>

    @InjectMocks
    lateinit var mMviFragmentAdapter: MviContainerFragmentAdapter<Any, MviView<Any>>

    @Test
    internal fun getView_thenReturnMviFragmentMviView() {

        //Given
        whenever(mMviFragment.getMviView()).thenReturn(mMviView)

        //When
        val mviView = mMviFragmentAdapter.getView()

        //Then
        mviView.shouldBeEqualTo(mMviView)
        verify(mMviFragment, only()).getMviView()
    }

    @Test
    internal fun createPresenter_thenReturnMviFragmentAbstractMviPresenter() {

        //Given
        whenever(mMviFragment.createPresenter()).thenReturn(mMviPresenter)

        //When
        val mviPresenter = mMviFragmentAdapter.createPresenter()

        //Then
        mviPresenter.shouldBeEqualTo(mMviPresenter)
        verify(mMviFragment, only()).createPresenter()
    }

    @Test
    internal fun isSurvivable_whenMviFragmentIsRemoving_thenReturnNotSurvivable() {

        //Given
        whenever(mMviFragment.isRemoving).thenReturn(true)

        //When
        val isSurvivable = mMviFragmentAdapter.isSurvivable()

        //Then
        isSurvivable.shouldBeFalse()
        verify(mMviFragment, only()).isRemoving
    }

    @Test
    internal fun isSurvivable_whenMviFragmentIsNotRemoving_thenReturnSurvivable() {

        //Given
        whenever(mMviFragment.isRemoving).thenReturn(false)

        //When
        val isSurvivable = mMviFragmentAdapter.isSurvivable()

        //Then
        isSurvivable.shouldBeTrue()
        verify(mMviFragment, only()).isRemoving
    }

    @Test
    internal fun getViewModelStore_thenReturnMviFragmentContainerViewModelStore() {

        //Given
        whenever(mMviFragment.viewModelStore).thenReturn(mViewModelStore)

        //When
        val viewModelStore = mMviFragmentAdapter.viewModelStore

        //Then
        viewModelStore.shouldBeEqualTo(mViewModelStore)
        verify(mMviFragment, only()).viewModelStore
    }
}
