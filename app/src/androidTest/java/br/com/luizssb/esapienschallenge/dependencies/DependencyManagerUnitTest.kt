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
import org.kodein.di.direct
import org.kodein.di.generic.instance

class DependencyManagerUnitTest {
    @Test
    fun loadDependencies_registeredAllStuff() {
        // arrange
        val context = InstrumentationRegistry.getTargetContext()
        val kodein = DependencyManager.loadDependencies(context)

        // act
        val personDao = kodein.direct.instance<PersonDao>()
        val personService1 = kodein.direct.instance<PersonService>()
        val personService2 = kodein.direct.instance<PersonService>()
        val personRepo = kodein.direct.instance<PersonRepository>()
        val cmProxy = kodein.direct.instance<ConnectivityManagerProxy>()

        // assert
        assertEquals(personDao, kodein.direct.instance<PersonDao>())
        assertNotEquals(personService1, personService2)
        assertEquals(personRepo, kodein.direct.instance<PersonRepository>())
        assertEquals(cmProxy, kodein.direct.instance<ConnectivityManagerProxy>())

        if (context is Application) {
            assertNotNull(kodein.direct.instance<LayoutInflater>())
        }
    }

}