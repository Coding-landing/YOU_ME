package com.sparta.youandme.view.main.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sparta.youandme.R
import com.sparta.youandme.model.MainTabs
import com.sparta.youandme.view.addcontact.AddContactDialogFragment
import com.sparta.youandme.view.call.CallFragment
import com.sparta.youandme.view.main.ContactListFragment
import com.sparta.youandme.view.mypage.MyPageFragment

class MainViewPagerAdapter(
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {
    private val fragments = ArrayList<MainTabs>()

    init {
        fragments.add(
            MainTabs(ContactListFragment.newInstance(), R.string.contact_list)
        )
        fragments.add(
            MainTabs(MyPageFragment.newInstance(), R.string.my_page),
        )
        fragments.add(
            MainTabs(CallFragment.newInstance(), R.string.calling)
        )
    }

    fun getFragment(position: Int): Fragment {
        return fragments[position].fragment
    }

    fun getTitle(position: Int): Int {
        return fragments[position].titleRes
    }
    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position].fragment
    }


}