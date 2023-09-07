package com.sparta.youandme.view.main

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.sparta.youandme.R
import com.sparta.youandme.data.CallObjectData
import com.sparta.youandme.data.CallObjectData.gridList
import com.sparta.youandme.databinding.FragmentContactListBinding
import com.sparta.youandme.model.CallingObject
import com.sparta.youandme.model.ViewType
import com.sparta.youandme.view.detail.ContactDetailFragment
import com.sparta.youandme.view.main.recyclerview.adapter.ContactListAdapter
import com.sparta.youandme.view.main.recyclerview.listener.ItemClickListener
import com.sparta.youandme.view.main.recyclerview.utility.SwipeToEditCallback

class ContactListFragment : Fragment() {
    private var _binding: FragmentContactListBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainAdapter: ContactListAdapter
    private lateinit var manager: LayoutManager
    private lateinit var mainActivity: MainActivity
    private var view: TabLayout? = null
    private var fab: FloatingActionButton? = null
    private val menuClickListener by lazy {
        Toolbar.OnMenuItemClickListener { item ->
            when (item?.itemId) {
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
                if (manager is GridLayoutManager) {
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
        mainActivity = (requireActivity() as MainActivity)
        view = mainActivity.findViewById(R.id.tab_layout)
        fab = mainActivity.findViewById(R.id.fab_add_todo)
        return binding.root
    }

    private fun initViews() = with(binding) {
        toolBar.run {
            title = getString(R.string.app_name)
            inflateMenu(R.menu.main_menu)
            setOnMenuItemClickListener(menuClickListener)
        }
    }

    private fun setOnClickListener() {
        mainAdapter.setOnClickListener(object : ItemClickListener {
            override fun onItemClick(position: Int) {
                val list = mainAdapter.list
                val bundle = Bundle().apply {
                    putParcelable("model", list[position])
                }
                mainActivity.changeFragment(
                    R.id.add_contact_fragment,
                    ContactDetailFragment.newInstance().apply {
                        arguments = bundle
                    }
                )
            }
        })
    }

    private fun initRecyclerView() = with(binding) {
        view?.isVisible = true
        fab?.show()
        mainAdapter = ContactListAdapter().apply {
            val items =
                CallObjectData.list.sortedWith(compareByDescending<CallingObject> { it.isLiked }.thenBy { it.name })
                    .onEachIndexed { index, callingObject ->
                        callingObject.type =
                            if (index % 2 == 0) ViewType.LEFT_POSITION else ViewType.RIGHT_POSITION
                    }
            if (manager is GridLayoutManager) {
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
        // 왼쪽 스와이프 콜백
        val callSwipeHandler = object : SwipeToEditCallback(requireActivity()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val activity =
                    mainActivity.findViewById<ViewPager2>(R.id.view_pager)
                val bundle = Bundle().apply {
                    putParcelable("model", mainAdapter.list[viewHolder.adapterPosition])
                }
                activity.setCurrentItem(2, true)
                parentFragmentManager.setFragmentResult("callObject", bundle)
            }
        }
        val callSwipeHelper = ItemTouchHelper(callSwipeHandler)
        callSwipeHelper.attachToRecyclerView(mainRecyclerView)
        setOnClickListener()
    }

    override fun onDestroyView() {
        _binding = null
        view = null
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
        var obj: CallingObject? = null
        parentFragmentManager.setFragmentResultListener(
            "callObject",
            viewLifecycleOwner
        ){ _, bundle->
            obj = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable("model", CallingObject::class.java)
            } else {
                bundle.getParcelable("model")
            }
            if (obj == null) {
                return@setFragmentResultListener
            }
            CallObjectData.addItem(obj!!)
            initRecyclerView()
        }
    }

    companion object {
        fun newInstance() = ContactListFragment()
    }
}