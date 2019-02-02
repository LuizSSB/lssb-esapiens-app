package br.com.luizssb.esapienschallenge.service

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import br.com.luizssb.esapienschallenge.model.Person
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PersonService(private val restApi: ChallengeRestApi) {
    fun getPeople(): LiveData<ApiResponse<List<Person>>> {
        val people = MutableLiveData<ApiResponse<List<Person>>>()

        restApi.getPeople().enqueue(object : Callback<List<Person>> {
            override fun onResponse(
                call: Call<List<Person>>, response: Response<List<Person>>
            ) = people.postValue(
                ApiResponse(response.body() ?: emptyList(), null)
            )

            override fun onFailure(
                call: Call<List<Person>>, t: Throwable
            ) = people.postValue(ApiResponse(null, t))
        })

        return people
    }
}