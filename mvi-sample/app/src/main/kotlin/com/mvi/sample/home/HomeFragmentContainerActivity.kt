package com.mvi.sample.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mvi.sample.databinding.ActivityHomeBinding
import com.mvi.sample.home.fragment.HomeFragment

class HomeFragmentContainerActivity : AppCompatActivity() {

    private var _homeFragment: HomeFragment? = null

    private val mHomeFragment: HomeFragment
        get() = _homeFragment!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonAdd.isEnabled = true
        binding.buttonDetach.isEnabled = false
        binding.buttonAttach.isEnabled = false
        binding.buttonRemove.isEnabled = false
        binding.buttonAdd.setOnClickListener { addFragment(it, binding) }
        binding.buttonDetach.setOnClickListener { detachFragment(it, binding) }
        binding.buttonAttach.setOnClickListener { attachFragment(it, binding) }
        binding.buttonRemove.setOnClickListener { removeFragment(it, binding) }
    }

    private fun addFragment(view: View, binding: ActivityHomeBinding) {
        view.isEnabled = false
        val transaction = supportFragmentManager.beginTransaction()
        _homeFragment = HomeFragment()
        transaction.add(binding.fragmentContainerView.id, mHomeFragment)
        transaction.commit()
        binding.buttonRemove.isEnabled = true
        binding.buttonDetach.isEnabled = true
    }

    private fun detachFragment(view: View, binding: ActivityHomeBinding) {
        view.isEnabled = false
        val transaction = supportFragmentManager.beginTransaction()
        transaction.detach(mHomeFragment)
        transaction.commit()
        binding.buttonRemove.isEnabled = true
        binding.buttonAttach.isEnabled = true
    }

    private fun attachFragment(view: View, binding: ActivityHomeBinding) {
        view.isEnabled = false
        val transaction = supportFragmentManager.beginTransaction()
        transaction.attach(mHomeFragment)
        transaction.commit()
        binding.buttonRemove.isEnabled = true
        binding.buttonDetach.isEnabled = true
    }

    private fun removeFragment(it: View, binding: ActivityHomeBinding) {
        it.isEnabled = false
        val transaction = supportFragmentManager.beginTransaction()
        transaction.remove(mHomeFragment)
        _homeFragment = null
        transaction.commit()
        binding.buttonAdd.isEnabled = true
        binding.buttonDetach.isEnabled = false
        binding.buttonAttach.isEnabled = false
    }

    override fun onBackPressed() {
        // To prevent android to launch LaunchActivityContainer
        moveTaskToBack(false);
    }
}
