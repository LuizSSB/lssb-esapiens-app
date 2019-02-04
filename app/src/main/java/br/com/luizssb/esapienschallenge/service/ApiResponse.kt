package br.com.luizssb.esapienschallenge.service

// Luiz: note to self: both can be null, in order to support requests with no
// response (silly me).
data class ApiResponse<T>(val data: T?, val error: Throwable?) {
    val succeeded get() = error == null
}