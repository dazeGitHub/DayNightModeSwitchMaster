package com.mario.day_night_mode_20170302.activities

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.bumptech.glide.Glide
import com.mario.day_night_mode_20170302.MyApplication
import com.mario.day_night_mode_20170302.R
import com.mario.day_night_mode_20170302.common.GlideCircleTransform
import com.mario.day_night_mode_20170302.fragments.MainFragment
import com.mario.day_night_mode_20170302.helpers.MarioResourceHelper

import kotlinx.android.synthetic.main.activity_theme.*

/**
 * Created by Mario on 2017-03-03.
 */

class ThemeActivity : BaseActivity() {

    private val tag_is_animation = false
    private var theme_user_photoPlaceHolder: Drawable? = null

    private var mAdapter: Custotheme_view_pagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAllViews()
        initAllDatum()
        theme_btn_turn_day.setOnClickListener{
            onClicked()
        }
        theme_btn_turn_night.setOnClickListener{
            onClicked()
        }
    }

    override fun initAllViews() {
        setContentView(R.layout.activity_theme)
    }

    override fun initAllDatum() {
        mAdapter = Custotheme_view_pagerAdapter(supportFragmentManager)
        theme_view_pager.setAdapter(mAdapter)

        initBtnStatus()
        theme_user_photoPlaceHolder = MarioResourceHelper.getInstance(context).getDrawableByAttr(R.attr.custom_attr_user_photo_place_holder)
        Glide.with(context).load(resources.getString(R.string.user_photo_url)).placeholder(theme_user_photoPlaceHolder).transform(GlideCircleTransform(context)).into(theme_user_photo)
    }

    private fun initBtnStatus() {
        val tag = themeTag
        theme_btn_turn_day.setEnabled(if (tag == 1) false else true)
        theme_btn_turn_night.setEnabled(if (tag == 1) true else false)
    }

    fun onClicked() {
        if (tag_is_animation) {
            val intent = Intent(context, AnimatorActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        } else {
            switchCurrentThemeTag()
            (application as MyApplication).notifyByThemeChanged()
            //            recreate();
        }
    }

    override fun notifyByThemeChanged() {
        val helper = MarioResourceHelper.getInstance(context)
        helper.setBackgroundResourceByAttr(custom_id_app_background, R.attr.custom_attr_app_bg)
        helper.setBackgroundResourceByAttr(custom_id_title_status_bar, R.attr.custom_attr_app_title_layout_bg)
        helper.setBackgroundResourceByAttr(custom_id_title_layout, R.attr.custom_attr_app_title_layout_bg)

        helper.setBackgroundResourceByAttr(theme_btn_turn_day, R.attr.custom_attr_btn_bg)
        helper.setTextColorByAttr(theme_btn_turn_day, R.attr.custom_attr_btn_text_color)
        helper.setBackgroundResourceByAttr(theme_btn_turn_night, R.attr.custom_attr_btn_bg)
        helper.setTextColorByAttr(theme_btn_turn_night, R.attr.custom_attr_btn_text_color)

        helper.setAlphaByAttr(theme_user_photo, R.attr.custom_attr_user_photo_alpha)

        helper.setTextColorByAttr(theme_nickname, R.attr.custom_attr_nickname_text_color)
        helper.setTextColorByAttr(theme_remark, R.attr.custom_attr_remark_text_color)

        theme_user_photoPlaceHolder = helper.getDrawableByAttr(R.attr.custom_attr_user_photo_place_holder)

        initBtnStatus() //
    }

    private inner class Custotheme_view_pagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

        override fun getItem(position: Int): Fragment {
            return MainFragment()
        }

        override fun getCount(): Int {
            return 3
        }
    }
}
