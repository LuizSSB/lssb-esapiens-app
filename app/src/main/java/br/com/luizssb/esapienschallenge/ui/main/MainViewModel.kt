package br.com.luizssb.esapienschallenge.ui.main

import android.arch.lifecycle.LiveData
import br.com.luizssb.esapienschallenge.model.Person
import br.com.luizssb.esapienschallenge.network.ConnectivityManagerProxy
import br.com.luizssb.esapienschallenge.repository.PersonRepository
import br.com.luizssb.esapienschallenge.ui.InjectionDependentViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

class MainViewModel(kodein: Kodein) : InjectionDependentViewModel(kodein) {
    private val repository: PersonRepository by kodein.instance()
    private val connectivityManager: ConnectivityManagerProxy by kodein.instance()
    private var isConnected = true

    val people: LiveData<List<Person>>
    val queryError: LiveData<Throwable>

    init {
        val result = repository.getPeople()
        people = result.first
        queryError = result.second
        connectivityManager.registerNetworkStatusChangeCallback { connected ->
            // Luiz: just making sure that we don't unnecessarily refresh the
            // entries. Needed because if the device is connected to the web
            // when the app boots, this callback is immediately called, even
            // though, tecnically, the connectivity status didn't change.
            if (isConnected != connected) {
                if (connected) {
                    refresh()
                }

                isConnected = connected
            }
        }
    }

    fun refresh() {
        repository.refresh()
    }
}
