package br.com.luizssb.esapienschallenge.repository

import android.arch.lifecycle.LiveData
import br.com.luizssb.esapienschallenge.LiveDataUnitTest
import br.com.luizssb.esapienschallenge.createLiveData
import br.com.luizssb.esapienschallenge.createStringLiveData
import br.com.luizssb.esapienschallenge.model.Status
import br.com.luizssb.esapienschallenge.randomString
import br.com.luizssb.esapienschallenge.service.ApiResponse
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test

// Luiz: now, let's be honest here: these tests are a mess, too much duplicated
// code, I know. The thing is, though, that I tried to improve stuff, by creating
// an instance of NetworkBoundResource that took care of the duplicated stuff and
// that should be anonynously extended used by the tests to implement their
// specifics. However, for some reason, all properties of that class ALWAYS
// ended up null, even though they were not optional! I have no idea why that
// happened, but it forced me to leave things as they currently are :/
class NetworkBoundResourceUnitTest : LiveDataUnitTest() {
    enum class ResourceAction {
        CREATE_CALL, LOAD_FROM_DB, SHOULD_FETCH, SAVE_CALL_RESULT, FETCH_FAILED
    }

    @Test
    fun constructor_rightOrder_fetchSuccess() {
        // arrange
        val performedActions = ArrayList<ResourceAction>()
        val receivedResults = ArrayList<Status>()
        var timesLoadedfromDb = 0
        val res = object : NetworkBoundResource<String, String>() {
            override fun saveCallResult(item: String) {
                performedActions.add(ResourceAction.SAVE_CALL_RESULT)
            }

            override fun shouldFetch(data: String?): Boolean {
                performedActions.add(ResourceAction.SHOULD_FETCH)
                return true
            }

            override fun loadFromDb(): LiveData<String> {
                performedActions.add(ResourceAction.LOAD_FROM_DB)
                timesLoadedfromDb += 1
                if (timesLoadedfromDb == 2) {
                    defaultUnlock()

                }
                return createStringLiveData()
            }

            override fun createCall(): LiveData<ApiResponse<String>> {
                performedActions.add(ResourceAction.CREATE_CALL)
                return createLiveData(ApiResponse(randomString(), null))
            }

            override fun onFetchFailed() {
                super.onFetchFailed()
                fail("not supposed to happen on successful request")
            }
        }

        // act
        res.asLiveData.observeForever {
            receivedResults.add(it!!.status)
        }
        defaultLock()

        // assert
        assertEquals(
            performedActions,
            arrayListOf(
                ResourceAction.LOAD_FROM_DB,
                ResourceAction.SHOULD_FETCH,
                ResourceAction.CREATE_CALL,
                ResourceAction.SAVE_CALL_RESULT,
                ResourceAction.LOAD_FROM_DB
            )
        )
        assertEquals(
            receivedResults,
            arrayListOf(Status.LOADING, Status.SUCCESS)
        )
    }

    @Test
    fun constructor_rightOrder_fetchError() {
        // arrange
        val performedActions = ArrayList<ResourceAction>()
        val receivedResults = ArrayList<Status>()
        val res = object : NetworkBoundResource<String, String>() {
            override fun saveCallResult(item: String) {
                fail("not supposed to happen on failed request")
            }

            override fun shouldFetch(data: String?): Boolean {
                performedActions.add(ResourceAction.SHOULD_FETCH)
                return true
            }

            override fun loadFromDb(): LiveData<String> {
                performedActions.add(ResourceAction.LOAD_FROM_DB)
                return createStringLiveData("bar")
            }

            override fun createCall(): LiveData<ApiResponse<String>> {
                performedActions.add(ResourceAction.CREATE_CALL)
                return createLiveData(ApiResponse<String>(null, Exception()))
            }

            override fun onFetchFailed() {
                performedActions.add(ResourceAction.FETCH_FAILED)
                countDownLatch.countDown()
            }
        }

        // act
        res.asLiveData.observeForever { receivedResults.add(it!!.status) }
        defaultLock()

        // assert
        assertEquals(
            performedActions,
            arrayListOf(
                ResourceAction.LOAD_FROM_DB,
                ResourceAction.SHOULD_FETCH,
                ResourceAction.CREATE_CALL,
                ResourceAction.FETCH_FAILED
            )
        )
        assertEquals(receivedResults, arrayListOf(Status.LOADING, Status.ERROR))
    }

    @Test
    fun constructor_rightOrder_noServiceCall() {
        // arrange
        val performedActions = ArrayList<ResourceAction>()
        val receivedResults = ArrayList<Status>()
        val res = object : NetworkBoundResource<String, String>() {
            override fun saveCallResult(item: String) {
                fail("not supposed to happen on non requesting resource")
            }

            override fun shouldFetch(data: String?): Boolean {
                performedActions.add(ResourceAction.SHOULD_FETCH)
                return false
            }

            override fun loadFromDb(): LiveData<String> {
                performedActions.add(ResourceAction.LOAD_FROM_DB)
                return createLiveData("bar")
            }

            override fun createCall(): LiveData<ApiResponse<String>> {
                fail("not supposed to happen on non requesting resource")
                return createLiveData(ApiResponse<String>(null, Exception()))
            }

            override fun onFetchFailed() {
                fail("not supposed to happen on non requesting resource")
            }
        }

        // act
        res.asLiveData.observeForever { receivedResults.add(it!!.status) }
        defaultLock()

        // assert
        assertEquals(
            performedActions,
            arrayListOf(ResourceAction.LOAD_FROM_DB, ResourceAction.SHOULD_FETCH)
        )
        assertEquals(receivedResults, arrayListOf(Status.SUCCESS))
    }

    @Test
    fun constructor_rightOrder_doubleFetchSuccess() {
        // arrange
        val performedActions = ArrayList<ResourceAction>()
        val receivedResults = ArrayList<Status>()
        var timesLoadedfromDb = 0
        val res = object : NetworkBoundResource<String, String>() {
            override fun saveCallResult(item: String) {
                performedActions.add(ResourceAction.SAVE_CALL_RESULT)
            }

            override fun shouldFetch(data: String?): Boolean {
                performedActions.add(ResourceAction.SHOULD_FETCH)
                return true
            }

            override fun loadFromDb(): LiveData<String> {
                performedActions.add(ResourceAction.LOAD_FROM_DB)
                timesLoadedfromDb += 1
                if (timesLoadedfromDb == 2) {
                    countDownLatch.countDown()

                }
                return createStringLiveData()
            }

            override fun createCall(): LiveData<ApiResponse<String>> {
                performedActions.add(ResourceAction.CREATE_CALL)
                return createLiveData(ApiResponse(randomString(), null))
            }

            override fun onFetchFailed() {
                super.onFetchFailed()
                fail("not supposed to happen on successful request")
            }
        }

        // act
        res.asLiveData.observeForever {
            receivedResults.add(it!!.status)
        }
        defaultLock()
        timesLoadedfromDb = 0
        res.loadUp(forceFromNetwork = true)
        defaultLock()

        // assert
        assertEquals(
            performedActions,
            arrayListOf(
                // 1st fetch
                ResourceAction.LOAD_FROM_DB,
                ResourceAction.SHOULD_FETCH,
                ResourceAction.CREATE_CALL,
                ResourceAction.SAVE_CALL_RESULT,
                ResourceAction.LOAD_FROM_DB,

                // 2nd fetch
                ResourceAction.LOAD_FROM_DB,
                ResourceAction.CREATE_CALL,
                ResourceAction.SAVE_CALL_RESULT,
                ResourceAction.LOAD_FROM_DB
            )
        )
        assertEquals(
            receivedResults,
            arrayListOf(
                Status.LOADING, Status.SUCCESS, // 1st fetch
                Status.LOADING, Status.SUCCESS // 2nd fetch
            )
        )
    }

    @Test
    fun constructor_rightOrder_fetchThenDb() {
        // arrange
        val performedActions = ArrayList<ResourceAction>()
        val receivedResults = ArrayList<Status>()
        var timesLoadedfromDb = 0
        val res = object : NetworkBoundResource<String, String>() {
            override fun saveCallResult(item: String) {
                performedActions.add(ResourceAction.SAVE_CALL_RESULT)
            }

            override fun shouldFetch(data: String?): Boolean {
                performedActions.add(ResourceAction.SHOULD_FETCH)
                return timesLoadedfromDb == 1
            }

            override fun loadFromDb(): LiveData<String> {
                performedActions.add(ResourceAction.LOAD_FROM_DB)
                timesLoadedfromDb += 1
                if (timesLoadedfromDb == 2 || timesLoadedfromDb == 3) {
                    countDownLatch.countDown()

                }
                return createStringLiveData()
            }

            override fun createCall(): LiveData<ApiResponse<String>> {
                performedActions.add(ResourceAction.CREATE_CALL)
                return createLiveData(ApiResponse(randomString(), null))
            }

            override fun onFetchFailed() {
                super.onFetchFailed()
                fail("not supposed to happen on successful request")
            }
        }

        // act
        res.asLiveData.observeForever {
            receivedResults.add(it!!.status)
        }
        defaultLock()
        res.loadUp(forceFromNetwork = false)
        defaultLock()

        // assert
        assertEquals(
            performedActions,
            arrayListOf(
                // 1st fetch
                ResourceAction.LOAD_FROM_DB,
                ResourceAction.SHOULD_FETCH,
                ResourceAction.CREATE_CALL,
                ResourceAction.SAVE_CALL_RESULT,
                ResourceAction.LOAD_FROM_DB,

                // 2nd fetch
                ResourceAction.LOAD_FROM_DB,
                ResourceAction.SHOULD_FETCH
            )
        )
        assertEquals(
            receivedResults,
            arrayListOf(
                Status.LOADING, Status.SUCCESS, // 1st fetch
                Status.SUCCESS // 2nd fetch
            )
        )
    }
}