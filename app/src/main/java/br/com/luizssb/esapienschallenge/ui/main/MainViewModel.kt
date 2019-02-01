package br.com.luizssb.esapienschallenge.ui.main

import br.com.luizssb.esapienschallenge.service.ChallengeService
import br.com.luizssb.esapienschallenge.ui.InjectionDependentViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

class MainViewModel(kodein: Kodein) : InjectionDependentViewModel(kodein) {
    val service: ChallengeService by kodein.instance()
}
