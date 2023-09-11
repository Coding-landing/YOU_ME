package com.sparta.youandme.view.call

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.sparta.youandme.databinding.FragmentCallBinding
import com.sparta.youandme.model.CallingObject



class CallFragment : Fragment() {
    private lateinit var binding: FragmentCallBinding
    private lateinit var editTextCallNumber: EditText
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            editTextCallNumber.text.clear()

        }
    }

    private fun appendDigit(editText: EditText, digit: String) {
        val currentText = editText.text.toString()
        val newText = currentText + digit
        editText.setText(newText)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 뷰 바인딩을 통해 EditText 및 버튼 찾기
        editTextCallNumber = binding.callNumber
        val button0 = binding.callZero
        val button1 = binding.callOne
        val button2 = binding.callTwo
        val button3 = binding.callThree
        val button4 = binding.callFour
        val button5 = binding.callFive
        val button6 = binding.callSix
        val button7 = binding.callSeven
        val button8 = binding.callEight
        val button9 = binding.callNine
        val buttonBackSpace = binding.callBackspace
        val buttonCall = binding.callCalling

        button0.setOnClickListener {
            appendDigit(editTextCallNumber, "0")
            buttonBackSpace.visibility = View.VISIBLE

        }
        button1.setOnClickListener {
            appendDigit(editTextCallNumber, "1")
            buttonBackSpace.visibility = View.VISIBLE
        }
        button2.setOnClickListener {
            appendDigit(editTextCallNumber, "2")
            buttonBackSpace.visibility = View.VISIBLE
        }
        button3.setOnClickListener {
            appendDigit(editTextCallNumber, "3")
            buttonBackSpace.visibility = View.VISIBLE
        }
        button4.setOnClickListener {
            appendDigit(editTextCallNumber, "4")
            buttonBackSpace.visibility = View.VISIBLE
        }
        button5.setOnClickListener {
            appendDigit(editTextCallNumber, "5")
            buttonBackSpace.visibility = View.VISIBLE
        }
        button6.setOnClickListener {
            appendDigit(editTextCallNumber, "6")
            buttonBackSpace.visibility = View.VISIBLE
        }
        button7.setOnClickListener {
            appendDigit(editTextCallNumber, "7")
            buttonBackSpace.visibility = View.VISIBLE
        }
        button8.setOnClickListener {
            appendDigit(editTextCallNumber, "8")
            buttonBackSpace.visibility = View.VISIBLE
        }
        button9.setOnClickListener {
            appendDigit(editTextCallNumber, "9")
            buttonBackSpace.visibility = View.VISIBLE
        }

        editTextCallNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {

                buttonBackSpace.visibility =
                    if (s?.isNotEmpty() == true) View.VISIBLE else View.GONE

                val formattedPhoneNumber = formatPhoneNumber(s.toString())
                if (editTextCallNumber.text.toString() != formattedPhoneNumber) {
                    editTextCallNumber.removeTextChangedListener(this)
                    editTextCallNumber.setText(formattedPhoneNumber)
                    editTextCallNumber.setSelection(formattedPhoneNumber.length)
                    editTextCallNumber.addTextChangedListener(this)
                }
            }
        })


        buttonBackSpace.setOnClickListener {
            val currentText = editTextCallNumber.text.toString()
            if (currentText.isNotEmpty()) {
                val newText = currentText.substring(0, currentText.length - 1)
                editTextCallNumber.setText(newText)
            }
            if (editTextCallNumber.text.isEmpty()) {
                buttonBackSpace.visibility = View.GONE
            }
        }
        buttonCall.setOnClickListener {
            val phoneNumber = editTextCallNumber.text.toString()


            if (phoneNumber.isNotEmpty()) {
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.CALL_PHONE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    (ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.CALL_PHONE),
                        1
                    )
                            )
                } else {
                    val callIntent = Intent(Intent.ACTION_CALL)
                    callIntent.data = Uri.parse("tel:$phoneNumber")
                    startActivityForResult(callIntent, 1)
                }
            }

        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCallBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        var obj: CallingObject? = null
        parentFragmentManager.setFragmentResultListener(
            "callObject",
            viewLifecycleOwner
        ) { _, bundle ->
            obj = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable("model", CallingObject::class.java)
            } else {
                bundle.getParcelable("model")
            }
            if (obj != null) // -> 데이터 받는 것 확인
                editTextCallNumber.setText(obj?.mobileNumber)
        }

    }

    companion object {
        fun newInstance() = CallFragment()

    }
}

