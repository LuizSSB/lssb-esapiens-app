package br.com.luizssb.esapienschallenge.ui.profile

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.luizssb.esapienschallenge.R
import br.com.luizssb.esapienschallenge.dependencies.viewModel
import br.com.luizssb.esapienschallenge.model.Person
import org.kodein.di.KodeinAware
import org.kodein.di.android.support.closestKodein

class ProfileFragment : Fragment(), KodeinAware {
    override val kodein by closestKodein()
    private lateinit var person: Person
    private val viewModel: ProfileViewModel by viewModel(person)

    companion object {
        fun newInstance(forPerson: Person): ProfileFragment {
            val fragment = ProfileFragment()
            fragment.person = forPerson
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.profile_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}
