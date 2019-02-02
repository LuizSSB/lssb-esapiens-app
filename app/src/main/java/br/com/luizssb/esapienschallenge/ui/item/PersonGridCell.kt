package br.com.luizssb.esapienschallenge.ui.item

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import br.com.luizssb.esapienschallenge.R
import br.com.luizssb.esapienschallenge.model.Person
import br.com.luizssb.esapienschallenge.ui.extension.loadRemoteImage
import kotlinx.android.synthetic.main.cell_person.view.*


class PersonGridCell : FrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        // Luiz: while the correct approach would be to use Kodein for this,
        // as far as my knowledge goes, this would require different ctors,
        // which would make it impossible to instantiate this view the layout
        // XMLs. Useless as keeping this capability is, I'd rather have it, as a
        // matter of view standardization.
        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.cell_person, this, true)
    }

    var person: Person? = null
        set(value) {
            field = value
            label_city.text = value?.city
            label_username.text = value?.username
            label_gender.text = value?.gender
            label_sexuality.text = value?.sexualOrientation
            label_age.text = value?.age.toString()
            image_profile.loadRemoteImage(context, value?.photoURL)
        }
}