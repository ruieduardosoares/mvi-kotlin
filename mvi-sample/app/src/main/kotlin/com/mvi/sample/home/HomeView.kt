package com.mvi.sample.home

import com.mvi.sample.databinding.ActivityHomeBinding
import io.github.ruieduardosoares.android.mvi.kotlin.MviView

class HomeView : MviView<HomeViewState> {

    var binding: ActivityHomeBinding? = null

    private val mBinding: ActivityHomeBinding
        get() = binding!!

    override fun render(state: HomeViewState) {
    }
}
