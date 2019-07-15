package com.mario.day_night_mode_20170302.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView

import com.mario.day_night_mode_20170302.MyApplication
import com.mario.day_night_mode_20170302.R
import com.mario.day_night_mode_20170302.helpers.MarioResourceHelper
import kotlinx.android.synthetic.main.activity_recycler.*


/**
 * Created by Mario on 2017-03-05.
 */

class RecyclerActivity : BaseActivity() {

    private var mAdapter: CustomAdapter? = null
    private var thumb_margin_left_day = 0
    private var thumb_margin_left_night = 0

    private val tag_is_animation = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAllViews()
        initAllDatum()
    }

    override fun initAllViews() {
        setContentView(R.layout.activity_recycler)
    }

    override fun initAllDatum() {
        list_recycler_view.setLayoutManager(LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false))
        list_recycler_view.setHasFixedSize(true)
        mAdapter = CustomAdapter()
        list_recycler_view.setAdapter(mAdapter)

        thumb_margin_left_day = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, resources.displayMetrics).toInt()
        thumb_margin_left_night = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 28f, resources.displayMetrics).toInt()
    }

    override fun notifyByThemeChanged() {
        val helper = MarioResourceHelper.getInstance(context)

        helper.setBackgroundResourceByAttr(custom_id_app_background, R.attr.custom_attr_app_bg)
        helper.setBackgroundResourceByAttr(custom_id_title_status_bar, R.attr.custom_attr_app_title_layout_bg)
        helper.setBackgroundResourceByAttr(custom_id_title_layout, R.attr.custom_attr_app_title_layout_bg)

        helper.setTextColorByAttr(list_main_title, R.attr.custom_attr_main_title_color)
        helper.setBackgroundResourceByAttr(list_checkbox_bg, R.attr.custom_attr_check_box_bg)
        helper.setBackgroundResourceByAttr(list_checkbox_thumb, R.attr.custom_attr_check_box_thumb_bg)

        if (mAdapter != null) mAdapter!!.notifyByThemeChanged()

        list_checkbox_bg.setOnClickListener{
            doCheckboxAnimation()
        }
    }

    private fun doCheckboxAnimation() {
        val animatorSet = AnimatorSet()
        animatorSet.duration = 240
        animatorSet.addListener(object : AnimatorListenerAdapter() {

            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                if (tag_is_animation) {
                    val intent = Intent(context, AnimatorActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                } else {
                    (application as MyApplication).notifyByThemeChanged() // 发送主题变更通知，让每一个注册监听事件的Activity主动更新UI.
                }
            }
        })

        animatorSet.play(obtainCheckboxAnimator())

        animatorSet.start()
    }

    private fun obtainCheckboxAnimator(): Animator {
        val start = if (themeTag == -1) thumb_margin_left_night else thumb_margin_left_day
        val end = if (themeTag == -1) thumb_margin_left_day else thumb_margin_left_night
        val animator = ValueAnimator.ofInt(start, end)
        animator.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            val layoutParams = list_checkbox_thumb.getLayoutParams() as RelativeLayout.LayoutParams
            layoutParams.leftMargin = value
            list_checkbox_thumb.setLayoutParams(layoutParams)
        }
        return animator
    }

    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var item_title: TextView
        var item_description: TextView
        var item_div_top: View
        var item_div_bottom: View
        var item_arrow: ImageView


        init {
            item_title = itemView.findViewById(R.id.item_title)
            item_description = itemView.findViewById(R.id.item_description)
            item_div_top = itemView.findViewById(R.id.item_div_top)
            item_div_bottom = itemView.findViewById(R.id.item_div_bottom)
            item_arrow = itemView.findViewById(R.id.item_arrow)
        }
    }

    private inner class CustomAdapter : RecyclerView.Adapter<CustomViewHolder>() {

        private var item_background_color = Color.parseColor("#00000000")
        private var item_arrow_tint_color = Color.parseColor("#00000000")
        private var item_main_text_color = Color.parseColor("#00000000")
        private var item_sub_text_color = Color.parseColor("#00000000")
        private var item_div_color = Color.parseColor("#00000000")

        init {
            initAllThemeAttrs()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
            return CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.include_layout_item, null))
        }

        override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
            holder.item_div_top.visibility = if (position == 0) View.VISIBLE else View.GONE
            val originLayoutParams = holder.item_div_bottom.layoutParams
            if (originLayoutParams != null) {
                val marginLayoutParams = originLayoutParams as LinearLayout.LayoutParams
                marginLayoutParams!!.leftMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (if (position == itemCount - 1) 0 else 12).toFloat(), context.resources.displayMetrics).toInt()
                holder.item_div_bottom.layoutParams = marginLayoutParams
            }

            holder.itemView.setBackgroundColor(item_background_color)
            holder.item_description.setTextColor(item_sub_text_color)
            holder.item_div_bottom.setBackgroundColor(item_div_color)
            holder.item_div_top.setBackgroundColor(item_div_color)
            holder.item_title.setTextColor(item_main_text_color)

            tintDrawable(holder.item_arrow, item_arrow_tint_color)
        }

        override fun getItemCount(): Int {
            return 36
        }

        private fun initAllThemeAttrs() {
            val helper = MarioResourceHelper.getInstance(context)
            item_background_color = helper.getColorByAttr(R.attr.custom_attr_app_content_layout_bg)
            item_arrow_tint_color = helper.getColorByAttr(R.attr.custom_attr_sub_text_color)
            item_main_text_color = helper.getColorByAttr(R.attr.custom_attr_main_text_color)
            item_sub_text_color = helper.getColorByAttr(R.attr.custom_attr_sub_text_color)
            item_div_color = helper.getColorByAttr(R.attr.custom_attr_app_content_layout_div_color)
        }

        fun notifyByThemeChanged() {
            initAllThemeAttrs()
            notifyDataSetChanged()
        }

        private fun tintDrawable(imageView: ImageView?, tintColor: Int) {
            if (imageView == null) return
            val originDrawable = imageView.drawable ?: return
            val tintDrawable = DrawableCompat.wrap(originDrawable.mutate()) ?: return
            DrawableCompat.setTint(tintDrawable, tintColor)
            if (tintDrawable == null) return
            imageView.setImageDrawable(tintDrawable)
        }
    }
}
