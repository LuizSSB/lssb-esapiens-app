package br.com.luizssb.esapienschallenge.ui

import android.support.annotation.NonNull
import org.junit.Assert.*
import org.junit.Test
import org.kodein.di.Kodein

class InjectionDependentViewModelFactoryUnitTest {
    private val kodein = Kodein.lazy { }

    class OnlyKodeinViewModel(kodein: Kodein) : InjectionDependentViewModel(kodein)

    @Test
    fun create_onlyKodein() {
        // arrange
        val factory = InjectionDependentViewModel.Factory(kodein)

        // act
        try {
            factory.create(OnlyKodeinViewModel::class.java)
        } catch (ex: IllegalArgumentException) {
            fail(ex.message)
        }

        // assert
    }

    class RepeatedParamTypeViewModel(
        kodein: Kodein, val str1: String, val str2: String
    ) : InjectionDependentViewModel(kodein)

    @Test
    fun create_repeatedParamType() {
        // arrange
        val str1 = "foo"
        val str2 = "bar"
        val factory = InjectionDependentViewModel.Factory(kodein, str1, str2)

        try {
            // act
            val viewModel = factory.create(RepeatedParamTypeViewModel::class.java)

            // assert
            assertEquals(viewModel.str1, str1)
            assertEquals(viewModel.str2, str2)
        } catch (ex: IllegalArgumentException) {
            fail(ex.message)
        }
    }

    class JVMPrimitiveMappedParamViewModel(kodein: Kodein, @NonNull val value: Int?) :
        InjectionDependentViewModel(kodein)

    @Test
    fun create_jvmPrimitiveMappedParam() {
        // arrange
        val value = 3
        val factory = InjectionDependentViewModel.Factory(kodein, value)

        try {
            // act
            val viewModel = factory.create(JVMPrimitiveMappedParamViewModel::class.java)

            // assert
            assertEquals(viewModel.value, value)
        } catch (ex: IllegalArgumentException) {
            fail(ex.message)
        }
    }

    class ComplexObjectParamViewModel(
        kodein: Kodein,
        val test: InjectionDependentViewModelFactoryUnitTest?
    ) : InjectionDependentViewModel(kodein)

    @Test
    fun create_complexObjectParamViewModel() {
        // arrange
        val factory = InjectionDependentViewModel.Factory(kodein, this)

        try {
            // act
            val viewModel = factory.create(ComplexObjectParamViewModel::class.java)

            // assert
            assertEquals(viewModel.test, this)
        } catch (ex: IllegalArgumentException) {
            fail(ex.message)
        }
    }

    class MultiCtorViewModel : InjectionDependentViewModel {
        val str: String?
        val value1: Int?
        val value2: Int?

        constructor(kodein: Kodein, str: String) : super(kodein) {
            this.str = str
            value1 = null
            value2 = null
        }

        constructor(kodein: Kodein, value1: Int?, value2: Int?) : super(kodein) {
            this.str = null
            this.value1 = value1
            this.value2 = value2
        }
    }

    @Test
    fun create_multiCtor() {
        // arrange
        val str = "goo"
        val value1 = 1
        val value2 = 2
        val factory1 = InjectionDependentViewModel.Factory(kodein, str)
        val factory2 = InjectionDependentViewModel.Factory(kodein, value1, value2)

        try {
            // act
            val viewModel1 = factory1.create(MultiCtorViewModel::class.java)
            val viewModel2 = factory2.create(MultiCtorViewModel::class.java)

            // assert
            assertEquals(viewModel1.str, str)
            assertNull(viewModel1.value1)
            assertNull(viewModel1.value2)
            assertEquals(viewModel2.value1, value1)
            assertEquals(viewModel2.value2, value2)
            assertNull(viewModel2.str)
        } catch (ex: IllegalArgumentException) {
            fail(ex.message)
        }
    }
}