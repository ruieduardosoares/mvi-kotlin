package com.mvi.sample

import android.app.Activity
import android.content.Intent
import com.mvi.sample.databinding.ActivityLaunchBinding
import com.mvi.sample.home.HomeFragmentContainerActivity
import io.github.ruieduardosoares.android.mvi.kotlin.MviView
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.PublishSubject

class LaunchView : MviView<LaunchViewState> {

    var binding: ActivityLaunchBinding? = null

    private val mBinding: ActivityLaunchBinding
        get() = binding!!

    private val mAnimateLogoIntentSubject = Single.just(Unit).toObservable()
    private val mAnimateLogoTextIntentSubject = PublishSubject.create<Unit>()

    fun animateLogoIntent(): Observable<Unit> = mAnimateLogoIntentSubject.hide()

    fun animateLogoTextIntent(): Observable<Unit> = mAnimateLogoTextIntentSubject.hide()

    override fun render(state: LaunchViewState) {
        when (state) {
            LaunchViewState.AnimateLogoState -> {
                mBinding.imageView.apply {
                    post {
                        mBinding.textWelcome.alpha = 0f
                        y = mBinding.root.height.toFloat()
                        animate()
                            .setDuration(700)
                            .translationY(0f)
                            .withEndAction {
                                mAnimateLogoTextIntentSubject.onNext(Unit)
                            }
                    }
                }
            }
            LaunchViewState.AnimateLogoTextState -> {
                mBinding.textWelcome.apply {
                    post {
                        animate().setDuration(700).alpha(1f)
                    }
                }
            }
            LaunchViewState.AnimationFinished -> {
                val context = mBinding.root.context
                val activity = context as Activity
                val intent = Intent(context, HomeFragmentContainerActivity::class.java)
                context.startActivity(intent)
                activity.finish()
            }
        }
    }
}
