package br.com.luizssb.esapienschallenge.dependencies

import br.com.luizssb.esapienschallenge.repository.ChallengeRepository
import br.com.luizssb.esapienschallenge.service.ChallengeService
import br.com.luizssb.esapienschallenge.service.RetrofitClientInstance
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

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