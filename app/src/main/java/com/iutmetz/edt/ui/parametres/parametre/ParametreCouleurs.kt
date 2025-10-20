package com.iutmetz.edt.ui.parametres.parametre

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.MutableLiveData
import com.google.android.material.textfield.TextInputEditText
import com.iutmetz.edt.data.local.entity.SessionEntity
import com.iutmetz.edt.databinding.LayoutParametreCouleursBinding
import com.iutmetz.edt.databinding.PopupColorPickerBinding
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerView
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener


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

    private val colorPickerBinding =
        PopupColorPickerBinding.inflate(inflater, popupContent, true)

    val colorPicker: ColorPickerView = colorPickerBinding.colorPicker

    val tieColor: TextInputEditText = colorPickerBinding.tieColor

    @OptIn(ExperimentalStdlibApi::class)
    override fun initView() { // on initialise la vue
        colorPicker.attachBrightnessSlider(colorPickerBinding.brightnessSlide)

        binding.apply { // on initialise les interactions avec l'utilisateur
            colorPicker.setColorListener(object : ColorEnvelopeListener {
                override fun onColorSelected(envelope: ColorEnvelope, fromUser: Boolean) {
                    colorPickerBinding.llColor.setBackgroundColor(envelope.color)
                    tieColor.setText(envelope.hexCode.substring(2))
                }
            })

            tieColor.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    s?.let {
                        val replace = it.replace(Regex("[^0-9a-fA-F]|\\s"), "")
                        val crop = if (replace.length > 6) replace.removeRange(6, it.length)
                        else replace

                        if ("FF$it".uppercase() == colorPicker.color.toHexString()
                                .uppercase()
                        ) return@let
                        if (it.length == 6) colorPicker.setInitialColor("FF$it".hexToInt())

                        tieColor.removeTextChangedListener(this)
                        tieColor.setText(crop)
                        tieColor.setSelection(crop.length)
                        tieColor.addTextChangedListener(this)
                    }
                }
            })

            tieColor.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
                if (!hasFocus) {
                    val imm: InputMethodManager =
                        getSystemService(binding.root.context, InputMethodManager::class.java)!!
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
            }

            ibCours.setOnClickListener { // on affiche le color picker lorsque l'on clique sur le bouton
                onColorPicked = { color -> // on définit la fonction de callback
                    session!!.coursColor = color
                }
                showColorPicker(
                    true,
                    session!!.coursColor
                ) // on affiche le popup avec le color picker et le slider de luminosité
            }

            ibCoursText.setOnClickListener { // idem
                onColorPicked = { color ->
                    session!!.coursTextColor = color
                }
                showColorPicker(true, session!!.coursTextColor)
            }

            ibBandeau.setOnClickListener { // idem
                onColorPicked = { color ->
                    session!!.bandeauColor = color
                }
                showColorPicker(true, session!!.bandeauColor)
            }

            ibBandeauText.setOnClickListener { // idem
                onColorPicked = { color ->
                    session!!.bandeauTextColor = color
                }
                showColorPicker(true, session!!.bandeauTextColor)
            }

            ibSae.setOnClickListener { // idem
                onColorPicked = { color ->
                    session!!.saeColor = color
                }
                showColorPicker(true, session!!.saeColor)
            }

            ibSaeText.setOnClickListener { // idem
                onColorPicked = { color ->
                    session!!.saeTextColor = color
                }
                showColorPicker(true, session!!.saeTextColor)
            }

            ibTodayBackground.setOnClickListener { // idem
                onColorPicked = { color ->
                    session!!.todayBackgroundColor = color
                }
                showColorPicker(true, session!!.todayBackgroundColor)
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
            ibSae.setColorFilter(session.saeColor)
            ibSaeText.setColorFilter(session.saeTextColor)
            ibTodayBackground.setColorFilter(session.todayBackgroundColor)
        }
    }

    fun showColorPicker(
        show: Boolean,
        initialColor: Int
    ) { // cette fonction permet d'afficher ou non le color picker et de changer la fonction de callback du bouton de confirmation
        changePopupVisibility(show)
        if (show) {
            colorPickerBinding.root.visibility = View.VISIBLE
            colorPicker.setInitialColor(initialColor)
            changeConfirmCallback {
                onColorPicked(colorPicker.color)
                changeButtonsColor()
            }
        } else {
            colorPickerBinding.root.visibility = View.GONE
        }
    }
}