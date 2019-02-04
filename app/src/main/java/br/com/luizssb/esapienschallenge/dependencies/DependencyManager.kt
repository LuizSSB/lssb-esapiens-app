package br.com.luizssb.esapienschallenge.dependencies

import android.app.Application
import android.content.Context
import org.kodein.di.Kodein
import org.kodein.di.android.androidModule

class DependencyManager private constructor() {
    companion object {
        fun loadDependencies(forApplication: Context) = Kodein.lazy {
            // Luiz: ideally, I would like for this to be done automatically, by
            // finding and calling all functions annotated with
            // @DependencyModuleLoader via reflection. However, the JVM's and
            // Kotlin's reflection capabilities are apparently limited enough
            // for this to not be possible. Oh, welp.
            // Another possibility would be to generate this file by annotation
            // processing, but that just seems like overkill right now.
            import(loadDatabaseDependencies(forApplication))
            import(loadRetrofitDependencies())
            import(loadRepositoryDependencies())
            import(loadConnectivityManagerDependencies(forApplication))

            // Luiz: safety check because in the tests we can't get the app context.
            if (forApplication is Application) {
                import(androidModule(forApplication))
            }
        }
    }
}