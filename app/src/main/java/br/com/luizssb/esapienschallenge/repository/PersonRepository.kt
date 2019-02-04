package br.com.luizssb.esapienschallenge.repository

import android.arch.lifecycle.LiveData
import br.com.luizssb.esapienschallenge.model.Person
import br.com.luizssb.esapienschallenge.model.Resource

interface PersonRepository {
    fun loadPeople(): LiveData<Resource<List<Person>>>
    fun refresh()
}