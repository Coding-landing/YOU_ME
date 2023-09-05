package com.sparta.youandme.view.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.Constraints
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.core.view.marginEnd
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.sparta.youandme.R
import com.sparta.youandme.data.CallObjectData
import com.sparta.youandme.databinding.FragmentContactListBinding
import com.sparta.youandme.extension.ContextExtension.toast
import com.sparta.youandme.view.main.recyclerviewadapter.ContactListAdapter


class ContactListFragment : Fragment() {
    private var _binding: FragmentContactListBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainAdapter: ContactListAdapter
    private val menuClickListener by lazy {
        Toolbar.OnMenuItemClickListener { item ->
            when(item?.itemId) {
                R.id.list_view -> {
                    // TODO 리니어레이아웃매니저로 설정
                }

                R.id.grid_view -> {
                    // TODO 그리드레이아웃매니저로 설정
                }
            }
            false
        }
    }
    private val scrollListener by lazy {
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val view = (requireActivity() as MainActivity).findViewById<TabLayout>(R.id.tab_layout)
                view.isVisible = binding.mainRecyclerView.canScrollVertically(1)
            }
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initRecyclerView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactListBinding.inflate(layoutInflater)
        return binding.root

    }

    private fun initViews() = with(binding) {
        toolBar.run {
            title = getString(R.string.app_name)
            inflateMenu(R.menu.main_menu)
            setOnMenuItemClickListener(menuClickListener)
        }
    }
    private fun initRecyclerView() = with(binding) {
        mainAdapter = ContactListAdapter().apply {
            addItems(CallObjectData.list)
        }
        mainRecyclerView.run {
            adapter = mainAdapter
            layoutManager = LinearLayoutManager(requireActivity())
            addOnScrollListener(scrollListener)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        fun newInstance() = ContactListFragment()
    }
}