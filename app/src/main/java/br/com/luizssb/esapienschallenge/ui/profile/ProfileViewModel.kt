package br.com.luizssb.esapienschallenge.ui.profile

import android.app.Person
import br.com.luizssb.esapienschallenge.ui.InjectionDependentViewModel
import org.kodein.di.Kodein

// Luiz: truth be told, this view model is not really necessary, as the profile
// page only displays the data and does nothing else. However, I'm leaving it
// here because, you know, standards and stuff. Also, I wanna use the damn
// InjectionDependentViewMode.Factory, because it gave me far more trouble to
// write it than what I'd like and now I just wanna show off, meeeeeeeh.
class ProfileViewModel(kodein: Kodein, val person: Person)
    : InjectionDependentViewModel(kodein)
