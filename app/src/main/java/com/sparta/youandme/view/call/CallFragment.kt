package com.sparta.youandme.view.call

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.sparta.youandme.R
import com.sparta.youandme.databinding.FragmentCallBinding


class CallFragment : Fragment() {
    private lateinit var binding : FragmentCallBinding

    private fun appendDigit(editText: EditText, digit: String) {
        val currentText = editText.text.toString()
        val newText = currentText + digit
        editText.setText(newText)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 뷰 바인딩을 통해 EditText 및 버튼 찾기
        val editTextPhoneNumber = binding.callNumber
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

       button0.setOnClickListener {
            appendDigit(editTextPhoneNumber, "0")
           buttonBackSpace.visibility = View.VISIBLE

       }
        button1.setOnClickListener {
            appendDigit(editTextPhoneNumber, "1")
            buttonBackSpace.visibility = View.VISIBLE
        }
        button2.setOnClickListener {
            appendDigit(editTextPhoneNumber, "2")
            buttonBackSpace.visibility = View.VISIBLE
        }
        button3.setOnClickListener {
            appendDigit(editTextPhoneNumber, "3")
            buttonBackSpace.visibility = View.VISIBLE
        }
        button4.setOnClickListener {
            appendDigit(editTextPhoneNumber, "4")
            buttonBackSpace.visibility = View.VISIBLE
        }
        button5.setOnClickListener {
            appendDigit(editTextPhoneNumber, "5")
            buttonBackSpace.visibility = View.VISIBLE
        }
        button6.setOnClickListener {
            appendDigit(editTextPhoneNumber, "6")
            buttonBackSpace.visibility = View.VISIBLE
        }
        button7.setOnClickListener {
            appendDigit(editTextPhoneNumber, "7")
            buttonBackSpace.visibility = View.VISIBLE
        }
        button8.setOnClickListener {
            appendDigit(editTextPhoneNumber, "8")
            buttonBackSpace.visibility = View.VISIBLE
        }
        button9.setOnClickListener {
            appendDigit(editTextPhoneNumber, "9")
            buttonBackSpace.visibility = View.VISIBLE
        }
        buttonBackSpace.setOnClickListener {
            val currentText = editTextPhoneNumber.text.toString()
            if (currentText.isNotEmpty()) {
                val newText = currentText.substring(0, currentText.length - 1)
                editTextPhoneNumber.setText(newText)
            }
            if (editTextPhoneNumber.text.isEmpty()){
                buttonBackSpace.visibility = View.GONE
            }
        }


    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCallBinding.inflate(inflater, container, false)
        return binding.root
   }




    companion object {
        fun newInstance() = CallFragment()
    }
}