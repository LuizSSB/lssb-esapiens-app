package br.com.luizssb.esapienschallenge.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import br.com.luizssb.esapienschallenge.data.PersonDao
import br.com.luizssb.esapienschallenge.model.Person
import br.com.luizssb.esapienschallenge.service.ChallengeService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PersonRepository(
    private val service: ChallengeService, private val personDao: PersonDao
) {
    private val people = MutableLiveData<List<Person>>()
    private val error = MutableLiveData<Throwable>()

    fun getPeople(): Pair<LiveData<List<Person>>, LiveData<Throwable>> {
        GlobalScope.launch {
            val dbPeople = personDao.getPeople()

            if (dbPeople.isNotEmpty()) {
                people.postValue(dbPeople)
                error.postValue(null)
            } else {
                service.getPeople().enqueue(object : Callback<List<Person>> {
                    override fun onResponse(
                        call: Call<List<Person>>, response: Response<List<Person>>
                    ) {
                        GlobalScope.launch {
                            val apiPeople = response.body()
                            if(apiPeople != null) {
                                personDao.savePeople(apiPeople)
                            }
                            people.postValue(apiPeople)
                        }
                    }

                    override fun onFailure(
                        call: Call<List<Person>>, t: Throwable
                    ) = error.postValue(t)
                })
            }
        }

        return Pair(people, error)
    }

    fun refresh() {
        GlobalScope.launch {
            personDao.cleanRecords()
            getPeople()
        }
    }
}