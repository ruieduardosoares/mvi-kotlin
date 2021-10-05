package com.mvi.kotlin

import androidx.annotation.MainThread

@MainThread
interface MviView<T> {

    fun render(state: T)
}
