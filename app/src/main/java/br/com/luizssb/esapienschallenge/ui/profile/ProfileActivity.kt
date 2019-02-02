package br.com.luizssb.esapienschallenge.ui.profile

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.MenuItem
import br.com.luizssb.esapienschallenge.R
import br.com.luizssb.esapienschallenge.model.Person
import br.com.luizssb.esapienschallenge.ui.BaseAppActivity
import br.com.luizssb.esapienschallenge.ui.profile.ProfileFragment.Companion.KEY_PERSON
import kotlinx.android.synthetic.main.profile_activity.*

class ProfileActivity : BaseAppActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_activity)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            val person = intent.getSerializableExtra(KEY_PERSON) as Person
            val fragment = ProfileFragment.newInstance(person)
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commitNow()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            die()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        die()
    }

    // some would disagree
    private fun die() {
        finish()
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
    }

    override val statusBarColor: Int
        get() = ContextCompat.getColor(this, R.color.background_status_profile);
}
