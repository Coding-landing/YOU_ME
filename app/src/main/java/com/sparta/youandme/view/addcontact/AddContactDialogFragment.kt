package com.sparta.youandme.view.addcontact

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentContainerView
import androidx.viewpager2.widget.ViewPager2
import com.sparta.youandme.R
import com.sparta.youandme.databinding.FragmentAddContactDialogBinding
import com.sparta.youandme.extension.ContextExtension.toast
import com.sparta.youandme.model.CallingObject
import com.sparta.youandme.view.main.ContactListFragment
import com.sparta.youandme.view.main.MainActivity
import java.util.UUID


class AddContactDialogFragment : Fragment() {
    private lateinit var binding: FragmentAddContactDialogBinding
    private lateinit var editelnum: EditText
    private lateinit var editmail: EditText
    private lateinit var editsns: EditText
    private lateinit var editname: EditText
    private lateinit var editblog: EditText
    private lateinit var editmbit: EditText
    private lateinit var editnickname: EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddContactDialogBinding.bind(view)
        val context = requireContext()
        val btn = binding.diaBtnWhite
        editelnum = binding.diaEditTelNum
        editmail = binding.diaEditMail
        editsns = binding.diaEditSns
        editname = binding.diaEditName
        editblog = binding.diaEditBlogAddress
        editmbit = binding.diaEditMbti
        editnickname = binding.diaEditNickname
        editelnum.addTextChangedListener(createUserTextWatcher(editelnum))
        editmail.addTextChangedListener(createUserTextWatcher(editmail))
        editname.addTextChangedListener(createUserTextWatcher(editname))
        editsns.addTextChangedListener(createurlcheckWatcher(editsns))
        editblog.addTextChangedListener(createurlcheckWatcher(editblog))
        btn.setOnClickListener {
            if (!editelnum.text.toString().isEmpty() && !editname.text.toString()
                    .isEmpty() && !editmail.text.toString().isEmpty()
            ) {
                context.toast("연락처 추가 성공!")
                btn.isEnabled = true
                val callingobject = CallingObject(
                    id = UUID.randomUUID().toString(),
                    name = editname.text.toString(),
                    mobileNumber = editelnum.text.toString(),
                    email = editmail.text.toString(),
                    snsAddress = editsns.text.toString(),
                    mbti = editmbit.text.toString(),
                    nickName = editnickname.text.toString(),
                    blogAddress = editblog.text.toString(),
                    imgId = R.drawable.bg_hyerin,
                    type = -1
                )
                val bundle = Bundle().apply {
                    putParcelable("Ncontact", callingobject)
                }
               println(bundle)
                setFragment(this, bundle)
            } else if(!editelnum.text.toString().isEmpty() && !editname.text.toString()
                .isEmpty() && !editmail.text.toString().isEmpty()){
                context.toast("다시 확인해주세요.")
            }
            else {
            binding.diaBtnWhite.isEnabled = false
                context.toast("다시 확인해주세요.")
            }
        }
        val cons2 = binding.diaCons2
        val check = binding.diaCheckBox
        check.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                cons2.visibility = View.VISIBLE
            } else {
                cons2.visibility = View.GONE
            }
        }
    }

    private fun setFragment(frag: Fragment, bundle: Bundle) {
        val activity = (requireActivity() as MainActivity)
        val viewPager = activity.findViewById<ViewPager2>(R.id.view_pager)
        parentFragmentManager.beginTransaction().remove(this).commit()
        frag.onDestroy()
        frag.onDestroy()
        viewPager.isVisible = true
        viewPager.setCurrentItem(0, false)
        parentFragmentManager.setFragmentResult("Bundle", bundle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_contact_dialog, container, false)
    }


    fun createUserTextWatcher(editText: EditText): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val context = requireContext()
                val name = editname.text.toString()
                val email = editmail.text.toString()
                val number = editelnum.text.toString()
                val numberPattern = "^01([016789])-?([0-9]{3,4})-?([0-9]{4})$".toRegex()
                val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
                if (!number.isEmpty() && !email.isEmpty() && !name.isEmpty() && isEmailValid && number.matches(
                        numberPattern
                    )
                ) {
                    binding.diaBtnWhite.isEnabled = true
                } else {
                    binding.diaBtnWhite.isEnabled = false
                }
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
            }
        }
    }

    fun createurlcheckWatcher(editText: EditText): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val blog = editblog.text.toString()
                val sns = editsns.text.toString()
                val urlPattern =
                    "^(https?://)?[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}(\\/[a-zA-Z0-9/?=&-]*)?\$".toRegex()
                if (blog.isEmpty() && sns.isEmpty() && blog.matches(urlPattern) && sns.matches(
                        urlPattern
                    )
                ) {
                    binding.diaBtnWhite.isEnabled = true
                } else {
                    binding.diaBtnWhite.isEnabled = false
                }

            }

            override fun beforeTextChanged(

                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
            }
        }
    }


    companion object {
        fun newInstance() = AddContactDialogFragment()
    }
}
