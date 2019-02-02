package br.com.luizssb.esapienschallenge.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import br.com.luizssb.esapienschallenge.model.Person

@Database(entities = [Person::class], version = 1, exportSchema = false)
abstract class ChallengeDatabase : RoomDatabase() {
    abstract fun personDao(): PersonDao
}