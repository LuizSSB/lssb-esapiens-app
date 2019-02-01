package br.com.luizssb.esapienschallenge.repository

// Luiz: only "Result" would suffice, but there's already a class named that, so meh.
data class OperationResult<T>(val data: T?, val throwable: Throwable? = null)