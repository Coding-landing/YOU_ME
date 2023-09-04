package com.sparta.youandme.view.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.get
import com.sparta.youandme.R
import com.sparta.youandme.databinding.FragmentContactListBinding
import com.sparta.youandme.extension.ContextExtension.toast


class ContactListFragment : Fragment() {
    private var _binding: FragmentContactListBinding? = null
    private val binding get() = _binding!!
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
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

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        fun newInstance() = ContactListFragment()
    }
}