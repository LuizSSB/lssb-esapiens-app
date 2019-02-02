package br.com.luizssb.esapienschallenge.data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import br.com.luizssb.esapienschallenge.model.Person

@Dao
interface PersonDao {
    @Query("SELECT * FROM Person")
    fun getPeople(): LiveData<List<Person>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savePeople(users: List<Person>)

    @Query("DELETE FROM Person")
    fun cleanRecords()
}