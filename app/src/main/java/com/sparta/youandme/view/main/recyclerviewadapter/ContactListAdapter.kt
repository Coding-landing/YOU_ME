package com.sparta.youandme.view.main.recyclerviewadapter

import android.view.LayoutInflater

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sparta.youandme.R
import com.sparta.youandme.databinding.ItemCallInfoBinding
import com.sparta.youandme.databinding.ItemCallInfoReversedBinding
import com.sparta.youandme.model.CallingObject
import com.sparta.youandme.model.ViewType
import java.lang.RuntimeException

class ContactListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val list = arrayListOf<CallingObject>()

    fun addItems(items: List<CallingObject>) {
        list.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            ViewType.LEFT_POSITION -> {
                ContactListLeftViewHolder(
                    ItemCallInfoBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            ViewType.RIGHT_POSITION -> {
                ContactListRightViewHolder(
                    ItemCallInfoReversedBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            else -> throw RuntimeException("disable view type")
        }

    }

    override fun getItemViewType(position: Int): Int {
        return list[position].type
    }
    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        when(item.type) {
            ViewType.LEFT_POSITION -> (holder as ContactListLeftViewHolder).bind(item)
            ViewType.RIGHT_POSITION -> (holder as ContactListRightViewHolder).bind(item)
        }
    }

    inner class ContactListLeftViewHolder(private val binding: ItemCallInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: CallingObject) = with(binding) {
            peopleImageView.setImageResource(model.imgId)
            nameTextView.text = model.name
            phoneNumberTextView.text = model.mobileNumber
            model.nickName.run {
                when(isNullOrEmpty()) {
                    true -> nameNicknameView.text = ""
                    false -> "(${model.nickName})".also { nameNicknameView.text = it }
                }
            }
            if(model.isLiked) {
                likedCheckBox.setBackgroundResource(R.drawable.icon_heart_on)
            } else {
                likedCheckBox.setBackgroundResource(R.drawable.icon_heart)
            }
        }
    }
    inner class ContactListRightViewHolder(private val binding: ItemCallInfoReversedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: CallingObject) = with(binding) {
            peopleImageView.setImageResource(model.imgId)
            nameTextView.text = model.name
            phoneNumberTextView.text = model.mobileNumber
            model.nickName.run {
                when(isNullOrEmpty()) {
                    true -> nameNicknameView.text = ""
                    false -> "(${model.nickName})".also { nameNicknameView.text = it }
                }
            }
            if(model.isLiked) {
                likedCheckBox.setBackgroundResource(R.drawable.icon_heart_on)
            } else {
                likedCheckBox.setBackgroundResource(R.drawable.icon_heart)
            }
        }
    }

}