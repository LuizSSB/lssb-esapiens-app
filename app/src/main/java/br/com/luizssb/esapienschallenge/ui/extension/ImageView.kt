package br.com.luizssb.esapienschallenge.ui.extension

import android.content.Context
import android.widget.ImageView
import br.com.luizssb.esapienschallenge.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun ImageView.loadRemoteImage(context: Context, url: String?) {
    Glide
        .with(context)
        .load(url)
        .apply(
            RequestOptions()
                .placeholder(R.mipmap.ic_launcher_round)
                .centerCrop()
        )
        .into(this)
}