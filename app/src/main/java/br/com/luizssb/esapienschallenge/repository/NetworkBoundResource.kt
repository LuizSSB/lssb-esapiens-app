package br.com.luizssb.esapienschallenge.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.support.annotation.MainThread
import android.support.annotation.WorkerThread
import br.com.luizssb.esapienschallenge.model.Resource
import br.com.luizssb.esapienschallenge.service.ApiResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// Adapted from:
// https://github.com/googlesamples/android-architecture-components/blob/master/GithubBrowserSample/app/src/main/java/com/android/example/github/repository/NetworkBoundResource.kt
abstract class NetworkBoundResource<ResultType, RequestType> {
    private val result = MediatorLiveData<Resource<ResultType>>()

    init {
        @Suppress("LeakingThis")
        loadUp()
    }

    fun loadUp(forceFromNetwork: Boolean = false) {
        val dbSource = loadFromDb()
        result.addSource(dbSource) { data ->
            result.removeSource(dbSource)
            if (forceFromNetwork || shouldFetch(data)) {
                setValue(Resource.loading(data))
                fetchFromNetwork(dbSource)
            } else {
                result.addSource(dbSource) { newData ->
                    setValue(Resource.success(newData))
                }
            }
        }
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        val apiResponse = createCall()

        result.removeSource(dbSource)
        result.addSource(dbSource) { newData ->
            setValue(Resource.loading(newData))
        }

        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            result.removeSource(dbSource)

            when {
                response == null ->
                    result.addSource(dbSource) { newData ->
                        result.removeSource(dbSource)
                        setValue(Resource.success(newData))
                    }

                response.succeeded -> {
                    GlobalScope.launch {
                        saveCallResult(response.data!!)
                        val sauce = loadFromDb()
                        result.addSource(sauce) { newData ->
                            result.removeSource(sauce)
                            setValue(Resource.success(newData))
                        }
                    }
                }

                else -> {
                    onFetchFailed()
                    result.addSource(dbSource) { newData ->
                        result.removeSource(dbSource)
                        setValue(Resource.error(response.error!!, newData))
                    }
                }

            }
        }
    }

    protected open fun onFetchFailed() {}

    fun asLiveData() = result as LiveData<Resource<ResultType>>

    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType)

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>
}