package com.mvi.sample.home.fragment

import android.os.Bundle
import android.view.View
import com.mvi.sample.R
import com.mvi.sample.databinding.FragmentHomeBinding
import io.github.ruieduardosoares.android.mvi.kotlin.containers.fragment.MviContainerFragment
import io.github.ruieduardosoares.android.mvi.kotlin.presenter.AbstractMviPresenter

class HomeFragment : MviContainerFragment<HomeViewState, HomeView>(R.layout.fragment_home) {

    private val mHomeView = HomeViewImpl()

    override fun getMviView(): HomeView = mHomeView

    override fun createPresenter(): AbstractMviPresenter<HomeViewState, HomeView> =
        HomeMviPresenter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mHomeView.binding = FragmentHomeBinding.bind(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mHomeView.binding = null
    }
}
