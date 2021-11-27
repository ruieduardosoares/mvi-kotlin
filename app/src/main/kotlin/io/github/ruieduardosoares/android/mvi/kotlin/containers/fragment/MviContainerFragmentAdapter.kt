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

import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelStore
import io.github.ruieduardosoares.android.mvi.kotlin.MviView
import io.github.ruieduardosoares.android.mvi.kotlin.containers.MviContainer
import io.github.ruieduardosoares.android.mvi.kotlin.presenter.AbstractMviPresenter

/**
 * Mvi container fragment adapter is necessary and used to prevent conflict between
 * [Fragment.getView] and [MviContainer.getView] since these two enter in conflict when
 * overridden in the fragment container
 *
 * @param S the type of the view state that will be used
 * @param V the type of the MviView that will be used
 * @property mMviFragment fragment to adapt to [MviContainer] contract
 */
@MainThread
internal class MviContainerFragmentAdapter<S : Any, V : MviView<S>>(
    private val mMviFragment: MviContainerFragment<S, V>
) : MviContainer<S, V> {

    override fun getView(): V = mMviFragment.getMviView()

    override fun createPresenter(): AbstractMviPresenter<S, V> = mMviFragment.createPresenter()

    /**
     * Is survivable informs if this fragment container has any characteristic state that makes
     * it survivable through its lifecycle. For that we use the [Fragment.isRemoving] state,
     * so the container is survivable if not being removed - "!isRemoving"
     *
     * Note:
     * A detached fragment is likely to be on backstack, so although a detached fragment is still
     * survivable, only its android view is destroyed, the container has not been destroyed.
     *
     * @return whether the mvi container is survivable or not
     */
    override fun isSurvivable(): Boolean = mMviFragment.isRemoving.not()

    override fun getViewModelStore(): ViewModelStore = mMviFragment.viewModelStore
}
