package br.com.luizssb.esapienschallenge.ui.profile

import br.com.luizssb.esapienschallenge.LiveDataUnitTest
import br.com.luizssb.esapienschallenge.createRandomPerson
import br.com.luizssb.esapienschallenge.model.Person
import org.junit.Assert.assertSame
import org.junit.Test
import org.kodein.di.Kodein

class ProfileViewModelUnitTest : LiveDataUnitTest() {
    @Test
    fun constructor_receivesParameter() {
        // arrange
        val kodein = Kodein.lazy { }
        val person = createRandomPerson()
        val viewModel = ProfileViewModel(kodein, person)
        var result: Person? = null

        // act
        viewModel.person.observeForever {
            result = it
            defaultUnlock()
        }

        // assert
        assertSame(result, person)
    }
}