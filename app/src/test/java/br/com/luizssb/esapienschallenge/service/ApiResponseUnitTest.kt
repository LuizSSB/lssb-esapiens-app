package br.com.luizssb.esapienschallenge.service

import org.junit.Assert.*
import org.junit.Test

class ApiResponseUnitTest {
    @Test
    fun suceeded_hasData_noError() {
        // arrange
        val data = "foo"
        val response = ApiResponse(data, null)

        // act

        // assert
        assertTrue(response.succeeded)
        assertNull(response.error)
        assertEquals(response.data, data)
    }

    @Test
    fun suceeded_noData_hasError() {
        // arrange
        val ex = Exception("foo")
        val response = ApiResponse(null, ex)

        // act

        // assert
        assertFalse(response.succeeded)
        assertEquals(response.error, ex)
        assertNull(response.data)
    }

    @Test
    fun suceeded_hasData_hasError() {
        // arrange
        val data = "bar"
        val ex = Exception("foo")
        val response = ApiResponse(data, ex)

        // act

        // assert
        assertFalse(response.succeeded)
        assertEquals(response.error, ex)
        assertEquals(response.data, data)
    }

    @Test
    fun suceeded_noData_noError() {
        // arrange
        val response = ApiResponse(null, null)

        // act

        // assert
        assertTrue(response.succeeded)
        assertNull(response.error)
        assertNull(response.data)
    }
}