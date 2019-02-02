package br.com.luizssb.esapienschallenge.dependencies

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import br.com.luizssb.esapienschallenge.data.ChallengeDatabase
import br.com.luizssb.esapienschallenge.data.PersonDao
import br.com.luizssb.esapienschallenge.network.ConnectivityManagerProxy
import br.com.luizssb.esapienschallenge.network.ReachabilityReceiver
import br.com.luizssb.esapienschallenge.network.NougatPlusConnectivityManagerProxy
import br.com.luizssb.esapienschallenge.repository.PersonRepository
import br.com.luizssb.esapienschallenge.service.ChallengeService
import br.com.luizssb.esapienschallenge.service.RetrofitClientInstance
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import android.content.IntentFilter


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
    bind<PersonRepository>() with singleton {
        PersonRepository(instance(), instance())
    }
}


@DependencyModuleLoader
fun loadConnectivityManagerDependencies(app: Application) =
    Kodein.Module("connectivityManager") {
        bind<ConnectivityManagerProxy>() with singleton {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                NougatPlusConnectivityManagerProxy(
                    app.getSystemService(ConnectivityManager::class.java)
                )
            else {
                val receiver = ReachabilityReceiver(
                    app.getSystemService(Context.CONNECTIVITY_SERVICE)
                            as ConnectivityManager
                )
                val intentFilter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
                intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED")
                intentFilter.addAction("android.net.wifi.STATE_CHANGE")
                app.registerReceiver(receiver, intentFilter)

                receiver
            }
        }
    }
