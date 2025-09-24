package com.iutmetz.edt.ui.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.iutmetz.edt.R

class ConfirmationDialog(
    context: Context,
    @StringRes title: Int,
    @StringRes message: Int,
    onOkClicked: (() -> Unit)? = null,
    onCancelClicked: (() -> Unit)? = null,
    onDismiss: (() -> Unit)? = null
) : MaterialAlertDialogBuilder(context) {

    init {
        val titleView: View =
            LayoutInflater.from(context).inflate(R.layout.custom_dialog_title, null, false).apply {
                findViewById<TextView>(R.id.tvTitle)?.setText(title)
            }
        setCustomTitle(titleView)

        setMessage(message)

        setPositiveButton(R.string.confirmer) { _, _ -> onOkClicked?.invoke() }

        setNegativeButton(R.string.annuler) { _, _ -> onCancelClicked?.invoke() }

        setOnDismissListener {
            onDismiss?.invoke()
        }
    }

    override fun show(): AlertDialog? {
        val dialog = super.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.resources.getColor(R.color.lightBlue))
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.resources.getColor(R.color.lightBlue))

        return dialog
    }
}