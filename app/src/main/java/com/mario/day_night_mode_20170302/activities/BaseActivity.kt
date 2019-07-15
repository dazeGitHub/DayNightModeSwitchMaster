package com.mario.day_night_mode_20170302.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import android.view.WindowManager

import com.mario.day_night_mode_20170302.MyApplication
import com.mario.day_night_mode_20170302.R
import com.mario.day_night_mode_20170302.interfaces.ThemeChangeObserver

/**
 * Created by Mario on 2017-03-02.
 */

abstract class BaseActivity : AppCompatActivity(), ThemeChangeObserver {

    private val KEY_MARIO_CACHE_THEME_TAG = "MarioCache_themeTag"

    /**
     */
    val statusBarHeight: Int
        get() {
            var result = 0
            val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = context.resources.getDimensionPixelSize(resourceId)
            }
            return result
        }

    /**
     */
    /**
     */
    protected var themeTag: Int
        get() {
            val preferences = getSharedPreferences("MarioCache", Context.MODE_PRIVATE)
            return preferences.getInt(KEY_MARIO_CACHE_THEME_TAG, 1)
        }
        set(tag) {
            val preferences = getSharedPreferences("MarioCache", Context.MODE_PRIVATE)
            val edit = preferences.edit()
            edit.putInt(KEY_MARIO_CACHE_THEME_TAG, tag)
            edit.commit()
        }

    /**
     */
    val context: Context
        get() = this@BaseActivity

    abstract fun initAllViews()
    abstract fun initAllDatum()

    override fun onCreate(savedInstanceState: Bundle?) {
        setupActivityBeforeCreate()
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val window = window
            val attributes = window.attributes
            attributes.flags = attributes.flags or WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            window.attributes = attributes
        }
    }

    /**
     */
    private fun setupActivityBeforeCreate() {
        (application as MyApplication).registerObserver(this)
        loadingCurrentTheme()
    }

    override fun loadingCurrentTheme() {
        when (themeTag) {
            1 -> setTheme(R.style.MarioTheme_Day)
            -1 -> setTheme(R.style.MarioTheme_Night)
        }
    }

    override fun setContentView(@LayoutRes layoutResID: Int) {
        super.setContentView(layoutResID)
        val status = findViewById<View>(R.id.custom_id_title_status_bar)
        if (status != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            status!!.getLayoutParams().height = statusBarHeight
        }
    }

    /**
     */
    fun switchCurrentThemeTag() {
        themeTag = 0 - themeTag
        loadingCurrentTheme()
    }

    override fun onDestroy() {
        (application as MyApplication).unregisterObserver(this)
        super.onDestroy()
    }
}
