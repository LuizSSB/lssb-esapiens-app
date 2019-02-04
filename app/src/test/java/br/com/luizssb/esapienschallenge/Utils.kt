package br.com.luizssb.esapienschallenge

import android.arch.lifecycle.MutableLiveData
import br.com.luizssb.esapienschallenge.model.Person

fun <T> createLiveData(value: T): MutableLiveData<T> {
    val data = MutableLiveData<T>()
    data.postValue(value)
    return data
}

fun randomString() = Math.random().toString()

fun createStringLiveData(value: String = randomString()) = createLiveData(value)

fun randomPerson() = Person(
    username = randomString(),
    photoURL = randomString(),
    age = Math.random().toInt(),
    city = randomString(),
    gender = randomString(),
    sexualOrientation = randomString()
)