package br.com.luizssb.esapienschallenge.repository

import android.arch.lifecycle.LiveData
import br.com.luizssb.esapienschallenge.LiveDataUnitTest
import br.com.luizssb.esapienschallenge.createLiveData
import br.com.luizssb.esapienschallenge.data.PersonDao
import br.com.luizssb.esapienschallenge.model.Person
import br.com.luizssb.esapienschallenge.randomPerson
import br.com.luizssb.esapienschallenge.service.MockSuccessfulChallengeRestApi
import br.com.luizssb.esapienschallenge.service.PersonService
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Call

class PersonRepositoryUnitTest : LiveDataUnitTest() {
    enum class Action {
        REQUEST, LOAD, SAVE, CLEAN
    }

    @Test
    fun getPeople_rightOrder_emptyDbThenRequest() {
        // arrange
        val actions = ArrayList<Action>()
        val ogPeople = listOf<Person>()
        var dbPeople = ogPeople
        val apiPeople = listOf(randomPerson())
        val peopleResult = ArrayList<List<Person>>()
        val api = object : MockSuccessfulChallengeRestApi(apiPeople) {
            override fun getPeople(): Call<List<Person>> {
                actions.add(Action.REQUEST)
                return super.getPeople()
            }
        }
        val dao = object : PersonDao {
            override fun getPeople(): LiveData<List<Person>> {
                actions.add(Action.LOAD)
                return createLiveData(dbPeople)
            }

            override fun savePeople(users: List<Person>) {
                dbPeople = apiPeople
                actions.add(Action.SAVE)
            }

            override fun cleanRecords() {
                actions.add(Action.CLEAN)
            }
        }
        val repo = PersonRepository(PersonService(api), dao)

        // act
        repo.loadPeople().observeForever {
            if (it!!.data != null) {
                peopleResult.add(it.data!!)
                if (peopleResult.size == 2) {
                    defaultUnlock()
                }
            }
        }
        defaultLock()

        // assert
        assertEquals(
            actions,
            listOf(Action.LOAD, Action.REQUEST, Action.CLEAN, Action.SAVE, Action.LOAD)
        )
        assertEquals(peopleResult, listOf(ogPeople, apiPeople))
    }
}