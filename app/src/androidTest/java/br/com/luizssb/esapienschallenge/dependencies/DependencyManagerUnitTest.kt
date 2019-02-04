package br.com.luizssb.esapienschallenge.dependencies

import android.app.Application
import android.support.test.InstrumentationRegistry
import android.view.LayoutInflater
import br.com.luizssb.esapienschallenge.data.PersonDao
import br.com.luizssb.esapienschallenge.network.ConnectivityManagerProxy
import br.com.luizssb.esapienschallenge.repository.PersonRepository
import br.com.luizssb.esapienschallenge.service.PersonService
import org.junit.Assert.*
import org.junit.Test
import org.kodein.di.generic.instance

class DependencyManagerUnitTest {
    @Test
    fun loadDependencies_registeredAllStuff() {
        // arrange
        val context = InstrumentationRegistry.getTargetContext()
        val kodein = DependencyManager.loadDependencies(context)

        // act
        val personDao = kodein.instance<PersonDao>()
        val personService1 = kodein.instance<PersonService>()
        val personService2 = kodein.instance<PersonService>()
        val personRepo = kodein.instance<PersonRepository>()
        val cmProxy = kodein.instance<ConnectivityManagerProxy>()

        // assert
        assertEquals(personDao, kodein.instance<PersonDao>())
        assertNotEquals(personService1, personService2)
        assertEquals(personRepo, kodein.instance<PersonRepository>())
        assertEquals(cmProxy, kodein.instance<ConnectivityManagerProxy>())

        if (context is Application) {
            assertNotNull(kodein.instance<LayoutInflater>())
        }
    }

}