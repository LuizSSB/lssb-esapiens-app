package br.com.luizssb.esapienschallenge.repository

import android.arch.lifecycle.LiveData
import br.com.luizssb.esapienschallenge.data.PersonDao
import br.com.luizssb.esapienschallenge.model.Person
import br.com.luizssb.esapienschallenge.service.ApiResponse
import br.com.luizssb.esapienschallenge.service.PersonService

/**
 * Repository of people. If there's stuff in the database, acquires the data
 * from there; otherwise, queries the REST API and saves the result in the DB.
 *
 * This repository does not follow Google's Single Source of Truth principle, as
 * as it uses two sources of truth. The reason for this is that this way we have
 * a _little_ more control over what data is passed to clients and when it is
 * passed, both things which, at the moment, are actually more important to me,
 * given the simplicity of the project.
 *
 * @author Luiz
 */
class PersonRepository(
    private val personService: PersonService, private val personDao: PersonDao
) {
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


    fun loadPeople() = peopleResource.asLiveData()

    fun refresh() = peopleResource.loadUp(forceFromNetwork = true)
}