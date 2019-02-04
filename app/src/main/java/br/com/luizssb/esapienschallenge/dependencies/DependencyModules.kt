package br.com.luizssb.esapienschallenge.dependencies

import android.arch.persistence.room.Room
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import br.com.luizssb.esapienschallenge.data.ChallengeDatabase
import br.com.luizssb.esapienschallenge.data.PersonDao
import br.com.luizssb.esapienschallenge.network.ConnectivityManagerProxy
import br.com.luizssb.esapienschallenge.network.NougatPlusConnectivityManagerProxy
import br.com.luizssb.esapienschallenge.network.ReachabilityReceiver
import br.com.luizssb.esapienschallenge.repository.PersonRepository
import br.com.luizssb.esapienschallenge.repository.PersonRepositoryImpl
import br.com.luizssb.esapienschallenge.service.ChallengeRestApi
import br.com.luizssb.esapienschallenge.service.PersonService
import br.com.luizssb.esapienschallenge.service.RetrofitClientInstance
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton


@DependencyModuleLoader
fun loadDatabaseDependencies(context: Context) = Kodein.Module("database") {
    val database = Room.databaseBuilder(
        context, ChallengeDatabase::class.java, "challengeDB"
    ).build()

    bind<PersonDao>() with singleton {
        database.personDao()
    }
}

@DependencyModuleLoader
fun loadRetrofitDependencies() = Kodein.Module("services") {
    val retrofit = RetrofitClientInstance.retrofitClientInstance
        .create(ChallengeRestApi::class.java)

    // Luiz: seems better to leave with a provider, instead of singleton, so as
    // to indicate the possibly stateful nature of the service.
    bind<PersonService>() with provider { PersonService(retrofit) }
}

@DependencyModuleLoader
fun loadRepositoryDependencies() = Kodein.Module("repositories") {
    bind<PersonRepository>() with singleton {
        PersonRepositoryImpl(instance(), instance())
    }
}

@DependencyModuleLoader
fun loadConnectivityManagerDependencies(context: Context) =
    Kodein.Module("connectivityManager") {
        bind<ConnectivityManagerProxy>() with singleton {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                NougatPlusConnectivityManagerProxy(
                    context.getSystemService(ConnectivityManager::class.java)
                )
            else {
                val receiver = ReachabilityReceiver(
                    context.getSystemService(Context.CONNECTIVITY_SERVICE)
                            as ConnectivityManager
                )
                val intentFilter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
                intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED")
                intentFilter.addAction("android.net.wifi.STATE_CHANGE")
                context.registerReceiver(receiver, intentFilter)

                receiver
            }
        }
    }
