package br.com.luizssb.esapienschallenge.repository

import android.arch.lifecycle.LiveData
import br.com.luizssb.esapienschallenge.LiveDataUnitTest
import br.com.luizssb.esapienschallenge.createLiveData
import br.com.luizssb.esapienschallenge.createRandomPerson
import br.com.luizssb.esapienschallenge.data.PersonDao
import br.com.luizssb.esapienschallenge.model.Person
import br.com.luizssb.esapienschallenge.model.Status
import br.com.luizssb.esapienschallenge.service.MockFailedChallengeRestApi
import br.com.luizssb.esapienschallenge.service.MockSuccessfulChallengeRestApi
import br.com.luizssb.esapienschallenge.service.PersonService
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import retrofit2.Call

class PersonRepositoryImplUnitTest : LiveDataUnitTest() {
    enum class Action {
        REQUEST, LOAD, SAVE, CLEAN
    }

    @Test
    fun getPeople_rightOrder_emptyDbThenFailedRequest() {
        // arrange
        val actions = ArrayList<Action>()
        val dbPeople = listOf<Person>()
        val result = ArrayList<Any>()
        val api = object : MockFailedChallengeRestApi() {
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
                fail("not supposed to happen on request failure")
            }

            override fun cleanRecords() {
                fail("not supposed to happen on request failure")
            }
        }
        val repo = PersonRepositoryImpl(PersonService(api), dao)

        // act
        repo.loadPeople().observeForever {
            if (it!!.status != Status.LOADING) {
                result.add(it.data!!)
                result.add(it.error!!)
                defaultUnlock()
            }
        }
        defaultLock()

        // assert
        assertEquals(actions, listOf(Action.LOAD, Action.REQUEST))
        assertEquals(result, listOf(dbPeople, api.error))
    }

    @Test
    fun getPeople_rightOrder_emptyDbThenRequestThenRefresh() {
        // arrange
        val actions = ArrayList<Action>()
        var dbPeople = listOf<Person>()
        val apiPeople1 = listOf(createRandomPerson())
        val apiPeople2 = listOf(createRandomPerson())
        val peopleResult = ArrayList<List<Person>>()
        val api = object : MockSuccessfulChallengeRestApi(apiPeople1) {
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
                if (dbPeople == apiPeople1) {
                    dbPeople = apiPeople2
                    assertEquals(users, apiPeople2)
                } else {
                    dbPeople = apiPeople1
                    assertEquals(users, apiPeople1)

                }
                actions.add(Action.SAVE)
            }

            override fun cleanRecords() {
                actions.add(Action.CLEAN)
            }
        }
        val repo = PersonRepositoryImpl(PersonService(api), dao)

        // act
        repo.loadPeople().observeForever {
            if (it!!.status == Status.SUCCESS) {
                peopleResult.add(it.data!!)
                if (peopleResult.size == 2 || peopleResult.size == 4) {
                    defaultUnlock()
                }
            }
        }
        defaultLock()
        api.people = apiPeople2
        repo.refresh()
        defaultLock()

        // assert
        assertEquals(
            actions,
            listOf(
                // initialization
                Action.LOAD, Action.REQUEST, Action.CLEAN, Action.SAVE, Action.LOAD,

                // refresh
                Action.LOAD, Action.REQUEST, Action.CLEAN, Action.SAVE, Action.LOAD
            )
        )
        assertEquals(peopleResult, listOf(apiPeople1, apiPeople2))
    }

    @Test
    fun getPeople_rightOrder_nonEmptyDb() {
        // arrange
        val actions = ArrayList<Action>()
        val dbPeople = listOf(createRandomPerson())
        val peopleResult = ArrayList<List<Person>>()
        val api = object : MockSuccessfulChallengeRestApi(listOf(createRandomPerson())) {
            override fun getPeople(): Call<List<Person>> {
                fail("Not supposed to happen when non empty db")
                return super.getPeople()
            }
        }
        val dao = object : PersonDao {
            override fun getPeople(): LiveData<List<Person>> {
                actions.add(Action.LOAD)
                return createLiveData(dbPeople)
            }

            override fun savePeople(users: List<Person>) {
                fail("Not supposed to happen when non empty db")
            }

            override fun cleanRecords() {
                fail("Not supposed to happen when non empty db")
            }
        }
        val repo = PersonRepositoryImpl(PersonService(api), dao)

        // act
        repo.loadPeople().observeForever {
            if (it!!.status == Status.SUCCESS) {
                peopleResult.add(dbPeople)
                defaultUnlock()
            }
        }
        defaultLock()

        // assert
        assertEquals(actions, listOf(Action.LOAD))
        assertEquals(peopleResult, listOf(dbPeople))
    }
}