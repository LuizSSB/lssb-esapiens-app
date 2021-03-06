package br.com.luizssb.esapienschallenge.service

import br.com.luizssb.esapienschallenge.Constants.Companion.SERVICE_BASE_URL
import br.com.luizssb.esapienschallenge.Constants.Companion.SERVICE_KEY_API
import br.com.luizssb.esapienschallenge.Constants.Companion.SERVICE_TIMEOUT_SECONDS
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit


class RetrofitClientInstance {
    companion object {
        val retrofitClientInstance: Retrofit by lazy {
            val httpClient = OkHttpClient.Builder()
                .addInterceptor(object : Interceptor {
                    @Throws(IOException::class)
                    override fun intercept(chain: Interceptor.Chain): Response {
                        return chain.proceed(
                            chain.request()
                            .newBuilder()
                            .addHeader("X-API-Key", SERVICE_KEY_API)
                            .build()
                        )
                    }
                })
                .readTimeout(SERVICE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .connectTimeout(SERVICE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .build()

            Retrofit.Builder()
                .baseUrl(SERVICE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build()
        }
    }
}