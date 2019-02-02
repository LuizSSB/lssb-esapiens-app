package br.com.luizssb.esapienschallenge.ui

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import br.com.luizssb.esapienschallenge.R
import br.com.luizssb.esapienschallenge.ui.extension.setStatusBarColor

abstract class BaseAppActivity : AppCompatActivity() {
    open val statusBarColor: Int
        get() = ContextCompat.getColor(this, R.color.colorPrimaryDark)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarColor(statusBarColor)
    }
}