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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.sparta.youandme.R
import com.sparta.youandme.data.CallObjectData
import com.sparta.youandme.data.CallObjectData.gridList
import com.sparta.youandme.databinding.FragmentContactListBinding
import com.sparta.youandme.extension.ContextExtension.toast
import com.sparta.youandme.model.ViewType
import com.sparta.youandme.view.main.recyclerviewadapter.ContactListAdapter


class ContactListFragment : Fragment() {
    private var _binding: FragmentContactListBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainAdapter: ContactListAdapter
    private lateinit var manager: LayoutManager
    private var view: TabLayout? = null
    private val menuClickListener by lazy {
        Toolbar.OnMenuItemClickListener { item ->
            when(item?.itemId) {
                R.id.list_view -> manager = LinearLayoutManager(requireActivity())
                R.id.grid_view -> manager = GridLayoutManager(requireActivity(), 3)
            }
            initRecyclerView()
            false
        }
    }
    private val scrollListener by lazy {
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(manager is GridLayoutManager) {
                    view?.isVisible = true
                    return
                }
                view?.isVisible = binding.mainRecyclerView.canScrollVertically(1)
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
        manager = LinearLayoutManager(requireActivity())
        view = (requireActivity() as MainActivity).findViewById(R.id.tab_layout)
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
        view?.isVisible = true
        mainAdapter = ContactListAdapter().apply {
            val items = CallObjectData.list
            if(manager is GridLayoutManager) {
                addItems(gridList)
                return@apply
            }
            addItems(items)
        }
        mainRecyclerView.run {
            adapter = mainAdapter
            layoutManager = manager
            addOnScrollListener(scrollListener)
        }
    }

    override fun onDestroyView() {
        _binding = null
        view = null
        super.onDestroyView()
    }

    companion object {
        fun newInstance() = ContactListFragment()
    }
}