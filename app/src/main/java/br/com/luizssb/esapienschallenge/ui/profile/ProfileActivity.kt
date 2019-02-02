package br.com.luizssb.esapienschallenge.ui.profile

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import br.com.luizssb.esapienschallenge.R
import br.com.luizssb.esapienschallenge.model.Person

class ProfileActivity : AppCompatActivity() {
    companion object {
        const val KEY_PERSON =
            "br.com.luizssb.esapienschallenge.profileactivity.person"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_activity)
        if (savedInstanceState == null) {
            val person = intent.getSerializableExtra(KEY_PERSON) as Person
            val fragment = ProfileFragment.newInstance(person)
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commitNow()
        }
    }

}
