package br.com.luizssb.esapienschallenge.service

import br.com.luizssb.esapienschallenge.LiveDataUnitTest
import br.com.luizssb.esapienschallenge.model.Person
import org.junit.Assert.*
import org.junit.Test

class PersonServiceUnitTest : LiveDataUnitTest() {
    @Test
    fun getPeople_success() {
        // arrange
        val people = listOf(
            Person(
                "foo", "bar", 2, "zig", "zag", "zurg"
            )
        )
        var response: ApiResponse<List<Person>>? = null
        val restApi = MockSuccessfulChallengeRestApi(people)
        val service = PersonService(restApi)

        // act
        val liveData = service.getPeople()
        liveData.observeForever {
            response = it
            defaultUnlock()
        }
        defaultLock()

        // assert
        assertNotNull(response)
        assertEquals(response!!.data, people)
        assertEquals(response!!.error, null)
        assertTrue(response!!.succeeded)
    }

    @Test
    fun getPeople_error() {
        // arrange
        val error = Exception("dude")
        var response: ApiResponse<List<Person>>? = null
        val restApi = MockFailedChallengeRestApi(error)
        val service = PersonService(restApi)

        // act
        val liveData = service.getPeople()
        liveData.observeForever {
            response = it
            defaultUnlock()
        }
        defaultLock()

        // assert
        assertNotNull(response)
        assertEquals(response!!.data, null)
        assertEquals(response!!.error, error)
        assertFalse(response!!.succeeded)
    }

    @Test
    fun getPeople_success_null() {
        // arrange
        var response: ApiResponse<List<Person>>? = null
        val restApi = MockSuccessfulChallengeRestApi(null)
        val service = PersonService(restApi)

        // act
        val liveData = service.getPeople()
        liveData.observeForever {
            response = it
            defaultUnlock()
        }
        defaultLock()

        // assert
        assertNotNull(response)
        assertEquals(response!!.data, emptyList<Person>())
        assertEquals(response!!.error, null)
        assertTrue(response!!.succeeded)
    }
}