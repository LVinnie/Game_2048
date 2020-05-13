package com.lvinnie.game_2048.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.blankj.utilcode.util.ToastUtils
import com.lvinnie.game_2048.R
import com.lvinnie.game_2048.databinding.ActivityGameBinding
import com.lvinnie.game_2048.widget.GameDialog

class GameActivity : AppCompatActivity() {

    lateinit var binding: ActivityGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<ActivityGameBinding>(this, R.layout.activity_game)
        binding.gameContainer.scoreView = binding.score
        binding.gameContainer.bestView = binding.best
    }

    public fun click(view: View) {
        when (view.id) {
            R.id.restart -> {
                binding.gameContainer.clickRestart()
            }
            R.id.about -> {
                binding.gameContainer.about()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        binding.gameContainer.save()
    }
}
