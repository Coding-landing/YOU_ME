package com.sparta.youandme.view.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.sparta.youandme.R
import com.sparta.youandme.data.CallObjectData
import com.sparta.youandme.databinding.FragmentCallBinding
import com.sparta.youandme.databinding.FragmentContactDetailBinding
import com.sparta.youandme.model.CallingObject

class ContactDetailFragment : Fragment() {

    private lateinit var binding: FragmentContactDetailBinding
    private lateinit var data: CallingObject
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        data = CallObjectData.list.first()

        binding = FragmentContactDetailBinding.bind(view)
        binding.detailMessageBtn.setOnClickListener {
            val phoneNumber = data.mobileNumber // data로 넘기면 될 듯함
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("sms:$phoneNumber")
            startActivity(intent)
        }

        binding.detailMyName.text = data.name
        binding.detailCallNum.text = data.mobileNumber
        binding.detailSnsAddress.text = data.snsAddress
        binding.detailEmail.text = data.email
        binding.detailMbti.text = data.mbti
        binding.detailNickName.text = data.nickName
        binding.detailPicture.setImageResource(data.imgId)
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
