package br.com.luizssb.esapienschallenge.dependencies

import br.com.luizssb.esapienschallenge.repository.ChallengeRepository
import android.app.Application
import android.arch.persistence.room.Room
import br.com.luizssb.esapienschallenge.data.ChallengeDatabase
import br.com.luizssb.esapienschallenge.data.PersonDao
import br.com.luizssb.esapienschallenge.service.ChallengeService
import br.com.luizssb.esapienschallenge.service.RetrofitClientInstance
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

@DependencyModuleLoader
fun loadDatabaseDependencies(app: Application) = Kodein.Module("database") {
    val database = Room.databaseBuilder(
        app, ChallengeDatabase::class.java, "challengeDB"
    ).build()

    bind<PersonDao>() with singleton {
        database.personDao()
    }
}

@DependencyModuleLoader
fun loadRetrofitDependencies() = Kodein.Module("services") {
    bind<ChallengeService>() with singleton {
        RetrofitClientInstance.retrofitClientInstance
            .create(ChallengeService::class.java)
    }
}

@DependencyModuleLoader
fun loadRepositoryDependencies() = Kodein.Module("repositories") {
    bind<ChallengeRepository>() with singleton {
        ChallengeRepository(instance())
    }
}