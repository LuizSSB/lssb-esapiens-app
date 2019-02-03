package br.com.luizssb.esapienschallenge.repository

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import br.com.luizssb.esapienschallenge.model.Status
import br.com.luizssb.esapienschallenge.service.ApiResponse
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

// Luiz: now, let's be honest here: these tests are a mess, too much duplicated
// code, I know. The thing is, though, that I tried to improve stuff, by creating
// an instance of NetworkBoundResource that took care of the duplicated stuff and
// that should be anonynously extended used by the tests to implement their
// specifics. However, for some reason, all properties of that class ALWAYS
// ended up null, even though they were not optional! I have no idea why that
// happened, but it forced me to leave things as they currently are :/
class NetworkBoundResourceUnitTest {
    enum class ResourceAction {
        CREATE_CALL,
        LOAD_FROM_DB,
        SHOULD_FETCH,
        SAVE_CALL_RESULT,
        FETCH_FAILED
    }

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private fun <T> createLiveData(value: T): MutableLiveData<T> {
        val data = MutableLiveData<T>()
        data.postValue(value)
        return data
    }

    private fun randomString(): String = Math.random().toString()

    private fun createStringLiveData(value: String = randomString()): MutableLiveData<String> {
        return createLiveData(value)
    }


    @Test
    fun constructor_rightOrder_fetchSuccess() {
        // arrange
        val performedActions = ArrayList<ResourceAction>()
        val receivedResults = ArrayList<Status>()
        var timesLoadedfromDb = 0
        val countDownLatch = CountDownLatch(1)
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
                Assert.fail("not supposed to happen on successful request")
            }
        }

        // act
        res.asLiveData().observeForever {
            receivedResults.add(it!!.status)
        }
        countDownLatch.await(1, TimeUnit.SECONDS)

        // assert
        Assert.assertEquals(
            performedActions,
            arrayListOf(
                ResourceAction.LOAD_FROM_DB,
                ResourceAction.SHOULD_FETCH,
                ResourceAction.CREATE_CALL,
                ResourceAction.SAVE_CALL_RESULT,
                ResourceAction.LOAD_FROM_DB
            )
        )
        Assert.assertEquals(
            receivedResults,
            arrayListOf(Status.LOADING, Status.SUCCESS)
        )
    }

    @Test
    fun constructor_rightOrder_fetchError() {
        // arrange
        val performedActions = ArrayList<ResourceAction>()
        val countDownLatch = CountDownLatch(1)
        val receivedResults = ArrayList<Status>()
        val res = object : NetworkBoundResource<String, String>() {
            override fun saveCallResult(item: String) {
                Assert.fail("not supposed to happen on failed request")
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
        res.asLiveData().observeForever { receivedResults.add(it!!.status) }
        countDownLatch.await(1, TimeUnit.SECONDS)

        // assert
        Assert.assertEquals(
            performedActions,
            arrayListOf(
                ResourceAction.LOAD_FROM_DB,
                ResourceAction.SHOULD_FETCH,
                ResourceAction.CREATE_CALL,
                ResourceAction.FETCH_FAILED
            )
        )
        Assert.assertEquals(receivedResults, arrayListOf(Status.LOADING, Status.ERROR))
    }

    @Test
    fun constructor_rightOrder_noServiceCall() {
        // arrange
        val performedActions = ArrayList<ResourceAction>()
        val countDownLatch = CountDownLatch(1)
        val receivedResults = ArrayList<Status>()
        val res = object : NetworkBoundResource<String, String>() {
            override fun saveCallResult(item: String) {
                Assert.fail("not supposed to happen on non requesting resource")
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
                Assert.fail("not supposed to happen on non requesting resource")
                return createLiveData(ApiResponse<String>(null, Exception()))
            }

            override fun onFetchFailed() {
                Assert.fail("not supposed to happen on non requesting resource")
            }
        }

        // act
        res.asLiveData().observeForever { receivedResults.add(it!!.status) }
        countDownLatch.await(1, TimeUnit.SECONDS)

        // assert
        Assert.assertEquals(
            performedActions,
            arrayListOf(ResourceAction.LOAD_FROM_DB, ResourceAction.SHOULD_FETCH)
        )
        Assert.assertEquals(receivedResults, arrayListOf(Status.SUCCESS))
    }

    @Test
    fun constructor_rightOrder_doubleFetchSuccess() {
        // arrange
        val performedActions = ArrayList<ResourceAction>()
        val receivedResults = ArrayList<Status>()
        var timesLoadedfromDb = 0
        val countDownLatch = CountDownLatch(2)
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
                Assert.fail("not supposed to happen on successful request")
            }
        }

        // act
        res.asLiveData().observeForever {
            receivedResults.add(it!!.status)
        }
        countDownLatch.await(1, TimeUnit.SECONDS)
        timesLoadedfromDb = 0
        res.loadUp(forceFromNetwork = true)
        countDownLatch.await(1, TimeUnit.SECONDS)

        // assert
        Assert.assertEquals(
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
        Assert.assertEquals(
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
        val countDownLatch = CountDownLatch(2)
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
                Assert.fail("not supposed to happen on successful request")
            }
        }

        // act
        res.asLiveData().observeForever {
            receivedResults.add(it!!.status)
        }
        countDownLatch.await(1, TimeUnit.SECONDS)
        res.loadUp(forceFromNetwork = false)
        countDownLatch.await(1, TimeUnit.SECONDS)

        // assert
        Assert.assertEquals(
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
        Assert.assertEquals(
            receivedResults,
            arrayListOf(
                Status.LOADING, Status.SUCCESS, // 1st fetch
                Status.SUCCESS // 2nd fetch
            )
        )
    }
}