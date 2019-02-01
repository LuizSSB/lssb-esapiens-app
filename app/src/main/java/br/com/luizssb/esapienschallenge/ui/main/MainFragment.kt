package br.com.luizssb.esapienschallenge.ui.main

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import br.com.luizssb.esapienschallenge.R
import br.com.luizssb.esapienschallenge.dependencies.viewModel
import kotlinx.android.synthetic.main.main_fragment.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.support.closestKodein

class MainFragment : Fragment(), KodeinAware {
    override val kodein by closestKodein()
    private val viewModel: MainViewModel by viewModel()

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = inflater
            .inflate(R.layout.main_fragment, container, false)

        with(rootView) {
            viewModel.people
                .observe(this@MainFragment, Observer {
                    grid_people.adapter = PeopleAdapter(context, it)
                })

            viewModel.queryError
                .observe(this@MainFragment, Observer {
                    if (it !== null) {
                        // Luiz: given the limited scope of the REST API (there
                        // isn't even any kind of error response), if an error
                        // occurs, it must be either unreachable network or
                        // deserialization error. Since the latter is extremely
                        // unlikely, we just assume the network failed and
                        // call it a day.
                        Toast.makeText(context, getString(R.string.error_query), Toast.LENGTH_SHORT).show()
                    }
                })
        }


        return rootView
    }
}
