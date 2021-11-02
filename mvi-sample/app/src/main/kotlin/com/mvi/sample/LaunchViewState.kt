package com.mvi.sample

sealed class LaunchViewState {

    object AnimateLogoState : LaunchViewState()

    object AnimateLogoTextState : LaunchViewState()

    object AnimationFinished : LaunchViewState()
}
