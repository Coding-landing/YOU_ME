package com.sparta.youandme.view.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sparta.youandme.R
import com.sparta.youandme.databinding.FragmentContactDetailBinding
import com.sparta.youandme.model.CallingObject
import com.sparta.youandme.view.main.MainActivity

class ContactDetailFragment : Fragment() {

    private lateinit var binding: FragmentContactDetailBinding
    private var data: CallingObject? = null
    private lateinit var callback: OnBackPressedCallback
    private lateinit var viewPager: ViewPager2
    private lateinit var fab: FloatingActionButton
    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewPager =
            (requireActivity() as MainActivity).findViewById(R.id.view_pager)
        fab = (requireActivity() as MainActivity).findViewById(R.id.fab_add_todo)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                this@ContactDetailFragment.onDestroy()
                this@ContactDetailFragment.onDetach()
                parentFragmentManager.beginTransaction().remove(this@ContactDetailFragment).commit()
                fab.show()
                viewPager.isVisible = true
                viewPager.setCurrentItem(0, false)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        data = arguments?.getParcelable<CallingObject>("model")

        binding = FragmentContactDetailBinding.bind(view)

        binding.detailMessageBtn.setOnClickListener {
            val phoneNumber = data?.mobileNumber // data로 넘기면 될 듯함
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("sms:$phoneNumber")
            startActivity(intent)
        }

        binding.detailCallBtn.setOnClickListener {
            val bundle = Bundle().apply {
                putParcelable("model", data)
            }
            this@ContactDetailFragment.onDestroy()
            this@ContactDetailFragment.onDetach()
            viewPager.setCurrentItem(2, true)
            parentFragmentManager.setFragmentResult("callObject", bundle)
        }

        binding.detailMyName.text = data?.name
        binding.detailCallNum.text = data?.mobileNumber
        binding.detailSnsAddress.text = data?.snsAddress
        binding.detailEmail.text = data?.email
        binding.detailMbti.text = data?.mbti
        binding.detailNickName.text = data?.nickName
        binding.detailPicture.setImageURI(data?.imgId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contact_detail, container, false)
    }

    companion object {
        fun newInstance() = ContactDetailFragment()
    }
}
