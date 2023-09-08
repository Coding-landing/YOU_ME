package com.sparta.youandme.view.main.recyclerview.adapter

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater

import android.view.ViewGroup
import android.widget.CheckBox
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.sparta.youandme.R
import com.sparta.youandme.data.CallObjectData
import com.sparta.youandme.databinding.ItemCallInfoBinding
import com.sparta.youandme.databinding.ItemCallInfoGridBinding
import com.sparta.youandme.databinding.ItemCallInfoReversedBinding
import com.sparta.youandme.extension.ContextExtension.toast
import com.sparta.youandme.model.CallingObject
import com.sparta.youandme.model.ViewType
import com.sparta.youandme.view.main.MainActivity
import com.sparta.youandme.view.main.notification.AlarmReceiver
import com.sparta.youandme.view.main.recyclerview.listener.ItemClickListener
import java.lang.RuntimeException
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class ContactListAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var _list = arrayListOf<CallingObject>()
    val list: List<CallingObject>
        get() = _list
    private lateinit var itemClickListener: ItemClickListener
    private lateinit var obj: CallingObject
    private val listener =
        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            val idList = CallObjectData.list.map { it.id }
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val now = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA)
            val alarm =
                Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA).apply {
                    set(Calendar.HOUR_OF_DAY, hourOfDay)
                    set(Calendar.MINUTE, minute)
                }
            val bundle = Bundle().apply {
                putParcelable("obj", obj)
            }
            val receiverIntent = Intent(context, AlarmReceiver::class.java).apply {
                putExtra("bundle", bundle)
            }
            // 각자의 고유 코드 필요
            val code = idList.indexOf(obj.id)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                code,
                receiverIntent,
                PendingIntent.FLAG_MUTABLE
            )
            val hourDifference = now.get(Calendar.HOUR_OF_DAY) - hourOfDay
            val minuteDifference = minute - now.get(Calendar.MINUTE)
            println(receiverIntent)
            println(pendingIntent)
            context.toast(
                "${obj.name}님께 연락 할 수 있도록 ${hourDifference * 60 + minuteDifference}" +
                        "분 이후 알림"
            )
            // alarm.timeInMillis --> 진짜 알람 // 일단은 5초 있다가
            alarmManager.set(AlarmManager.RTC_WAKEUP, now.timeInMillis + 5, pendingIntent)
        }
    fun addItems(items: List<CallingObject>) {
        _list.clear()
        _list.addAll(items)
        notifyDataSetChanged()
    }

    fun setOnClickListener(listener: ItemClickListener) {
        itemClickListener = listener
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
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setTimePicker() {
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as MainActivity,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                1
            )
        }
        val localDate = LocalTime.now(ZoneId.of("Asia/Seoul"))
        val formatter = DateTimeFormatter.ofPattern("HH mm")
        val (hours, minutes) = localDate.format(formatter).split(" ").map { it.toInt() }
        TimePickerDialog(
            context,
            listener,
            hours,
            minutes,
            false
        ).show()
    }
    fun sortingLikedList(position: Int, isLiked: Boolean) {
        val targetItem = _list[position]
        var sortedList = sortingList()

        if(sortedList[position].id == targetItem.id) {
            targetItem.isLiked = !isLiked
            sortedList = sortingList()

        }
        addItems(sortedList)
        notifyItemMoved(position, sortedList.indexOf(targetItem))
    }
    private fun sortingList() =
        _list.sortedWith(compareByDescending<CallingObject> { it.isLiked }.thenBy { it.name })
            .onEachIndexed { index, callingObject ->
                callingObject.type =
                    if (index % 2 == 0) ViewType.LEFT_POSITION else ViewType.RIGHT_POSITION
            }

    private fun isLikedResources(isLiked: Boolean, checkBox: CheckBox) {
        when (isLiked) {
            true -> checkBox.setBackgroundResource(R.drawable.icon_heart_on)
            false -> checkBox.setBackgroundResource(R.drawable.icon_heart)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
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
        init {
            binding.root.setOnClickListener {
                itemClickListener.onItemClick(adapterPosition)
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
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
                    isLikedResources(isChecked, this)
                    sortingLikedList(adapterPosition, isChecked)
                }
            }
            notifyButton.setOnClickListener {
                obj = _list[adapterPosition]
                setTimePicker()
            }
        }
    }

    inner class ContactListRightViewHolder(private val binding: ItemCallInfoReversedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                itemClickListener.onItemClick(adapterPosition)
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
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
                    isLikedResources(isChecked, this)
                    sortingLikedList(adapterPosition, isChecked)
                }
            }
            notifyButton.setOnClickListener {
                obj = _list[adapterPosition]
                setTimePicker()
            }
        }
    }

    inner class ContactListGridViewHolder(private val binding: ItemCallInfoGridBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                itemClickListener.onItemClick(adapterPosition)
            }
        }

        fun bind(model: CallingObject) = with(binding) {
            gridImageView.setImageResource(model.imgId)
            gridTextView.text = model.name
        }
    }
}