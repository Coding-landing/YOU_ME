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
import com.sparta.youandme.R
import com.sparta.youandme.databinding.FragmentAddContactDialogBinding
import com.sparta.youandme.extension.ContextExtension.toast
import java.util.regex.Pattern


class AddContactDialogFragment : Fragment() {
    private lateinit var binding: FragmentAddContactDialogBinding
    private lateinit var editelnum: EditText
    private lateinit var editmail: EditText
    private lateinit var editsns: EditText
    private lateinit var editname: EditText
    private lateinit var editblog: EditText
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddContactDialogBinding.bind(view)
        val btn = binding.diaBtnWhite
        editelnum = binding.diaEditTelNum
        editmail = binding.diaEditMail
        editsns = binding.diaEditSns
        editname = binding.diaEditName
        editblog = binding.diaEditBlogAddress
        val context = requireContext()
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
            } else {
                context.toast("다시 확인해주세요.")
            }
        }

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
                val name = editname.text.toString()
                val email = editmail.text.toString()
                val number = editelnum.text.toString()
                val numberPattern = "^01([016789])-?([0-9]{3,4})-?([0-9]{4})$".toRegex()
                val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
                if (!number.isEmpty() && !email.isEmpty() && !name.isEmpty() && isEmailValid && number.matches(
                        numberPattern)
                    )
                 {
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
                val urlPattern = "^(https?://)?[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}(\\/[a-zA-Z0-9/?=&-]*)?\$".toRegex()
                if (blog.matches(urlPattern) && sns.matches(urlPattern)){
                    binding.diaBtnWhite.isEnabled = true
                }
                else{binding.diaBtnWhite.isEnabled = false}

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
