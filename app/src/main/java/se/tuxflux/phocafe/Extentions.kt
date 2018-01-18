package se.tuxflux.phocafe

import android.content.Context
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.squareup.picasso.Picasso
import java.util.concurrent.TimeUnit

fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun ImageView.loadUrl(url: String) {
}

fun View.disable(time: Long, unit: TimeUnit) {
    this.isEnabled = false
    this.postDelayed({ this.isEnabled = true }, unit.toMillis(time))
}

fun View.view(time: Long, unit: TimeUnit) {
    this.visibility = View.VISIBLE
    this.postDelayed({ this.visibility = View.GONE }, unit.toMillis(time))
}

fun View.hide(time: Long, unit: TimeUnit) {
    this.visibility = View.INVISIBLE
    this.postDelayed({ this.visibility = View.VISIBLE }, unit.toMillis(time))
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun Context.toast(message: CharSequence) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()