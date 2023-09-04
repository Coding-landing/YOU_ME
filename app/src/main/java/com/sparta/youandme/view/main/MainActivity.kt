package com.sparta.youandme.view.main

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.sparta.youandme.R
import com.sparta.youandme.databinding.ActivityMainBinding
import com.sparta.youandme.extension.ContextExtension.toast
import com.sparta.youandme.view.addcontact.AddContactDialogFragment
import com.sparta.youandme.view.main.viewpager.MainViewPagerAdapter
import com.sparta.youandme.view.mypage.MyPageFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewPagerAdapter by lazy {
        MainViewPagerAdapter(this@MainActivity)
    }
    private val callback by lazy {
        object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) = with(binding) {
                super.onPageSelected(position)
                addContactFragment.isVisible = false
                viewPager.isVisible = true
                if (viewPagerAdapter.getFragment(position) is MyPageFragment) {
                    fabAddTodo.hide()
                } else {
                    fabAddTodo.show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() = with(binding) {
        toolBar.title = getString(R.string.app_name)
        viewPager.isVisible = true
        viewPager.run {
            adapter = viewPagerAdapter
            registerOnPageChangeCallback(callback)
        }
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.setText(viewPagerAdapter.getTitle(position))
        }.attach()
        initButton()
    }

    private fun initButton() = with(binding) {
        fabAddTodo.setOnClickListener {
            viewPager.isVisible = false
            addContactFragment.isVisible = true
            supportFragmentManager.beginTransaction().replace(
                R.id.add_contact_fragment, AddContactDialogFragment()
            ).commit()
        }
    }
}