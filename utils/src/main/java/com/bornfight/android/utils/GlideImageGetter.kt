package com.bornfight.android.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.Request
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.ViewTarget
import com.bumptech.glide.request.transition.Transition
import java.util.*

class GlideImageGetter(private val mContext: Context, private val mTextView: TextView) : Html.ImageGetter, Drawable.Callback {

    private val mTargets: MutableSet<ImageGetterViewTarget>

    init {

        clear()
        mTargets = HashSet()
        mTextView.setTag(R.id.drawable_callback_tag, this)
    }

    fun clear() {
        val prev = get(mTextView) ?: return

        for (target in prev.mTargets) {
            Glide.with(mContext).clear(target)
        }
    }

    override fun getDrawable(url: String): Drawable {
        val urlDrawable = UrlDrawable()


        Log.d("GlideImageGetter", "Downloading from: $url")
        Glide.with(mContext)
            .load(url)
            .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
            .into(ImageGetterViewTarget(mTextView, urlDrawable))


        return urlDrawable

    }

    override fun invalidateDrawable(who: Drawable) {
        mTextView.invalidate()
    }

    override fun scheduleDrawable(who: Drawable, what: Runnable, whenScheduled: Long) {

    }

    override fun unscheduleDrawable(who: Drawable, what: Runnable) {

    }

    private inner class ImageGetterViewTarget internal constructor(view: TextView, private val mDrawable: UrlDrawable) : ViewTarget<TextView, Drawable>(view) {
        private var request: Request? = null

        init {
            mTargets.add(this)
        }

        override fun onResourceReady(resource: Drawable, glideAnimation: Transition<in Drawable>?) {
            val rect: Rect
            if (resource.intrinsicWidth > 100) {
                val width: Float
                val height: Float
                println("Image width is " + resource.intrinsicWidth)
                println("View width is " + view.width)
                if (resource.intrinsicWidth >= getView().width) {
                    val downScale = resource.intrinsicWidth.toFloat() / getView().width
                    width = resource.intrinsicWidth.toFloat() / downScale
                    height = resource.intrinsicHeight.toFloat() / downScale
                } else {
                    val multiplier = getView().width.toFloat() / resource.intrinsicWidth
                    width = resource.intrinsicWidth.toFloat() * multiplier
                    height = resource.intrinsicHeight.toFloat() * multiplier
                }
                println("New Image width is $width")


                rect = Rect(0, 0, Math.round(width), Math.round(height))
            } else {
                rect = Rect(0, 0, resource.intrinsicWidth * 2, resource.intrinsicHeight * 2)
            }
            resource.bounds = rect

            mDrawable.bounds = rect
            mDrawable.drawable = resource


            getView().text = getView().text
            getView().invalidate()
        }

        override fun getRequest(): Request? {
            return request
        }

        override fun setRequest(request: Request?) {
            this.request = request
        }
    }

    inner class UrlDrawable : BitmapDrawable() {
        // the drawable that you need to set, you could set the initial drawing
        // with the loading image if you need to
        internal var drawable: Drawable? = null

        override fun draw(canvas: Canvas) {
            // override the draw to facilitate refresh function later
            if (drawable != null) {
                drawable?.draw(canvas)
            }
        }
    }

    companion object {

        operator fun get(view: View): GlideImageGetter? {
            return view.getTag(R.id.drawable_callback_tag) as GlideImageGetter
        }
    }
}