package br.com.luizssb.esapienschallenge.service

import br.com.luizssb.esapienschallenge.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClientInstance {
    companion object {
        val retrofitClientInstance by lazy {
            Retrofit.Builder()
                .baseUrl(Constants.SERVICE_BASE_URI)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}