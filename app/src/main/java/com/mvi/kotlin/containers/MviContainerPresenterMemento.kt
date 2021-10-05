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

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mvi.kotlin.presenter.AbstractMviPresenter
import java.util.concurrent.atomic.AtomicReference

@MainThread
internal class MviContainerPresenterMemento(viewModelProvider: ViewModelProvider) {

    private val mPresenterStoreModel: PresenterStoreModel =
        viewModelProvider.get(PresenterStoreModel::class.java)

    fun keep(presenter: AbstractMviPresenter<*, *>) =
        mPresenterStoreModel.presenterReference.set(presenter)

    fun recover(): AbstractMviPresenter<*, *>? =
        mPresenterStoreModel.presenterReference.getAndSet(null)

    @MainThread
    internal class PresenterStoreModel : ViewModel() {

        val presenterReference = AtomicReference<AbstractMviPresenter<*, *>>()
    }
}
