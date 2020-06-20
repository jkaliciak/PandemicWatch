package dev.jakal.pandemicwatch.presentation.common

import android.content.Context
import android.graphics.Color.MAGENTA
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import dev.jakal.pandemicwatch.R

fun ImageView.load(url: String, onLoadingFinished: () -> Unit = {}) {
    val requestOptions =
        RequestOptions.placeholderOf(R.drawable.ic_content_not_initiated_96).dontTransform()
    val listener = object : RequestListener<Drawable> {

        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            onLoadingFinished()
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            onLoadingFinished()
            return false
        }
    }
    Glide.with(this)
        .load(url)
        .apply(requestOptions)
        .listener(listener)
        .into(this)
}

@ColorInt
fun Context.getThemeColor(@AttrRes attrResId: Int): Int {
    val typedArray = obtainStyledAttributes(null, intArrayOf(attrResId))
    try {
        return typedArray.getColor(0, MAGENTA)
    } finally {
        typedArray.recycle()
    }
}