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

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.ContentView
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import io.github.ruieduardosoares.android.mvi.kotlin.MviView
import io.github.ruieduardosoares.android.mvi.kotlin.containers.MviContainerDelegate
import io.github.ruieduardosoares.android.mvi.kotlin.presenter.AbstractMviPresenter

/**
 * Mvi container fragment that merely acts as a container for the view and the presenter.
 * This fragment container shouldn't be your view implementation but rather a container for your view.
 *
 * Example:
 * ```
 * class SomeViewContainerFragment : MviContainerFragment<SomeViewState, SomeMviView>(R.layout.fragment_layout) {
 *
 *      private val mMviView = SomeMviView()
 *
 *      override fun getMviView(): SomeMviView = mMviView
 *
 *      override fun createPresenter(): AbstractMviPresenter<SomeViewState, SomeMviView> = SomeMviPresenter()
 *
 *      override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
 *          super.onViewCreated(view, savedInstanceState)
 *          mMviView.mBinding = FragmentTestBinding.bind(view) // using view binding
 *      }
 *
 *      override fun onDestroyView() {
 *          super.onDestroyView()
 *          mMviView.mBinding = null //important to clear reference to binding/android-view
 *      }
 * }
 * ```
 * For more implementation details see [MviContainerDelegate]
 *
 * @param S the type of the view state that will be used
 * @param V the type of the MviView that will be used
 */
@MainThread
abstract class MviContainerFragment<S : Any, V : MviView<S>> : Fragment {

    constructor() : super()

    @ContentView
    constructor(contentLayoutId: Int) : super(contentLayoutId)

    private val mDelegate by lazy { MviContainerDelegate(MviContainerFragmentAdapter(this)) }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDelegate.onCreate(savedInstanceState)
    }

    /**
     * You must implement this method and provide a view instance of [V] type.
     * It should return the same mvi view instance as long your fragment is not destroyed.
     *
     * @return view instance of [V] type
     */
    abstract fun getMviView(): V

    /**
     * You must implement this method and provide a new instance of the given presenter.
     * It should return a new instance of an implementation of [AbstractMviPresenter].
     *
     * @return new instance of an implementation of [AbstractMviPresenter]
     */
    abstract fun createPresenter(): AbstractMviPresenter<S, V>

    @CallSuper
    override fun onStart() {
        super.onStart()
        mDelegate.onStart()
    }

    @CallSuper
    override fun onStop() {
        super.onStop()
        mDelegate.onStop()
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        mDelegate.onDestroy()
    }
}
