package com.sparta.youandme.view.main.recyclerview.adapter

import android.view.LayoutInflater

import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.sparta.youandme.R
import com.sparta.youandme.databinding.ItemCallInfoBinding
import com.sparta.youandme.databinding.ItemCallInfoGridBinding
import com.sparta.youandme.databinding.ItemCallInfoReversedBinding
import com.sparta.youandme.model.CallingObject
import com.sparta.youandme.model.ViewType
import java.lang.RuntimeException

class ContactListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val _list = arrayListOf<CallingObject>()
    val list: List<CallingObject>
        get() = _list


    fun addItems(items: List<CallingObject>) {
        _list.clear()
        _list.addAll(items)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
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

            ViewType.GRID_POSITION -> {
                ContactListGridViewHolder(
                    ItemCallInfoGridBinding.inflate(
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
        return _list[position].type
    }

    override fun getItemCount(): Int = _list.size

    fun sortingLikedList() {
        val sortedList = _list.sortedByDescending { it.isLiked }
        addItems(sortedList)
    }

    private fun isLikedResources(isLiked: Boolean, checkBox: CheckBox) {
        when(isLiked) {
            true -> checkBox.setBackgroundResource(R.drawable.icon_heart_on)
            false -> checkBox.setBackgroundResource(R.drawable.icon_heart)
        }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = _list[position]
        when (item.type) {
            ViewType.LEFT_POSITION -> (holder as ContactListLeftViewHolder).bind(item)
            ViewType.RIGHT_POSITION -> (holder as ContactListRightViewHolder).bind(item)
            ViewType.GRID_POSITION -> (holder as ContactListGridViewHolder).bind(item)
        }
    }

    inner class ContactListLeftViewHolder(private val binding: ItemCallInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: CallingObject) = with(binding) {
            peopleImageView.setImageResource(model.imgId)
            nameTextView.text = model.name
            phoneNumberTextView.text = model.mobileNumber
            model.nickName.run {
                when (isNullOrEmpty()) {
                    true -> nameNicknameView.text = ""
                    false -> "(${model.nickName})".also { nameNicknameView.text = it }
                }
            }
            likedCheckBox.run {
                isLikedResources(model.isLiked, this)
                setOnCheckedChangeListener { _, isChecked ->
                    model.isLiked = isChecked
                    isLikedResources(model.isLiked, this)
                }
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
                when (isNullOrEmpty()) {
                    true -> nameNicknameView.text = ""
                    false -> "(${model.nickName})".also { nameNicknameView.text = it }
                }
            }

            likedCheckBox.run {
                isLikedResources(model.isLiked, this)
                setOnCheckedChangeListener { _, isChecked ->
                    model.isLiked = isChecked
                    isLikedResources(model.isLiked, this)
                }
            }
        }
    }

    inner class ContactListGridViewHolder(private val binding: ItemCallInfoGridBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: CallingObject) = with(binding) {
            gridImageView.setImageResource(model.imgId)
            gridTextView.text = model.name
        }
    }
}