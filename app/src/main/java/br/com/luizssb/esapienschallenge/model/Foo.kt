package br.com.luizssb.esapienschallenge.model

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Insert

@Entity
data class Foo (val id: Int)

@Dao
interface Dao {
    @Insert
    fun addFoo(foo: Foo)
}