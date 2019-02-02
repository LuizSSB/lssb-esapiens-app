package br.com.luizssb.esapienschallenge.ui.profile

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.luizssb.esapienschallenge.R
import br.com.luizssb.esapienschallenge.dependencies.viewModel
import br.com.luizssb.esapienschallenge.model.Person
import br.com.luizssb.esapienschallenge.ui.extension.loadRemoteImage
import kotlinx.android.synthetic.main.profile_fragment.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.support.closestKodein

class ProfileFragment : Fragment(), KodeinAware {
    override val kodein by closestKodein()

    // Luiz: due to the fact that fragments cannot (or, at least, should  not)
    // have constructor parameters, we can't use the inline 'viewModel()' as
    // originally intended, hence this little bit.
    // The intention here was to not let the fragment grab a hold of the Person
    // object at any point. This way, if a fragment instance wants to get the
    // Person, it must go through the view model.
    private lateinit var acquireViewModel: Lazy<ProfileViewModel>
    private val viewModel get() = acquireViewModel.value

    companion object {
        fun newInstance(forPerson: Person): ProfileFragment {
            val fragment = ProfileFragment()
            fragment.acquireViewModel = fragment.viewModel(forPerson)
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(
        R.layout.profile_fragment, container, false
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.person.observe(this, Observer {
            label_username.text = it?.username.toString()
            label_age.text = it?.age.toString()
            label_city.text = it?.city.toString()
            label_gender.text = it?.gender.toString()
            label_sexuality.text = it?.sexualOrientation.toString()
            image_profile.loadRemoteImage(context!!, it?.photoURL)
        })
    }
}
