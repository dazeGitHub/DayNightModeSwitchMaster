package com.mario.day_night_mode_20170302.activities

import android.content.Intent
import android.os.Message
import android.os.Handler
import android.os.Bundle

import com.mario.day_night_mode_20170302.R
import com.mario.day_night_mode_20170302.common.KeyStore

/**
 * Created by Mario on 2017-03-02.
 */

class LaunchActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)
        initAllViews()
        initAllDatum()
    }

    override fun initAllViews() {
        //TODO
    }

    override fun initAllDatum() {
        InternalHandler().sendEmptyMessageDelayed(KeyStore.KEY_SKIP_LUNCH_TO_MAIN, 800)
    }

    override fun notifyByThemeChanged() {
        // TODO
    }

    private inner class InternalHandler : Handler() {

        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            if (msg == null) return
            when (msg.what) {
                KeyStore.KEY_SKIP_LUNCH_TO_MAIN -> {
                    val intent = Intent(context, ThemeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else -> {
                }
            }
        }
    }
}
