package br.com.luizssb.esapienschallenge.service

data class ApiResponse<T>(val data: T?, val error: Throwable?) {
    val succeeded get() = error == null
}