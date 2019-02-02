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
    ): View =
        inflater.inflate(R.layout.main_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        refresh_control.isRefreshing = true
        refresh_control.setOnRefreshListener(this::refresh)
        button_refresh.setOnClickListener { refresh() }

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

    private fun refresh() {
        refresh_control.isRefreshing = true
        progressbar.visibility = View.VISIBLE
        button_refresh.visibility = View.GONE
        viewModel.refresh()
    }

    private fun finishLoading(errorMsg: String? = null) {
        refresh_control.isRefreshing = false
        progressbar.visibility = View.GONE

        if (viewModel.people.value == null || viewModel.people.value.isNullOrEmpty()) {
            label_error.text = errorMsg ?: ""
            button_refresh.visibility = View.VISIBLE
            container_error.visibility = View.VISIBLE
        } else if (errorMsg == null) {
            container_error.visibility = View.GONE
        } else {
            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
        }
    }
}
