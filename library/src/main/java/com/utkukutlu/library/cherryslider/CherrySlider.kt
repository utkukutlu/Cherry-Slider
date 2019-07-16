package com.utkukutlu.library.cherryslider

import android.content.Context
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import java.util.*

class CherrySlider @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    RelativeLayout(context, attrs, defStyleAttr) {

    private var viewPager: ViewPager? = null
    private var pagerDots: LinearLayout? = null
    private var viewPagerAdapter: ViewPagerAdapter? = ViewPagerAdapter(context)


    private var dots: ArrayList<ImageView>? = null

    private var currentPage = 0
    private var imageCount = 0

    private var period: Int = 0
    private var duration: Int = 0
    var autoSlide = false
        set(value) {
            if (value) {
                slide()
            } else {
                slideTimer?.cancel()
            }
            field = value
        }

    private var selectedDot = 0
    private var unselectedDot = 0

    private var showIndicator = true

    private var slideTimer: Timer? = null

    init {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_cherry_slider, this, true)
        viewPager = findViewById(R.id.view_pager)
        pagerDots = findViewById(R.id.pager_dots)

        val typedArray =
            context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.CherrySlider,
                defStyleAttr,
                defStyleAttr
            )

        period = typedArray.getInt(R.styleable.CherrySlider_period, 0)

        duration = typedArray.getInt(R.styleable.CherrySlider_duration, 1000)

        autoSlide = typedArray.getBoolean(R.styleable.CherrySlider_auto_slide, false)
        selectedDot =
            typedArray.getResourceId(
                R.styleable.CherrySlider_selected_dot,
                R.drawable.shape_default_selected_dot
            )
        unselectedDot =
            typedArray.getResourceId(
                R.styleable.CherrySlider_unselected_dot,
                R.drawable.shape_default_unselected_dot
            )

        showIndicator = typedArray.getBoolean(R.styleable.CherrySlider_indicator, true)

        viewPager?.adapter = viewPagerAdapter

    }

    fun setImages(imageList: List<CherrySliderModel>) {
        slideTimer?.cancel()
        viewPagerAdapter?.replaceData(imageList)
        imageCount = imageList.size
        setDots(imageList.size)
        if (autoSlide) {
            slide()
        }
    }

    private fun setDots(size: Int) {
        if (showIndicator) {
            pagerDots?.visibility = View.VISIBLE
        } else {
            pagerDots?.visibility = View.INVISIBLE
        }

        pagerDots?.removeAllViews()
        dots = arrayListOf()

        for (i in 0 until size) {
            dots?.add(ImageView(context))
            dots?.get(i)?.setImageDrawable(ContextCompat.getDrawable(context, unselectedDot))
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 0, 8, 0)
            pagerDots?.addView(dots?.get(i), params)
        }
        dots?.get(0)?.setImageDrawable(ContextCompat.getDrawable(context, selectedDot))


        viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
//                currentPage = position
                for (dot in dots ?: arrayListOf()) {
                    dot.setImageDrawable(ContextCompat.getDrawable(context, unselectedDot))
                }
                dots?.get(position)
                    ?.setImageDrawable(ContextCompat.getDrawable(context, selectedDot))
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private fun slide() {
        if (imageCount > 0) {
            val handler = Handler()
            val update = Runnable {
                if (currentPage == imageCount) {
                    currentPage = 0
                }
                viewPager?.setCurrentItem(currentPage++, true)
            }
            slideTimer = Timer()
            slideTimer?.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    handler.post(update)
                }
            }, 0, 5000)
        }
    }

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        viewPagerAdapter?.setItemClickListener(itemClickListener)
    }

    fun setItemClickListener(itemClickListener: (position: Int) -> Unit) {
        viewPagerAdapter?.setItemClickListener(object : ItemClickListener {
            override fun onItemSelected(position: Int) {
                itemClickListener.invoke(position)
            }
        })
    }


    enum class Scale {
        FIT_CENTER,
        CENTER_INSIDE,
        CENTER_CROP
    }

}