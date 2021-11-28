package com.mvi.sample.home.fragment

sealed class HomeViewState {

    data class DisplayInputText(val text: String) : HomeViewState()

    object ClearText : HomeViewState()
}
