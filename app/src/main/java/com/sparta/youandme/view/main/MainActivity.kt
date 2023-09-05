package com.sparta.youandme.view.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.sparta.youandme.R
import com.sparta.youandme.databinding.ActivityMainBinding
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
        val iconList = intArrayOf(R.drawable.icon_home, R.drawable.icon_book, R.drawable.icon_call)
        viewPager.isVisible = true
        viewPager.run {
            adapter = viewPagerAdapter
            registerOnPageChangeCallback(callback)
        }
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
//            tab.setText(viewPagerAdapter.getTitle(position))
        }.attach()

        setCustomTabLayoutIcon(iconList)
        initButton()
    }

    private fun setCustomTabLayoutIcon(iconList: IntArray) = with(binding) {
        for (i in iconList.indices) {
            val view = layoutInflater.inflate(R.layout.tab_icon, null)
            val tab = tabLayout.getTabAt(i)
            view.findViewById<ImageView>(R.id.tab_icon_image_view)
                .setBackgroundResource(iconList[i])
            if (tab != null)
                tab.customView = view
        }
    }

    private fun initButton() = with(binding) {
        fabAddTodo.setOnClickListener {
            viewPager.isVisible = false
            addContactFragment.isVisible = true
            supportFragmentManager.beginTransaction().replace(
                R.id.add_contact_fragment, AddContactDialogFragment.newInstance()
            ).commit()
        }
    }
}