package com.mvi.sample.home.fragment

import com.jakewharton.rxbinding4.view.clicks
import com.mvi.sample.databinding.FragmentHomeBinding
import io.reactivex.rxjava3.core.Observable

class HomeViewImpl : HomeView {

    var binding: FragmentHomeBinding? = null

    private val mBinding: FragmentHomeBinding
        get() = binding!!

    override fun displayInputTextIntent(): Observable<String> =
        mBinding.displayTextButton.clicks().map { mBinding.inputEditTextView.text.toString() }

    override fun clearTextIntent(): Observable<Unit> = mBinding.clearTextButton.clicks()

    override fun render(state: HomeViewState) {
        when (state) {
            is HomeViewState.DisplayInputText -> {
                mBinding.displayTextButton.isEnabled = false
                mBinding.clearTextButton.isEnabled = true
                mBinding.inputTextView.text = state.text
            }
            is HomeViewState.ClearText -> {
                mBinding.inputTextView.text = null
                mBinding.inputEditTextView.text = null
                mBinding.displayTextButton.isEnabled = true
                mBinding.clearTextButton.isEnabled = false
            }
        }
    }
}
