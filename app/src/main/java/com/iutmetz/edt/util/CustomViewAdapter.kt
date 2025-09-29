package com.iutmetz.edt.util

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.google.android.material.textview.MaterialTextView


class CustomViewAdapter<T>(
    context: Context,
    resource: Int,
    objects: List<T>,
    var backgroundColor: Int,
    var textColor: Int
): ArrayAdapter<T>(context, resource, objects) {
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent) as MaterialTextView
        view.setBackgroundColor(backgroundColor)
        view.setTextColor(textColor)
        return view
    }
}