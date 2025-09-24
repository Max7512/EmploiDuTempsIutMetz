package com.iutmetz.edt.ui.parametres.parametre

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.iutmetz.edt.R
import com.iutmetz.edt.data.local.entity.SessionEntity
import com.iutmetz.edt.databinding.LayoutParametreCouleursBinding
import com.skydoves.colorpickerview.ColorPickerView
import com.skydoves.colorpickerview.listeners.ColorListener

class ParametreCouleurs(
    session: SessionEntity,
    popupContent: ConstraintLayout,
    inflater: LayoutInflater,
    parent: ViewGroup
) : Parametre(session, popupContent) {
    override val binding = LayoutParametreCouleursBinding.inflate(inflater, parent, true)
    private var onColorPicked = { color: Int -> }

    val colorPickerView: ColorPickerView =
        popupContent.findViewById(R.id.colorPicker)
            ?: ColorPickerView(binding.root.context).apply {
                visibility = View.GONE
                popupContent.addView(this)
            }

    override fun initView() {
        colorPickerView.setColorListener(object : ColorListener {
            override fun onColorSelected(color: Int, fromUser: Boolean) {
                if (fromUser) {
                    onColorPicked(color)
                    changePopupVisibility(false)
                    changeButtonsColor()
                }
            }
        })

        binding.apply {
            ibCours.setOnClickListener {
                onColorPicked = { color ->
                    session.coursColor = color
                }
                changePopupVisibility(true)
                colorPickerView.visibility = View.VISIBLE
            }

            ibCoursText.setOnClickListener {
                onColorPicked = { color ->
                    session.coursTextColor = color
                }
                changePopupVisibility(true)
                colorPickerView.visibility = View.VISIBLE
            }

            ibBandeau.setOnClickListener {
                onColorPicked = { color ->
                    session.bandeauColor = color
                }
                changePopupVisibility(true)
                colorPickerView.visibility = View.VISIBLE
            }

            ibBandeauText.setOnClickListener {
                onColorPicked = { color ->
                    session.bandeauTextColor = color
                }
                changePopupVisibility(true)
                colorPickerView.visibility = View.VISIBLE
            }
        }
    }

    fun changeButtonsColor() {
        binding.apply {
            ibCours.setColorFilter(session.coursColor)
            ibCoursText.setColorFilter(session.coursTextColor)
            ibBandeau.setColorFilter(session.bandeauColor)
            ibBandeauText.setColorFilter(session.bandeauTextColor)
        }
    }
}