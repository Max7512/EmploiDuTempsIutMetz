package com.iutmetz.edt.ui.parametres.parametre;

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.lifecycle.MutableLiveData
import androidx.viewbinding.ViewBinding
import com.iutmetz.edt.data.local.entity.SessionEntity

abstract class Parametre( // cette classe permet de créer un modèle pour afficher les paramètres, elle est abstraite car elle ne peut pas être instanciée
    protected val session: SessionEntity, // la session utilisée par le view model des paramètres
    protected val popupContent: ConstraintLayout, // le layout du popup où certains outils peuvent être utilisés
    protected val onConfirmLiveData: MutableLiveData<() -> Unit> // un live data qui permet de changer la fonction de callback du bouton de confirmation
) {
    protected abstract val binding: ViewBinding // on définit un binding qui est un objet qui permet de lier les éléments de la vue avec le code, ce binding peut être de n'importe quel layout

    abstract fun initView() // cette fonction permet de définir l'initialisation de la vue

    protected fun changePopupVisibility(visible: Boolean) { // cette fonction permet de changer la visibilité du popup
        if (!visible) { // si le popup ne doit pas être visible on cache tous les éléments du popup
            popupContent.children.forEach {
                it.visibility = View.GONE
            }
        }

        (popupContent.parent as View).visibility = if (visible) View.VISIBLE else View.GONE // on change la visibilité du popup
    }

    protected fun changeConfirmCallback(callback: () -> Unit) { // cette fonction permet de changer la fonction de callback du bouton de confirmation
        onConfirmLiveData.value = callback
    }
}
