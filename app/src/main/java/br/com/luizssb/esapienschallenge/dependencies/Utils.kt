package br.com.luizssb.esapienschallenge.dependencies

import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import br.com.luizssb.esapienschallenge.ui.InjectionDependentViewModel
import org.kodein.di.KodeinAware

inline fun <reified TVM : InjectionDependentViewModel, T> T.viewModel(
    vararg params: Any
) where T : KodeinAware, T : FragmentActivity = lazy {
    ViewModelProviders.of(
        this,
        InjectionDependentViewModel.Factory(this.kodein, *params)
    ).get(TVM::class.java)
}

inline fun <reified TVM : InjectionDependentViewModel, T> T.viewModel(
    vararg params: Any
) where T : KodeinAware, T : Fragment = lazy {
    ViewModelProviders.of(
        this,
        InjectionDependentViewModel.Factory(this.kodein, *params)
    ).get(TVM::class.java)
}
