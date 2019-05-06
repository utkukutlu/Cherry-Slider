package com.utkukutlu.library.cherryslider

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide

class ViewPagerAdapter(context: Context?, imageList: List<CherrySliderModel>) : PagerAdapter() {

    private var imageList: List<CherrySliderModel>? = imageList
    private var layoutInflater: LayoutInflater? =
        context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?

    private var itemClickListener: ItemClickListener? = null

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun getCount(): Int {
        return imageList?.size ?: 0
    }

    override fun instantiateItem(container: ViewGroup, position: Int): View {

        val itemView = layoutInflater!!.inflate(R.layout.item_cherry_slider, container, false)

        val imageView = itemView.findViewById<ImageView>(R.id.image_view)
        val linearLayout = itemView.findViewById<LinearLayout>(R.id.linear_layout)
        val textView = itemView.findViewById<TextView>(R.id.text_view)

        if (imageList?.get(position)?.title != null) {
            linearLayout.visibility = View.VISIBLE
            textView.text = imageList?.get(position)?.title
        } else {
            linearLayout.visibility = View.INVISIBLE
        }

        if (imageList?.get(position)?.imageUrl == null) {
            val img = Glide.with(imageView.context).load(imageList?.get(position)?.imagePath)
            imageList?.get(position)?.opts?.let {
                img.apply(it)
            }
            img.into(imageView)
        } else {
            val img = Glide.with(imageView.context).load(imageList?.get(position)?.imageUrl)
            imageList?.get(position)?.opts?.let {
                img.apply(it)
            }
            img.into(imageView)
        }

        container.addView(itemView)

        imageView.setOnClickListener { itemClickListener?.onItemSelected(position) }

        return itemView
    }

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as RelativeLayout)
    }

}