package br.com.luizssb.esapienschallenge.repository

import android.arch.lifecycle.LiveData
import br.com.luizssb.esapienschallenge.data.PersonDao
import br.com.luizssb.esapienschallenge.model.Person
import br.com.luizssb.esapienschallenge.service.ApiResponse
import br.com.luizssb.esapienschallenge.service.PersonService


class PersonRepositoryImpl(
    private val personService: PersonService, private val personDao: PersonDao
) : PersonRepository {
    private val peopleResource by lazy {
        object : NetworkBoundResource<List<Person>, List<Person>>() {
            override fun saveCallResult(item: List<Person>) {
                personDao.cleanRecords()
                personDao.savePeople(item)
            }

            override fun shouldFetch(data: List<Person>?): Boolean =
                data == null || data.isEmpty()

            override fun loadFromDb(): LiveData<List<Person>> =
                personDao.getPeople()

            override fun createCall(): LiveData<ApiResponse<List<Person>>> =
                personService.getPeople()
        }
    }


    override fun loadPeople() = peopleResource.asLiveData

    override fun refresh() = peopleResource.loadUp(forceFromNetwork = true)
}