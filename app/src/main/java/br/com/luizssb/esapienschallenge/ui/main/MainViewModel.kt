package br.com.luizssb.esapienschallenge.ui.main

import android.arch.lifecycle.LiveData
import br.com.luizssb.esapienschallenge.model.Person
import br.com.luizssb.esapienschallenge.repository.PersonRepository
import br.com.luizssb.esapienschallenge.ui.InjectionDependentViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

class MainViewModel(kodein: Kodein) : InjectionDependentViewModel(kodein) {
    private val repository: PersonRepository by kodein.instance()

    val people: LiveData<List<Person>>
    val queryError: LiveData<Throwable>

    init {
        val result = repository.getPeople()
        people = result.first
        queryError = result.second
    }

    fun refresh() {
        repository.refresh()
    }
}
