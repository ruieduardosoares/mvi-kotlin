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
