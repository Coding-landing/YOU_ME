package com.sparta.youandme.view.addcontact

import android.Manifest
import android.app.Activity.RESULT_OK
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
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.sparta.youandme.R
import com.sparta.youandme.databinding.FragmentAddContactDialogBinding
import com.sparta.youandme.extension.ContextExtension.toast
import com.sparta.youandme.model.CallingObject
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
    private  var uri1 : Uri? =null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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
                    imgId = R.drawable.icon_user_profile,
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
        val diaLayout = binding.diaLayout
        diaLayout.setOnClickListener {
            // 클릭 시 현재 프래그먼트를 종료하고 메인 화면으로 이동
            parentFragmentManager.beginTransaction().remove(this).commit()
            val activity = requireActivity() as MainActivity
            val viewPager = activity.findViewById<ViewPager2>(R.id.view_pager)
            viewPager.isVisible = true
            viewPager.setCurrentItem(0, false)
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
            }
            else {
                selectGallery()
            }
        }
        )

    }
    private fun selectGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        imageResult.launch(intent)
    }
    private val imageResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
            result ->
        if (result.resultCode == RESULT_OK){
            val imageUri = result.data?.data
                println(imageUri)
            var uri =  getRealPathFromURI(imageUri!!)
            uri1 = Uri.parse(uri)
            println(uri)
//            val input = (requireActivity() as MainActivity).contentResolver.openInputStream(imageUri)
//            var draw = Drawable.createFromStream(input, "123.jpg")
            binding.diaImg.setImageURI(Uri.parse(uri))
            }
        }


    private fun setFragment(frag: Fragment, bundle: Bundle) {
        val activity = (requireActivity() as MainActivity)
        val viewPager = activity.findViewById<ViewPager2>(R.id.view_pager)
        parentFragmentManager.beginTransaction().remove(this).commit()
        frag.onDestroy()
        frag.onDetach()
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
    fun getRealPathFromURI(uri: Uri):String{
        val buildName = Build.MANUFACTURER
        if(buildName.equals("Xiami")){
            return uri.path!!
        }
        var columnIndex = 0
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = (requireActivity() as MainActivity).contentResolver.query(uri,proj,null,null,null)
        if (cursor!!.moveToFirst()){
            columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        }
        val  result = cursor.getString(columnIndex)
        cursor.close()
        return result}


    companion object {
        fun newInstance() = AddContactDialogFragment()
        const val REVIEW_MIN_LENGTH = 10
        const val REQ_GALLERY = 1

        const val  PRAM_KEY_IMAGE ="image"
        const val  PRAM_KEY_PRODUCT_ID = "product_id"
        const val  PRAM_KEY_REVIEW = "review_content"
        const val  PRAM_KEY_RATING = "rating" }



    }

