package com.lvinnie.game_2048.widget

import com.blankj.utilcode.util.SPUtils

/**
 * @author : Vinny
 * date : 2020/5/13
 * desc :
 */
class GameHelper {

    companion object {

        val BEST_SCORE = "BEST_SCORE"
        val GAME_DATA = "GAME_DATA"

        private var bestScore = -1

        fun init() {
            bestScore = SPUtils.getInstance().getInt(BEST_SCORE, 0)
        }

        fun setNewScore(score: Int): Boolean {
            if (score > bestScore) {
                bestScore = score
                SPUtils.getInstance().put(BEST_SCORE, score)
                return true
            } else {
                return false
            }
        }

        fun resetBestScore() {
            bestScore = 0
            SPUtils.getInstance().put(BEST_SCORE, 0)
        }

        fun getBestScore(): Int {
            return bestScore
        }

        fun saveGameData(data: String) {
            SPUtils.getInstance().put(GAME_DATA, data)
        }

        fun getGameData(): String {
            return SPUtils.getInstance().getString(GAME_DATA, "")
        }

        fun clearGameData() {
            saveGameData("")
        }
    }
}