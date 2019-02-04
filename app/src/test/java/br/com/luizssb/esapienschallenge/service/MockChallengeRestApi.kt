package br.com.luizssb.esapienschallenge.service

import br.com.luizssb.esapienschallenge.model.Person
import retrofit2.Call
import retrofit2.mock.Calls

open class MockSuccessfulChallengeRestApi(var people: List<Person>?) : ChallengeRestApi {
    override fun getPeople(): Call<List<Person>> {
        return Calls.response(people)
    }
}

class MockFailedChallengeRestApi(var error: Throwable = Exception()) : ChallengeRestApi {
    override fun getPeople(): Call<List<Person>> {
        return Calls.failure(error)
    }
}