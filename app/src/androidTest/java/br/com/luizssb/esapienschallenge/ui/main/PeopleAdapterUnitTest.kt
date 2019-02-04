package br.com.luizssb.esapienschallenge.ui.main

import android.support.test.InstrumentationRegistry
import br.com.luizssb.esapienschallenge.createRandomPerson
import br.com.luizssb.esapienschallenge.ui.item.PersonGridCell
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert.*
import org.junit.Test

class PeopleAdapterUnitTest {
    @Test
    fun getView_reusesCell() {
        // arrange
        val people = listOf(createRandomPerson(), createRandomPerson())
        val adapter = PeopleAdapter(
            InstrumentationRegistry.getTargetContext(), people
        )

        // act
        val cell1 = adapter.getView(0, null, null)
        val cell2 = adapter.getView(1, cell1, null)

        // assert
        assertSame(cell1, cell2)
        assertThat(cell1, instanceOf(PersonGridCell::class.java))
        assertEquals((cell1 as PersonGridCell).person, people[1])
    }

    @Test
    fun getItem_implementedAsItShould() {
        // arrange
        val people = listOf(createRandomPerson(), createRandomPerson())
        val adapter = PeopleAdapter(
            InstrumentationRegistry.getTargetContext(), people
        )

        // act

        // assert
        assertSame(adapter.getItem(0), people[0])
        assertSame(adapter.getItem(1), people[1])
    }
}