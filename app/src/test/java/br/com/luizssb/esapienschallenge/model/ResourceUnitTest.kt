package br.com.luizssb.esapienschallenge.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ResourceUnitTest {
    @Test
    fun success_hasData_hasNoError() {
        // arrange
        val data = "foo"
        val resource = Resource.success(data)

        // act

        // assert
        assertEquals(resource.data, data)
        assertNull(resource.error)
        assertEquals(resource.status, Status.SUCCESS)

    }

    @Test
    fun error_hasData_hasError() {
        // arrange
        val error = Exception("foo")
        val data = "bar"
        val resource = Resource.error(error, data)

        // act

        // assert
        assertEquals(resource.error, error)
        assertEquals(resource.data, data)
        assertEquals(resource.status, Status.ERROR)
    }

    @Test
    fun loading_hasData_hasNoError() {
        // arrange
        val data = "bar"
        val resource = Resource.loading(data)

        // act

        // assert
        assertNull(resource.error)
        assertEquals(resource.data, data)
        assertEquals(resource.status, Status.LOADING)
    }
}
