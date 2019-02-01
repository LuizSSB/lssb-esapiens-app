package br.com.luizssb.esapienschallenge.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Person")
data class Person(
    @PrimaryKey val username: String,
    @SerializedName("photo") val photoURL: String,
    val age: Int,
    val city: String,

    // Luiz: normally, one would use an enum for these props, however, due to
    // our current status as a society (as well as the products by and target
    // demographic of eSapiens), it seems more appropriate to leave them open.
    // This, however, is bound to cause localization conflicts, as the REST API
    // doesn't consider the request locale and returns everything in English.
    val gender: String,
    val sexualOrientation: String
)