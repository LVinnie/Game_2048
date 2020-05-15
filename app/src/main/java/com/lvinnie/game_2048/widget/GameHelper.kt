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

        fun test() {
            var data = "{" +
                    "\"score\":1000000," +
                    "\"count\":4," +
                    "\"data\":[" +
                    "{" +
                    "\"x\":0," +
                    "\"y\":0," +
                    "\"n\":4" +
                    "}," +
                    "{" +
                    "\"x\":0," +
                    "\"y\":1," +
                    "\"n\":4" +
                    "}," +
                    "{" +
                    "\"x\":0," +
                    "\"y\":2," +
                    "\"n\":8" +
                    "}," +
                    "{" +
                    "\"x\":0," +
                    "\"y\":3," +
                    "\"n\":16" +
                    "}," +
                    "{" +
                    "\"x\":1," +
                    "\"y\":3," +
                    "\"n\":32" +
                    "}," +
                    "{" +
                    "\"x\":1," +
                    "\"y\":2," +
                    "\"n\":64" +
                    "}," +
                    "{" +
                    "\"x\":1," +
                    "\"y\":1," +
                    "\"n\":128" +
                    "}," +
                    "{" +
                    "\"x\":1," +
                    "\"y\":0," +
                    "\"n\":256" +
                    "}," +
                    "{" +
                    "\"x\":2," +
                    "\"y\":0," +
                    "\"n\":512" +
                    "}," +
                    "{" +
                    "\"x\":2," +
                    "\"y\":1," +
                    "\"n\":1024" +
                    "}," +
                    "{" +
                    "\"x\":2," +
                    "\"y\":2," +
                    "\"n\":2048" +
                    "}," +
                    "{" +
                    "\"x\":2," +
                    "\"y\":3," +
                    "\"n\":4096" +
                    "}," +
                    "{" +
                    "\"x\":3," +
                    "\"y\":3," +
                    "\"n\":8192" +
                    "}," +
                    "{" +
                    "\"x\":3," +
                    "\"y\":2," +
                    "\"n\":16384" +
                    "}," +
                    "{" +
                    "\"x\":3," +
                    "\"y\":1," +
                    "\"n\":32768" +
                    "}," +
                    "{" +
                    "\"x\":3," +
                    "\"y\":0," +
                    "\"n\":65536" +
                    "}" +
                    "]" +
                    "}"

            saveGameData(data)
        }
    }
}