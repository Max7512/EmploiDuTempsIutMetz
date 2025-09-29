package com.iutmetz.edt.ui.parametres.parametre

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import com.iutmetz.edt.R
import com.iutmetz.edt.data.local.entity.SessionEntity
import com.iutmetz.edt.databinding.LayoutParametreCouleursBinding
import com.skydoves.colorpickerview.ColorPickerView
import com.skydoves.colorpickerview.listeners.ColorListener
import com.skydoves.colorpickerview.sliders.BrightnessSlideBar

class ParametreCouleurs( // cette classe génère un paramètre qui permet de changer les couleurs de certains éléments de l'app
    session: SessionEntity,
    popupContent: ConstraintLayout,
    onConfirmLiveData: MutableLiveData<() -> Unit>,
    inflater: LayoutInflater,
    parent: ViewGroup
) : Parametre(session, popupContent, onConfirmLiveData) {
    override val binding =
        LayoutParametreCouleursBinding.inflate(inflater, parent, true) // on initialise le binding
    private var onColorPicked =
        { color: Int -> } // cette fonction est appelée lorsque l'utilisateur choisit une couleur

    val colorPickerView: ColorPickerView =
        popupContent.findViewById(R.id.colorPicker)!! // on récupère la vue du color picker

    val brightnessSlideBar: BrightnessSlideBar =
        popupContent.findViewById(R.id.brightnessSlide)!! // on récupère la vue du slider de luminosité

    override fun initView() { // on initialise la vue
        colorPickerView.attachBrightnessSlider(brightnessSlideBar)

        binding.apply { // on initialise les interactions avec l'utilisateur
            ibCours.setOnClickListener { // on affiche le color picker lorsque l'on clique sur le bouton
                onColorPicked = { color -> // on définit la fonction de callback
                    session!!.coursColor = color
                }
                showColorPicker(true) // on affiche le popup avec le color picker et le slider de luminosité
            }

            ibCoursText.setOnClickListener { // idem
                onColorPicked = { color ->
                    session!!.coursTextColor = color
                }
                showColorPicker(true)
            }

            ibBandeau.setOnClickListener { // idem
                onColorPicked = { color ->
                    session!!.bandeauColor = color
                }
                showColorPicker(true)
            }

            ibBandeauText.setOnClickListener { // idem
                onColorPicked = { color ->
                    session!!.bandeauTextColor = color
                }
                showColorPicker(true)
            }
        }

        changeButtonsColor()
    }

    fun changeButtonsColor() { // cette fonction permet de changer la couleur des boutons
        binding.apply { // on change la couleur des boutons
            ibCours.setColorFilter(session!!.coursColor)
            ibCoursText.setColorFilter(session.coursTextColor)
            ibBandeau.setColorFilter(session.bandeauColor)
            ibBandeauText.setColorFilter(session.bandeauTextColor)
        }
    }

    fun showColorPicker(show: Boolean) { // cette fonction permet d'afficher ou non le color picker et de changer la fonction de callback du bouton de confirmation
        changePopupVisibility(show)
        if (show) {
            colorPickerView.visibility = View.VISIBLE
            brightnessSlideBar.visibility = View.VISIBLE
            changeConfirmCallback {
                onColorPicked(colorPickerView.color)
                changeButtonsColor()
            }
        }
    }
}