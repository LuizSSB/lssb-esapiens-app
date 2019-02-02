package br.com.luizssb.esapienschallenge.ui.extension

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager

fun AppCompatActivity.setStatusBarColor(color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = color
    }
}