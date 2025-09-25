package com.iutmetz.edt.ui.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.iutmetz.edt.R

class ConfirmationDialog( // cette classe permet de créer une boite de dialogue de confirmation
    context: Context, // le contexte de l'application
    @StringRes title: Int, // le titre de la boite de dialogue
    @StringRes message: Int, // le message de la boite de dialogue
    onOkClicked: (() -> Unit)? = null, // la fonction à exécuter lorsque l'utilisateur clique sur le bouton "confirmer"
    onCancelClicked: (() -> Unit)? = null, // la fonction à exécuter lorsque l'utilisateur clique sur le bouton "annuler"
    onDismiss: (() -> Unit)? = null // la fonction à exécuter lorsque la boite de dialogue est fermée
) : MaterialAlertDialogBuilder(context) {

    init {
        val titleView: View = // on crée une vue pour le titre de la boite de dialogue
            LayoutInflater.from(context).inflate(R.layout.custom_dialog_title, null, false).apply {
                findViewById<TextView>(R.id.tvTitle)?.setText(title) // on met le titre dans la vue
            }
        setCustomTitle(titleView) // on met la vue dans la boite de dialogue en tant que titre

        setMessage(message) // on met le message dans la boite de dialogue

        setPositiveButton(R.string.confirmer) { _, _ -> onOkClicked?.invoke() } // on définit le message et le comportement du bouton de confirmation

        setNegativeButton(R.string.annuler) { _, _ -> onCancelClicked?.invoke() } // on définit le message et le comportement du bouton d'annulation

        setOnDismissListener { // on définit le comportement de la fermeture de la boite de dialogue
            onDismiss?.invoke()
        }
    }

    override fun show(): AlertDialog? { // cette fonction est appelée pour afficher la boite de dialogue
        val dialog = super.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.resources.getColor(R.color.lightBlue)) // on change la couleur du bouton de confirmation et d'annulation
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.resources.getColor(R.color.lightBlue))

        return dialog
    }
}