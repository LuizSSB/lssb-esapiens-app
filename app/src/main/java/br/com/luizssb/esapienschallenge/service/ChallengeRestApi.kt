package br.com.luizssb.esapienschallenge.service

import br.com.luizssb.esapienschallenge.model.Person
import retrofit2.Call
import retrofit2.http.GET

interface ChallengeRestApi {
    @GET("/mobiletest.json/")
    fun getPeople(): Call<List<Person>>
}