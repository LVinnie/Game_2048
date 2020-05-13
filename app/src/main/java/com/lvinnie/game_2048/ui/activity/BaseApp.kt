package com.lvinnie.game_2048.ui.activity

import android.app.Application
import com.lvinnie.game_2048.widget.GameHelper

/**
 * @author : Vinny
 * date : 2020/5/11
 * desc :
 */
class BaseApp : Application() {

    override fun onCreate() {
        super.onCreate()

        GameHelper.init()
    }
}