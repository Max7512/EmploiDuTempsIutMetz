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

class ParametreCouleurs( // cette classe génère un paramètre qui permet de changer les couleurs de certains éléments de l'app
    session: SessionEntity,
    popupContent: ConstraintLayout,
    inflater: LayoutInflater,
    parent: ViewGroup
) : Parametre(session, popupContent) {
    override val binding = LayoutParametreCouleursBinding.inflate(inflater, parent, true) // on initialise le binding
    private var onColorPicked = { color: Int -> } // cette fonction est appelée lorsque l'utilisateur choisit une couleur

    val colorPickerView: ColorPickerView = // on récupère la vue du color picker si elle existe sinon on en crée une nouvelle
        popupContent.findViewById(R.id.colorPicker)
            ?: ColorPickerView(binding.root.context).apply {
                visibility = View.GONE
                popupContent.addView(this)
            }

    override fun initView() { // on initialise la vue
        colorPickerView.setColorListener(object : ColorListener { // on définit le comportement du color picker
            override fun onColorSelected(color: Int, fromUser: Boolean) {
                if (fromUser) { // si l'utilisateur a choisi une couleur on l'applique
                    onColorPicked(color) // on appelle la fonction de callback
                    changePopupVisibility(false) // on cache le popup
                    changeButtonsColor() // on change la couleur des boutons
                }
            }
        })

        binding.apply { // on initialise les interactions avec l'utilisateur
            ibCours.setOnClickListener { // on affiche le color picker lorsque l'on clique sur le bouton
                onColorPicked = { color -> // on définit la fonction de callback
                    session.coursColor = color
                }
                changePopupVisibility(true) // on affiche le popup
                colorPickerView.visibility = View.VISIBLE // on affiche le color picker
            }

            ibCoursText.setOnClickListener { // idem
                onColorPicked = { color ->
                    session.coursTextColor = color
                }
                changePopupVisibility(true)
                colorPickerView.visibility = View.VISIBLE
            }

            ibBandeau.setOnClickListener { // idem
                onColorPicked = { color ->
                    session.bandeauColor = color
                }
                changePopupVisibility(true)
                colorPickerView.visibility = View.VISIBLE
            }

            ibBandeauText.setOnClickListener { // idem
                onColorPicked = { color ->
                    session.bandeauTextColor = color
                }
                changePopupVisibility(true)
                colorPickerView.visibility = View.VISIBLE
            }
        }

        changeButtonsColor()
    }

    fun changeButtonsColor() { // cette fonction permet de changer la couleur des boutons
        binding.apply { // on change la couleur des boutons
            ibCours.setColorFilter(session.coursColor)
            ibCoursText.setColorFilter(session.coursTextColor)
            ibBandeau.setColorFilter(session.bandeauColor)
            ibBandeauText.setColorFilter(session.bandeauTextColor)
        }
    }
}