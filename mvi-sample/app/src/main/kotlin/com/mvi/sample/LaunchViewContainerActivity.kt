package com.mvi.sample

import android.os.Bundle
import com.mvi.sample.databinding.ActivityLaunchBinding
import io.github.ruieduardosoares.android.mvi.kotlin.containers.MviContainerActivity

class LaunchViewContainerActivity : MviContainerActivity<LaunchViewState, LaunchView>() {

    private val mLaunchView = LaunchView()

    private val mLazyPresenter by lazy(LazyThreadSafetyMode.NONE) { LaunchMviPresenter() }

    private lateinit var mBinding: ActivityLaunchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLaunchBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mLaunchView.binding = mBinding
    }

    override fun getView(): LaunchView = mLaunchView

    override fun createPresenter(): LaunchMviPresenter = mLazyPresenter
}
