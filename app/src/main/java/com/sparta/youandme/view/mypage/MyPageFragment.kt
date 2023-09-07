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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_my_page, container, false)

        val phoneNumberTextView = view.findViewById<TextView>(R.id.my_phone_number)
        val REQUEST_CALL_PHONE_PERMISSION = 1
        val myCallImageView = view.findViewById<ImageView>(R.id.my_call)
        myCallImageView.setOnClickListener {
            val phoneNumber = phoneNumberTextView.text.toString()

            // 권한이 이미 허용되었는지 확인
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                // 이미 권한이 허용된 경우, 전화 걸기 시도
                val intent = Intent(Intent.ACTION_CALL)
                intent.data = Uri.parse("tel:$phoneNumber")

                startActivity(intent)
            } else {
                // 권한이 없는 경우, 권한 요청
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CALL_PHONE),
                    REQUEST_CALL_PHONE_PERMISSION
                )
            }
        }


        val myMessageImage = view.findViewById<ImageView>(R.id.my_message)

        myMessageImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_APP_MESSAGING)
            startActivity(intent)
        }  // 기본 메세지 연결 버튼 구현

        val myEmailImageView = view.findViewById<ImageView>(R.id.my_emailbt)

        myEmailImageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
            }

            intent.setPackage("com.google.android.gm")

            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(requireContext(), "Gmail 앱을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }  // emailbt 클릭시 기본 gmail이 실행되도록 연결 버튼 구현

        val mySnsImageView = view.findViewById<ImageView>(R.id.my_snsbt)
        val snsTextView = view.findViewById<TextView>(R.id.my_sns)

        mySnsImageView.setOnClickListener {
            val searchText = snsTextView.text.toString()

            val intent = Intent(Intent.ACTION_WEB_SEARCH)
            intent.putExtra("query", searchText)
            startActivity(intent)
        }  // snsbt 클릭시 sns의 텍스트를 인터넷과 연동해 사이트와 연결, 버튼 구현

        val myblgoImageView = view.findViewById<ImageView>(R.id.my_blogbt)
        val blogTextView = view.findViewById<TextView>(R.id.my_blog_adress)

        myblgoImageView.setOnClickListener {
            val searchText = blogTextView.text.toString()

            val intent = Intent(Intent.ACTION_WEB_SEARCH)
            intent.putExtra("query", searchText)
            startActivity(intent)
        }  // blogbt 클릭시 blog의 텍스트를 인터넷과 연동해 사이트와 연결, 버튼 구현

        return view
    }

    companion object {
        fun newInstance() = MyPageFragment()
    }
}