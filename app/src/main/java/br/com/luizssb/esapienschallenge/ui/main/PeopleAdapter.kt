package br.com.luizssb.esapienschallenge.ui.main

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import br.com.luizssb.esapienschallenge.model.Person

class PeopleAdapter(
    private val context: Context, private val _people: List<Person>?
) : BaseAdapter() {
    val people get() = _people ?: emptyList()

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val imageView: TextView
        if (p1 == null) {
            // if it's not recycled, initialize some attributes
            imageView = TextView(context)
            imageView.layoutParams = ViewGroup.LayoutParams(85, 85)
            imageView.setPadding(8, 8, 8, 8)
        } else {
            imageView = p1 as TextView
        }

        imageView.text = people[p0].username
        return imageView
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