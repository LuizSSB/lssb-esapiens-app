package br.com.luizssb.esapienschallenge.ui.main

import android.arch.lifecycle.LiveData
import br.com.luizssb.esapienschallenge.LiveDataUnitTest
import br.com.luizssb.esapienschallenge.createLiveData
import br.com.luizssb.esapienschallenge.createRandomPerson
import br.com.luizssb.esapienschallenge.model.Person
import br.com.luizssb.esapienschallenge.model.Resource
import br.com.luizssb.esapienschallenge.network.ConnectivityManagerProxy
import br.com.luizssb.esapienschallenge.repository.PersonRepository
import org.junit.Assert.*
import org.junit.Test
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class MainViewModelUnitTest : LiveDataUnitTest() {
    @Test
    fun constructor_success_baseFlow() {
        // arrange
        var subscribed = false
        val resource1 = Resource.success(listOf(createRandomPerson()))
        val resource2 = Resource.success(listOf(createRandomPerson()))
        val peopleData = createLiveData(resource1)
        val kodein = Kodein.lazy {
            bind<ConnectivityManagerProxy>() with singleton {
                object : ConnectivityManagerProxy {
                    override fun registerNetworkStatusChangeCallback(connectionStatusChangeHandler: (connected: Boolean) -> Unit) {
                        if (subscribed) {
                            fail("not meant to subscribe twice")
                        }
                        subscribed = true
                    }

                    override fun unregisterNetworkStatusChangeCallback(connectionStatusChangeHandler: (connected: Boolean) -> Unit) {
                        fail("view model not meant to stop listening to network events")
                    }

                    override var isConnected: Boolean = false
                }
            }
            bind<PersonRepository>() with singleton {
                object : PersonRepository {
                    override fun loadPeople(): LiveData<Resource<List<Person>>> {
                        return peopleData
                    }

                    override fun refresh() {
                        peopleData.postValue(resource2)
                    }
                }
            }
        }
        val viewModel = MainViewModel(kodein)
        val results = ArrayList<Any>()

        // act
        viewModel.peopleResource.observeForever {
            results.add(it!!)
            defaultUnlock()
        }
        defaultLock()
        viewModel.refresh()
        defaultLock()

        // assert
        assertTrue(subscribed)
        assertEquals(results, listOf(resource1, resource2))
    }

    @Test
    fun constructor_reload_connectivityChange() {
        // arrange
        val resource1 = Resource.success(listOf(createRandomPerson()))
        val resource2 = Resource.success(listOf(createRandomPerson()))
        val peopleData = createLiveData(resource1)
        val cmProxy = object : ConnectivityManagerProxy {
            var handler: ((Boolean) -> Unit)? = null
            override fun registerNetworkStatusChangeCallback(
                connectionStatusChangeHandler: (connected: Boolean) -> Unit
            ) {
                handler = connectionStatusChangeHandler
            }

            override fun unregisterNetworkStatusChangeCallback(
                connectionStatusChangeHandler: (connected: Boolean) -> Unit
            ) = Unit

            override var isConnected: Boolean = false
                set(value) {
                    field = value
                    handler!!(value)
                }
        }
        val kodein = Kodein.lazy {
            bind<ConnectivityManagerProxy>() with instance(cmProxy)
            bind<PersonRepository>() with singleton {
                object : PersonRepository {
                    override fun loadPeople(): LiveData<Resource<List<Person>>> {
                        return peopleData
                    }

                    override fun refresh() {
                        peopleData.postValue(resource2)
                    }
                }
            }
        }
        val viewModel = MainViewModel(kodein)
        val results = ArrayList<Any>()

        // act
        viewModel.peopleResource.observeForever {
            results.add(it!!)
            defaultUnlock()
        }
        defaultLock()
        cmProxy.isConnected = true
        defaultLock()

        // assert
        assertEquals(results, listOf(resource1, resource2))
    }
}