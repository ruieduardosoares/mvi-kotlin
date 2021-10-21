package io.github.ruieduardosoares.android.mvi.kotlin

import androidx.annotation.MainThread

@MainThread
interface MviView<T> {

    fun render(state: T)
}
