package com.mario.day_night_mode_20170302.fragments

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
import android.widget.TextView
import com.mario.day_night_mode_20170302.R

import com.mario.day_night_mode_20170302.activities.RecyclerActivity
import com.mario.day_night_mode_20170302.helpers.MarioResourceHelper
import kotlinx.android.synthetic.main.fragment_main.*


/**
 * Created by Mario on 2017-03-06.
 */

class MainFragment : BaseFragment() {

    private var mAdapter: CustomAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initAllDatum()
    }

    private fun initAllDatum() {
        fragment_main_recycler_view.setLayoutManager(LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false))
        fragment_main_recycler_view.setHasFixedSize(true)
        mAdapter = CustomAdapter()
        fragment_main_recycler_view.setAdapter(mAdapter)
    }

    override fun notifyByThemeChanged() {
        if (mAdapter != null) mAdapter!!.notifyByThemeChanged() //
    }

    /**
     */
    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var item_title: TextView? = null
        var item_description: TextView? = null
        var item_div_top: View? = null
        var item_div_bottom: View? = null
        var item_arrow: ImageView? = null

        init {
            item_title = itemView.findViewById(R.id.item_title)
            item_description = itemView.findViewById(R.id.item_description)
            item_div_top = itemView.findViewById(R.id.item_div_top)
            item_div_bottom = itemView.findViewById(R.id.item_div_bottom)
            item_arrow = itemView.findViewById(R.id.item_arrow)
        }
    }

    /**
     */
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
            holder.item_div_top!!.visibility = if (position == 0) View.VISIBLE else View.GONE
            val originLayoutParams = holder.item_div_bottom!!.layoutParams
            if (originLayoutParams != null) {
                val marginLayoutParams = originLayoutParams as LinearLayout.LayoutParams
                marginLayoutParams!!.leftMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (if (position == itemCount - 1) 0 else 12).toFloat(), context!!.resources.displayMetrics).toInt()
                holder.item_div_bottom!!.layoutParams = marginLayoutParams
            }

            holder.itemView.setBackgroundColor(item_background_color)
            holder.item_description!!.setTextColor(item_sub_text_color)
            holder.item_div_bottom!!.setBackgroundColor(item_div_color)
            holder.item_div_top!!.setBackgroundColor(item_div_color)
            holder.item_title!!.setTextColor(item_main_text_color)

            tintDrawable(holder.item_arrow, item_arrow_tint_color)

            holder.itemView.setOnClickListener(CustomItemClickListener())
        }

        override fun getItemCount(): Int {
            return 20
        }

        private fun initAllThemeAttrs() {
            val helper = MarioResourceHelper.getInstance(context!!)
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

        private inner class CustomItemClickListener : View.OnClickListener {

            override fun onClick(view: View) {
                val intent = Intent(context, RecyclerActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
