package br.com.luizssb.esapienschallenge.model

import com.google.gson.annotations.SerializedName

data class Person(
    val username: String,
    @SerializedName("photo") val photoURL: String,
    val age: Int,
    val city: String,

    // Luiz: normally, one would use an enum for these props, however, due to
    // our current status as a society (as well as the products by and target
    // demographic of eSapiens), it seems more appropriate to leave them open.
    val gender: String,
    val sexualOrientation: String
)