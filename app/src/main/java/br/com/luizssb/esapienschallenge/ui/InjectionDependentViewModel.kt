package br.com.luizssb.esapienschallenge.ui

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import org.kodein.di.Kodein

abstract class InjectionDependentViewModel(protected val kodein: Kodein) : ViewModel() {

    /**
     * Factory to instantiate a view model while injecting the Kodein container
     * in the constructor, as well as any other constructor parameters it might
     * have.
     * Not really necessary, as Kodein can store values and stuff besides the
     * dependencies, however, this approach avoids using tags, constants and so.
     * It unfortunately has no way to compile-time check if the supplied
     * arguments indeed fit one of the available constructors, however that's
     * the trade-off you get for the commodity of not having to implement a
     * factory for every view model.
     *
     * Should probably be refactored to be able to instantiate any kind of view
     * model, but since we only use subclasses of this one here, there's no real
     * reason for that.
     *
     * Has a problem with constructor parameters that are mapped to primitive
     * types in the JVM (Kotlin's non-nullable Int, Double, Boolean, Char, etc).
     * To use one of these, the constructor parameter MUST be nullable,
     * otherwise Kotlin will translate it as a primitive JVM type and will have
     * problems mathinc parameter types. In this case, the @NonNull annotation
     * may help to indicate that the parameter should, well, not be null.
     *
     * @author Luiz
     */
    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val kodein: Kodein, private vararg val args: Any
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            // Luiz: I'm pretty sure there's a better way of doing this.
            val params = args.toMutableList()
            params.add(0, kodein)

            val constructors = modelClass.constructors
            for (constructor in constructors) {
                // Luiz: other, more suitable, properties are only avaliable on
                // API 26+.
                val ctorParamTypes = constructor.parameterTypes

                if (ctorParamTypes.size == params.size) {
                    var ctorMatches = true

                    // Luiz: is there no '..<' operator? swift much better kthx
                    for (idx in 0..(ctorParamTypes.size - 1)) {
                        val param = params[idx]
                        val ctorParamType = ctorParamTypes[idx]
                        if (!ctorParamType.isInstance(param)) {
                            ctorMatches = false
                            break
                        }
                    }

                    if (ctorMatches) {
                        return constructor
                            .newInstance(*params.toTypedArray()) as T
                    }
                }

            }

            val argsTypes = params.joinToString(separator = ", ") {
                it::class.java.simpleName
            }

            throw IllegalArgumentException(
                "Tried to instance a ${modelClass.simpleName} as ${modelClass.simpleName}($argsTypes)"
            )
        }
    }
}