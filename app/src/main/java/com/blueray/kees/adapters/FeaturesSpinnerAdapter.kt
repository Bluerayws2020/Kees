package com.blueray.kees.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.blueray.kees.R
import com.blueray.kees.helpers.ViewUtils.hide
import com.blueray.kees.model.Option

class FeaturesSpinnerAdapter(context: Context, val list: List<Option>) :
    ArrayAdapter<Option>(context, R.layout.spinner_item, list) {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: inflater.inflate(R.layout.spinner_item, parent, false)
        setupView(view, position)
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: inflater.inflate(R.layout.spinner_item, parent, false)
        setupView(view, position)
        return view
    }

    private fun setupView(view: View, position: Int) {
        view.findViewById<ImageView>(R.id.icon)?.hide()
        view.findViewById<TextView>(R.id.title)?.text = list[position].name
    }
}
