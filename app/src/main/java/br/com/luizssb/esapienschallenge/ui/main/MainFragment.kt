package br.com.luizssb.esapienschallenge.ui.main

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import br.com.luizssb.esapienschallenge.R
import br.com.luizssb.esapienschallenge.dependencies.viewModel
import br.com.luizssb.esapienschallenge.model.Person
import br.com.luizssb.esapienschallenge.model.Status
import br.com.luizssb.esapienschallenge.ui.profile.ProfileActivity
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

        refresh_control.setOnRefreshListener(this::refresh)
        button_refresh.setOnClickListener { refresh() }

        grid_people.onItemClickListener = AdapterView.OnItemClickListener { _, _, i, _ ->
            val adapter = grid_people.adapter as PeopleAdapter
            val intent = Intent(context, ProfileActivity::class.java)
            intent.putExtra(
                ProfileActivity.KEY_PERSON, adapter.getItem(i) as Person
            )
            startActivity(intent)
        }

        viewModel.peopleResource.observe(this, Observer {
            if (it == null) return@Observer

            when (it.status) {
                Status.SUCCESS -> {
                    grid_people.adapter = PeopleAdapter(context!!, it.data!!)
                    container_error.visibility = View.GONE
                    refresh_control.isRefreshing = false
                }
                Status.LOADING -> {
                    refresh_control.isRefreshing = true
                    progressbar.visibility = View.VISIBLE
                    button_refresh.visibility = View.GONE
                }
                Status.ERROR -> {
                    progressbar.visibility = View.GONE
                    refresh_control.isRefreshing = false

                    // Luiz: given the limited scope of the REST API (there
                    // isn't even any kind of error response), if an error
                    // occurs, it must be either unreachable network or
                    // deserialization error. Since the latter is extremely
                    // unlikely, we just show a network failure message and
                    // call it a day.
                    val msg = getString(R.string.error_query)

                    if (container_error.visibility == View.GONE) {
                        // Luiz: i.e. there's already something visible
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    } else {
                        button_refresh.visibility = View.VISIBLE
                        label_error.text = msg
                    }
                }
            }
        })
    }

    private fun refresh() = viewModel.refresh()
}
