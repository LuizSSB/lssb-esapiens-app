package br.com.luizssb.esapienschallenge

import android.app.Application
import br.com.luizssb.esapienschallenge.dependencies.DependencyManager
import org.kodein.di.KodeinAware

class ChallengeApplication : Application(), KodeinAware {
    override val kodein = DependencyManager.loadDependencies(this)
}