package br.com.luizssb.esapienschallenge.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import br.com.luizssb.esapienschallenge.model.Person
import br.com.luizssb.esapienschallenge.service.ChallengeService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChallengeRepository(private val service: ChallengeService) {
    private val people = MutableLiveData<List<Person>>()
    private val error = MutableLiveData<Throwable>()

    fun getPeople(): Pair<LiveData<List<Person>>, LiveData<Throwable>> {

        service.getPeople().enqueue(object : Callback<List<Person>> {
            override fun onResponse(
                call: Call<List<Person>>, response: Response<List<Person>>
            ) = people.postValue(response.body())

            override fun onFailure(call: Call<List<Person>>, t: Throwable) =
                error.postValue(t)
        })

        return Pair(people, error)
    }

    fun refresh() {
        getPeople()
    }
}