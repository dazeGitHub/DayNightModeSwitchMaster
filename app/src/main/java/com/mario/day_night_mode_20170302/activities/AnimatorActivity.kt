package com.mario.day_night_mode_20170302.activities

import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView

import com.mario.day_night_mode_20170302.MyApplication
import com.mario.day_night_mode_20170302.R
import com.mario.day_night_mode_20170302.common.KeyStore


/**
 * Created by Mario on 2017-03-05.
 */

class AnimatorActivity : BaseActivity() {

    var mode_change_animator_view: ImageView? = null

    private var mHandler: InternalHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val window = window
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)
        initAllViews()
        initAllDatum()
    }

    override fun initAllViews() {
        setContentView(R.layout.activity_animator)
    }

    override fun initAllDatum() {
        mode_change_animator_view!!.setImageResource(if (themeTag == -1) R.drawable.custom_drawable_mode_translation_turn_day_v16 else R.drawable.custom_drawable_mode_translation_turn_night_v16)
        sendEmptyMessageDelayed(KeyStore.KEY_TAG_ANIMATOR_START, 300)
    }

    override fun notifyByThemeChanged() {
        // TODO
    }

    private inner class InternalHandler : Handler() {

        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            if (msg == null) return
            when (msg.what) {
                KeyStore.KEY_SKIP_ANIMATOR_FINISH -> finish()
                KeyStore.KEY_TAG_ANIMATOR_START -> startAnimator()
                KeyStore.KEY_TAG_ANIMATOR_STOP -> stopAnimator()
                else -> {
                }
            }
        }

        /**
         *
         */
        private fun startAnimator() {
            val drawable = mode_change_animator_view!!.drawable
            if (drawable == null || drawable !is AnimationDrawable) return
            drawable.start()
            sendEmptyMessageDelayed(KeyStore.KEY_TAG_ANIMATOR_STOP, 1760)
        }

        /**
         *
         */
        private fun stopAnimator() {
            sendEmptyMessageDelayed(KeyStore.KEY_SKIP_ANIMATOR_FINISH, 360)
            switchCurrentThemeTag() //
            (application as MyApplication).notifyByThemeChanged() //
        }
    }

    /**
     */
    private fun sendEmptyMessageDelayed(what: Int, delay: Long) {
        if (mHandler == null) mHandler = InternalHandler()
        mHandler!!.sendEmptyMessageDelayed(what, delay)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}
