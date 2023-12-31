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
import androidx.appcompat.app.AlertDialog
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
        } // 메세지 관련

        view.findViewById<ImageView>(R.id.my_emailbt).setOnClickListener {
            sendEmail()
        } // 이메일 관련

        view.findViewById<ImageView>(R.id.my_snsbt).setOnClickListener {
            searchOnWeb(view.findViewById<TextView>(R.id.my_sns).text.toString())
        } // sns관련 웹서치

        view.findViewById<ImageView>(R.id.my_blogbt).setOnClickListener {
            searchOnWeb(view.findViewById<TextView>(R.id.my_blog_adress).text.toString())
        } // blog관련 웹서치

        view.findViewById<ImageView>(R.id.my_call).setOnClickListener {
            showCallConfirmationDialog()
        } // 전화 관련

    }

    private fun openMessagingApp() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_APP_MESSAGING)
        Toast.makeText(requireContext(), "메세지앱을 실행합니다.", Toast.LENGTH_SHORT).show()
        startActivity(intent)
    }

    private fun sendEmail() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
        }
        Toast.makeText(requireContext(), "메일앱을 실행합니다.", Toast.LENGTH_SHORT).show()
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
        Toast.makeText(requireContext(), "해당 주소로 이동합니다.", Toast.LENGTH_SHORT).show()
        startActivity(intent)
    }

    private fun showCallConfirmationDialog() {
        val phoneNumber = phoneNumberTextView.text.toString()

        val alertDialogBuilder = AlertDialog.Builder(requireContext())

        alertDialogBuilder
            .setMessage("정말 전화를 거시겠습니까?")
            .setPositiveButton("예") { _, _ ->
                makePhoneCall(phoneNumber)
            }
            .setNegativeButton("아니오") { _, _ ->
            }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun makePhoneCall(phoneNumber: String) {
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
                1
            )
        }
    }


    companion object {
        fun newInstance() = MyPageFragment()
    }
}
