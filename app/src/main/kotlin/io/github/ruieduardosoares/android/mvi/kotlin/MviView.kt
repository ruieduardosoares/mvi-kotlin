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

package io.github.ruieduardosoares.android.mvi.kotlin

import androidx.annotation.MainThread

/**
 * This interface represents the base MVI view contract to implement.
 *
 * Example of an implementation would be creating a new specific view interface and then extend
 * from MviView with the respective ViewState class.
 *
 * After that you just need to declare the necessary intent methods that your view will trigger
 *
 *
 * Example:
 * ```
 * interface NewScreenView: MviView<NewScreenState>{
 *
 *     fun someIntent(): Observable<Unit>
 *
 *     fun anotherIntent(): Observable<Unit>
 * }
 * ```
 *
 * As a final step you would implement this interface in a class that has access to the
 * Activity view for example.
 *
 * Remember that the Activity/Fragment are not your view, but rather a container for your view
 *
 * @param T is the ViewState class
 */
@MainThread
interface MviView<T> {

    /**
     * Responsible for rendering the final state after it has been generated by pipeline
     *
     * @param state
     */
    fun render(state: T)
}
