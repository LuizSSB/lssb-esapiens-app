package br.com.luizssb.esapienschallenge.dependencies

import org.kodein.di.Kodein

class DependencyManager private constructor() {
    companion object {
        fun loadDependencies() = Kodein.lazy {
            // Luiz: ideally, I would like for this to be done automatically, by
            // finding and calling all functions annotated with
            // @DependencyModuleLoader via reflection. However, the JVM's and
            // Kotlin's reflection capabilities are apparently limited enough
            // for this to not be possible. Oh, welp.
            // Another possibility would be to generate this file by annotation
            // processing, but that seems just overkill right now.
            import(loadRetrofitDependencies())
            import(loadRepositoryDependencies())
        }
    }
}