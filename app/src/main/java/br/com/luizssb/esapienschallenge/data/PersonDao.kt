package br.com.luizssb.esapienschallenge.data

import android.arch.persistence.room.*
import br.com.luizssb.esapienschallenge.model.Person

@Dao
interface PersonDao {
    @Query("SELECT * FROM Person")
    fun getPeople(): List<Person>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savePeople(users: List<Person>)

    @Query("DELETE FROM Person")
    fun cleanRecords()
}