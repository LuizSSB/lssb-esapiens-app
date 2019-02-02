package br.com.luizssb.esapienschallenge.ui.main

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import br.com.luizssb.esapienschallenge.R
import br.com.luizssb.esapienschallenge.model.Person
import br.com.luizssb.esapienschallenge.ui.item.PersonGridCell

class PeopleAdapter(private val context: Context, people: List<Person> = emptyList()) : BaseAdapter() {
    var people: List<Person> = people
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val cellView: PersonGridCell
        if (p1 == null) {
            val cellSide = context.resources.getDimension(R.dimen.size_cell_person)
            cellView = PersonGridCell(context)
            cellView.layoutParams =
                AbsListView.LayoutParams(cellSide.toInt(), cellSide.toInt())
        } else {
            cellView = p1 as PersonGridCell
        }

        cellView.person = people[p0]
        return cellView
    }

    override fun getItem(p0: Int): Any = people[p0]

    override fun getItemId(p0: Int): Long = p0.toLong()

    override fun getCount(): Int = people.size
}