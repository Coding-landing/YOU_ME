package com.sparta.youandme.view.addcontact

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
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
    private var uri1: Uri? = null
    private lateinit var viewPager: ViewPager2
    private lateinit var fab: FloatingActionButton
    private lateinit var tabLayout: TabLayout
    private lateinit var activity: MainActivity
    private lateinit var callback: OnBackPressedCallback

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddContactDialogBinding.bind(view)
        activity = requireActivity() as MainActivity
        fab = activity.findViewById(R.id.fab_add_todo)
        tabLayout = activity.findViewById(R.id.tab_layout)
        val context = requireContext()
        val btn = binding.diaBtnWhite
        editelnum = binding.diaEditTelNum
        editmail = binding.diaEditMail
        editsns = binding.diaEditSns
        editname = binding.diaEditName
        editblog = binding.diaEditBlogAddress
        editmbit = binding.diaEditMbti
        editnickname = binding.diaEditNickname
        btn.setOnClickListener {
            editelnum.addTextChangedListener(createUserTextWatcher(editelnum))
            editmail.addTextChangedListener(createUserTextWatcher(editmail))
            editname.addTextChangedListener(createUserTextWatcher(editname))
            editsns.addTextChangedListener(createurlcheckWatcher(editsns))
            editblog.addTextChangedListener(createurlcheckWatcher(editblog))
            if (!editelnum.text.toString().isEmpty() && !editname.text.toString()
                    .isEmpty() && !editmail.text.toString().isEmpty()
            ) {
                context.toast("연락처 추가 성공!")
                btn.isEnabled = true
                val callingobject = CallingObject(
                    id = UUID.randomUUID().toString().trim(),
                    name = editname.text.toString().trim(),
                    mobileNumber = editelnum.text.toString().trim(),
                    email = editmail.text.toString().trim(),
                    snsAddress = editsns.text.toString().trim(),
                    mbti = editmbit.text.toString().trim(),
                    nickName = editnickname.text.toString().trim(),
                    blogAddress = editblog.text.toString().trim(),
                    imgId = uri1,
                    type = -1
                )
                val bundle = Bundle().apply {
                    putParcelable("Ncontact", callingobject)
                }
                setFragment(this, bundle)
            } else if (editelnum.text.toString().isEmpty() || editname.text.toString()
                    .isEmpty() || editmail.text.toString().isEmpty()
            ) {
                context.toast("필수 정보를 모두 입력해주세요.")
                return@setOnClickListener
            } else {
                binding.diaBtnWhite.isEnabled = false
                context.toast("다시 확인해주세요.")
                return@setOnClickListener
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
        val back = binding.diaIconBack
        back.setOnClickListener {
            setFragment(this@AddContactDialogFragment, Bundle())
        }

        val img = binding.diaImg
        img.setOnClickListener(View.OnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_MEDIA_IMAGES
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                (ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(
                        Manifest.permission.READ_MEDIA_IMAGES,
                    ),
                    1
                )
                        )
            } else {
                selectGallery()
            }
        })
    }

    private fun selectGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        imageResult.launch(intent)
    }

    private val imageResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val imageUri = result.data?.data
            var uri = getRealPathFromURI(imageUri!!)
            uri1 = Uri.parse(uri)
            binding.diaImg.setImageURI(Uri.parse(uri))
        }
    }


    private fun setFragment(frag: Fragment, bundle: Bundle) {
        val manager = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(
            activity.currentFocus?.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
        viewPager = activity.findViewById(R.id.view_pager)
        parentFragmentManager.beginTransaction().remove(this).commit()
        frag.onDestroy()
        frag.onDetach()
        viewPager.isVisible = true
        tabLayout.isVisible = true
        fab.show()
        viewPager.setCurrentItem(0, false)
        if (bundle.isEmpty.not()) {
            parentFragmentManager.setFragmentResult("Bundle", bundle)
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
                val context = requireContext()
                val name = editname.text.toString()
                val email = editmail.text.toString()
                val number = editelnum.text.toString()
                val numberPattern = "^01([016789])-?([0-9]{3,4})-?([0-9]{4})$".toRegex()
                val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
                binding.diaBtnWhite.isEnabled =
                    !number.isEmpty() && !email.isEmpty() && !name.isEmpty() && isEmailValid && number.matches(
                        numberPattern
                    )
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
                binding.diaBtnWhite.isEnabled =
                    blog.isEmpty() && sns.isEmpty() && blog.matches(urlPattern) && sns.matches(
                        urlPattern
                    )
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

    private fun getRealPathFromURI(uri: Uri): String {
        val buildName = Build.MANUFACTURER
        if (buildName.equals("Xiami")) {
            return uri.path!!
        }
        var columnIndex = 0
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor =
            (activity).contentResolver.query(
                uri,
                proj,
                null,
                null,
                null
            )
        if (cursor!!.moveToFirst()) {
            columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        }
        val result = cursor.getString(columnIndex)
        cursor.close()
        return result
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                setFragment(this@AddContactDialogFragment, Bundle())
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    override fun onResume() {
        super.onResume()
        fab.hide()
        tabLayout.isVisible = false
    }
    companion object {
        fun newInstance() = AddContactDialogFragment()
    }
}

