package br.com.luizssb.esapienschallenge.ui.main

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.luizssb.esapienschallenge.R
import br.com.luizssb.esapienschallenge.dependencies.viewModel
import kotlinx.android.synthetic.main.main_fragment.*
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
    ): View = inflater
            .inflate(R.layout.main_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        refresh_control.isRefreshing = true
        refresh_control.setOnRefreshListener {
            refresh_control.isRefreshing = true
            viewModel.refresh()
        }

        viewModel.people
            .observe(this@MainFragment, Observer {
                grid_people.adapter = PeopleAdapter(context!!, it)
                finishLoading()
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
                    finishLoading(getString(R.string.error_query))
                }
            })
    }

    private fun finishLoading(errorMsg: String? = null) {
        refresh_control.isRefreshing = false
        label_error.text = errorMsg ?: ""
        container_error.visibility =
            if (errorMsg === null) View.GONE else View.VISIBLE
    }
}
