package com.lvinnie.game_2048.widget

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import androidx.databinding.DataBindingUtil
import com.lvinnie.game_2048.R
import com.lvinnie.game_2048.databinding.DialogGameBinding

/**
 * @author : Vinny
 * date : 2020/5/13
 * desc :
 */
class GameDialog: Dialog, View.OnClickListener {

    var binding: DialogGameBinding
    var title: String
    var message: String
    var confirm: String

    constructor(context: Context): super(context, R.style.Dialog) {

        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_game, null, false)

        title = context.resources.getString(R.string.tips)
        message = ""
        confirm = context.resources.getString(R.string.confirm)

        binding.confirm.setOnClickListener(this)

        setContentView(binding.root)
    }

    var onConfirmListener: OnConfirmListener? = null

    public interface OnConfirmListener {
        fun onConfirm()
    }

    override fun onClick(p0: View?) {
        dismiss()
        onConfirmListener?.onConfirm()
    }

    override fun show() {
        super.show()

        binding.title.text = title
        binding.message.text = message
        binding.confirm.text = confirm

    }

}