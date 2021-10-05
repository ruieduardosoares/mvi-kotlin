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

package com.mvi.kotlin.containers

import android.os.Bundle
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModelProvider
import com.mvi.kotlin.MviView
import com.mvi.kotlin.presenter.AbstractMviPresenter

@MainThread
internal class MviContainerActivityDelegate<S : Any, V : MviView<S>>(
    private val mMviContainerActivity: MviContainerActivity<S, V>,
    private val mLazyMviPresenterMemento: Lazy<MviContainerPresenterMemento> =
        lazyOf(MviContainerPresenterMemento(ViewModelProvider(mMviContainerActivity)))
) {

    private val mMviView: V = mMviContainerActivity.getView()

    private var mMviPresenter: AbstractMviPresenter<S, V>? = null

    fun onCreate(savedInstanceState: Bundle?) {
        mMviPresenter = loadPresenter(savedInstanceState)
    }

    @Suppress("UNCHECKED_CAST")
    private fun loadPresenter(savedInstanceState: Bundle?): AbstractMviPresenter<S, V> =
        if (savedInstanceState == null) {
            mMviContainerActivity.createPresenter()
        } else {
            val mviPresenter = mLazyMviPresenterMemento.value.recover()
            mviPresenter?.run { this as AbstractMviPresenter<S, V> }
                ?: mMviContainerActivity.createPresenter()
        }

    fun onStart() {
        mMviPresenter?.attachView(mMviView)
    }

    fun onStop() {
        mMviPresenter?.let { presenter ->
            val isChangingConfigurations = mMviContainerActivity.isChangingConfigurations
            presenter.detachView(isChangingConfigurations.not())
            if (isChangingConfigurations) {
                mLazyMviPresenterMemento.value.keep(presenter)
            }
        }
    }

    fun onDestroy() {
        if (mMviContainerActivity.isChangingConfigurations.not()) {
            mMviPresenter?.destroy()
        }
        mMviPresenter = null
    }
}
