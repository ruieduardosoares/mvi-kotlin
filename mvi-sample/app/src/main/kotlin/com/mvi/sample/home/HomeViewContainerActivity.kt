package com.mvi.sample.home

import android.os.Bundle
import com.mvi.sample.databinding.ActivityHomeBinding
import io.github.ruieduardosoares.android.mvi.kotlin.containers.MviContainerActivity

class HomeViewContainerActivity : MviContainerActivity<HomeViewState, HomeView>() {

    private val mHomeView = HomeView()

    private val mLazyPresenter by lazy(LazyThreadSafetyMode.NONE) { HomeMviPresenter() }

    private lateinit var mBinding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mHomeView.binding = mBinding
    }

    override fun getView(): HomeView = mHomeView

    override fun createPresenter(): HomeMviPresenter = mLazyPresenter

    override fun onBackPressed() {
        // To prevent android to launch LaunchActivityContainer
        moveTaskToBack(false);
    }
}
