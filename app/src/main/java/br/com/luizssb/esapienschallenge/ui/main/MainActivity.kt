package br.com.luizssb.esapienschallenge.ui.main

import android.os.Bundle
import br.com.luizssb.esapienschallenge.R
import br.com.luizssb.esapienschallenge.ui.BaseAppActivity
import kotlinx.android.synthetic.main.profile_activity.*

class MainActivity : BaseAppActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.title_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }

}
