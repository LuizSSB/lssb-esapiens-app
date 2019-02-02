package br.com.luizssb.esapienschallenge.ui.main

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import br.com.luizssb.esapienschallenge.R
import br.com.luizssb.esapienschallenge.model.Person
import br.com.luizssb.esapienschallenge.ui.item.PersonGridCell

class PeopleAdapter(
    private val context: Context, private val _people: List<Person>?
) : BaseAdapter() {
    val people get() = _people ?: emptyList()

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val cellView: PersonGridCell
        if (p1 == null) {
            val cellSide = context.resources.getDimension(R.dimen.cell_person_side)
            cellView = PersonGridCell(context)
            cellView.layoutParams =
                AbsListView.LayoutParams(cellSide.toInt(), cellSide.toInt())
        } else {
            cellView = p1 as PersonGridCell
        }

        cellView.person = people[p0]
        return cellView
    }

    override fun getItem(p0: Int): Any {
        return people[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return people.size
    }
}