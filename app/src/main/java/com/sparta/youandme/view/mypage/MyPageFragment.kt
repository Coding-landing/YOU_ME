package com.sparta.youandme.view.mypage

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import android.Manifest
import com.sparta.youandme.R

class MyPageFragment : Fragment() {

    private lateinit var phoneNumberTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_page, container, false)

        phoneNumberTextView = view.findViewById(R.id.my_phone_number)

        setupClickListeners(view)

        return view
    }

    private fun setupClickListeners(view: View) {
        view.findViewById<ImageView>(R.id.my_message).setOnClickListener {
            openMessagingApp()
        }

        view.findViewById<ImageView>(R.id.my_emailbt).setOnClickListener {
            sendEmail()
        }

        view.findViewById<ImageView>(R.id.my_snsbt).setOnClickListener {
            searchOnWeb(view.findViewById<TextView>(R.id.my_sns).text.toString())
        }

        view.findViewById<ImageView>(R.id.my_blogbt).setOnClickListener {
            searchOnWeb(view.findViewById<TextView>(R.id.my_blog_adress).text.toString())
        }

        view.findViewById<ImageView>(R.id.my_call).setOnClickListener {
            callPhoneNumber()
        }
    }

    private fun openMessagingApp() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_APP_MESSAGING)
        startActivity(intent)
    }

    private fun sendEmail() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
        }
        intent.setPackage("com.google.android.gm")

        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                requireContext(),
                "Gmail 앱을 찾을 수 없습니다.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun searchOnWeb(query: String) {
        val intent = Intent(Intent.ACTION_WEB_SEARCH)
        intent.putExtra("query", query)
        startActivity(intent)
    }

    private fun callPhoneNumber() {
        val phoneNumber = phoneNumberTextView.text.toString()

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:$phoneNumber")
            startActivity(intent)
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CALL_PHONE),
                1 // REQUEST_CALL_PHONE_PERMISSION 변수 대신 직접 사용
            )
        }
    }

    companion object {
        fun newInstance() = MyPageFragment()
    }
}
